package ru.otus.bytecode.done;

import javassist.ClassPool;
import javassist.CtClass;
import javassist.CtMethod;
import javassist.NotFoundException;
import org.objectweb.asm.*;

import java.io.FileOutputStream;
import java.io.OutputStream;
import java.lang.instrument.ClassFileTransformer;
import java.lang.instrument.Instrumentation;
import java.lang.invoke.CallSite;
import java.lang.invoke.MethodHandles;
import java.lang.invoke.MethodType;
import java.security.ProtectionDomain;

import static org.objectweb.asm.Opcodes.H_INVOKESTATIC;

public class LogAgent {

    public static void premain(String agentArgs, Instrumentation inst) {
        System.out.println("LOG PREMAIN");
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
                    if(cc.hasAnnotation(WithLog.class)) {
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
        for(CtMethod method : methods) {
            if(method.hasAnnotation(Log.class)) {
                result = addProxyMethod(method, result);
            }
        }

        return result;
    }

    private static byte[] addProxyMethod(CtMethod method, byte[] classBuffer) {
        ClassReader classReader = new ClassReader(classBuffer);
        ClassWriter classWriter = new ClassWriter(classReader, ClassWriter.COMPUTE_MAXS);
        ClassVisitor classVisitor = new ClassVisitor(Opcodes.ASM5, classWriter) {
            @Override
            public MethodVisitor visitMethod(int access, String name, String descriptor, String signature, String[] exceptions) {
                return super.visitMethod(access, name + "Proxied", descriptor, signature, exceptions);
            }
        };
        classReader.accept(classVisitor, Opcodes.ASM5);

        System.out.println(method.getName());
        System.out.println(method.getSignature());
        System.out.println("==============================");

        return classBuffer;
    }

}
