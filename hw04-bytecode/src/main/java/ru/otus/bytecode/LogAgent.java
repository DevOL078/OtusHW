package ru.otus.bytecode;

import javassist.ClassPool;
import javassist.CtClass;
import javassist.CtMethod;
import javassist.NotFoundException;
import org.objectweb.asm.*;
import ru.otus.bytecode.annotations.Log;
import ru.otus.bytecode.annotations.WithLog;

import java.io.FileOutputStream;
import java.io.OutputStream;
import java.lang.instrument.ClassFileTransformer;
import java.lang.instrument.Instrumentation;
import java.lang.invoke.CallSite;
import java.lang.invoke.MethodHandles;
import java.lang.invoke.MethodType;
import java.lang.invoke.StringConcatFactory;
import java.security.ProtectionDomain;

import static org.objectweb.asm.Opcodes.H_INVOKESTATIC;

public class LogAgent {

    public static void premain(String agentArgs, Instrumentation inst) {
        inst.addTransformer(new ClassFileTransformer() {
            @Override
            public byte[] transform(ClassLoader loader, String className,
                                    Class<?> classBeingRedefined,
                                    ProtectionDomain protectionDomain,
                                    byte[] classfileBuffer) {
                try {
                    ClassPool classPool = ClassPool.getDefault();
                    String classNamePoint = className.replaceAll("/", ".");
                    CtClass cc = classPool.get(classNamePoint);
                    if (cc.hasAnnotation(WithLog.class)) {
                        return addProxyMethods(cc, classfileBuffer);
                    }
                } catch (NotFoundException e) {
                }
                return classfileBuffer;
            }
        });
    }

    private static byte[] addProxyMethods(CtClass clazz, byte[] originalClass) throws NotFoundException {
        CtMethod[] methods = clazz.getDeclaredMethods();
        byte[] result = originalClass;
        for (CtMethod method : methods) {
            if (method.hasAnnotation(Log.class)) {
                result = addProxyMethod(clazz, method, result);
            }
        }

        return result;
    }

    private static byte[] addProxyMethod(CtClass ctClass, CtMethod method, byte[] classBuffer) throws NotFoundException {
        ClassReader classReader = new ClassReader(classBuffer);
        ClassWriter classWriter = new ClassWriter(classReader, ClassWriter.COMPUTE_MAXS);
        ClassVisitor classVisitor = new ClassVisitor(Opcodes.ASM5, classWriter) {
            @Override
            public MethodVisitor visitMethod(int access, String name, String descriptor, String signature, String[] exceptions) {
                if (name.equals(method.getName())) {
                    return super.visitMethod(access, name + "Proxied", descriptor, signature, exceptions);
                } else {
                    return super.visitMethod(access, name, descriptor, signature, exceptions);
                }
            }
        };
        classReader.accept(classVisitor, Opcodes.ASM5);

        MethodVisitor mv = classWriter.visitMethod(Opcodes.ACC_PUBLIC, method.getName(), method.getSignature(), null, null);

        Handle handle = new Handle(
                H_INVOKESTATIC,
                Type.getInternalName(StringConcatFactory.class),
                "makeConcatWithConstants",
                MethodType.methodType(CallSite.class, MethodHandles.Lookup.class, String.class, MethodType.class, String.class, Object[].class).toMethodDescriptorString(),
                false);

        CtClass[] parametersTypes = method.getParameterTypes();
        int parametersCount = parametersTypes.length;

        mv.visitFieldInsn(Opcodes.GETSTATIC, "java/lang/System", "out", "Ljava/io/PrintStream;");
        mv.visitLdcInsn("expected method: " + method.getName());
        mv.visitMethodInsn(Opcodes.INVOKEVIRTUAL, "java/io/PrintStream", "print", "(Ljava/lang/String;)V", false);

        if (parametersCount > 0) {
            mv.visitFieldInsn(Opcodes.GETSTATIC, "java/lang/System", "out", "Ljava/io/PrintStream;");
            mv.visitLdcInsn(", params:");
            mv.visitMethodInsn(Opcodes.INVOKEVIRTUAL, "java/io/PrintStream", "print", "(Ljava/lang/String;)V", false);

            for (int i = 0; i < parametersCount; ++i) {
                mv.visitFieldInsn(Opcodes.GETSTATIC, "java/lang/System", "out", "Ljava/io/PrintStream;");
                mv.visitVarInsn(Opcodes.ALOAD, i + 1);
                mv.visitMethodInsn(Opcodes.INVOKEVIRTUAL, "java/lang/Object", "toString", "()Ljava/lang/String;", false);
                mv.visitInvokeDynamicInsn("makeConcatWithConstants", "(Ljava/lang/String;)Ljava/lang/String;", handle, " (\u0001)");
                mv.visitMethodInsn(Opcodes.INVOKEVIRTUAL, "java/io/PrintStream", "print", "(Ljava/lang/String;)V", false);
            }
        }
        mv.visitFieldInsn(Opcodes.GETSTATIC, "java/lang/System", "out", "Ljava/io/PrintStream;");
        mv.visitLdcInsn("");
        mv.visitMethodInsn(Opcodes.INVOKEVIRTUAL, "java/io/PrintStream", "println", "(Ljava/lang/String;)V", false);

        mv.visitVarInsn(Opcodes.ALOAD, 0);
        for (int i = 0; i < parametersCount; ++i) {
            mv.visitVarInsn(Opcodes.ALOAD, i + 1);
        }

        mv.visitMethodInsn(Opcodes.INVOKEVIRTUAL, ctClass.getName().replaceAll("\\.", "/"), method.getName() + "Proxied", method.getSignature(), false);

        mv.visitInsn(Opcodes.RETURN);
        mv.visitMaxs(0, 0);
        mv.visitEnd();


        byte[] finalClass = classWriter.toByteArray();

        try (OutputStream fos = new FileOutputStream("proxy.class")) {
            fos.write(finalClass);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return finalClass;
    }

}
