package ru.otus.annotations.framework.runner;

import ru.otus.annotations.framework.annotations.After;
import ru.otus.annotations.framework.annotations.Before;
import ru.otus.annotations.framework.annotations.Test;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

public class TestRunner {

    private Class<?> clazz;
    private final List<String> beforeMethodsNames = new ArrayList<>();
    private final List<String> testMethodsNames = new ArrayList<>();
    private final List<String> afterMethodsNames = new ArrayList<>();

    public TestRunner(String className) throws ClassNotFoundException {
        this.clazz = Class.forName(className);
        findMethods();
    }

    private void findMethods() {
        Method[] methods = clazz.getDeclaredMethods();
        for(Method method : methods) {
            if(method.isAnnotationPresent(Before.class)) {
                beforeMethodsNames.add(method.getName());
            }
            else if(method.isAnnotationPresent(Test.class)) {
                testMethodsNames.add(method.getName());
            }
            else if(method.isAnnotationPresent(After.class)) {
                afterMethodsNames.add(method.getName());
            }
        }
    }

    public void run() {
        for(String methodName : testMethodsNames) {
            Object instance = null;
            try {
                instance = clazz.getDeclaredConstructor().newInstance();
                runBefore(instance);
                runOneTest(instance, methodName);
                System.out.println("Method " + methodName + ": OK");
            } catch (Exception e) {
                System.err.println("Exception in method " + methodName);
                System.err.println(e.getMessage());
            } finally {
                if(instance != null) {
                    runAfter(instance);
                }
            }
            System.out.println("===========================");
        }
    }

    private void runBefore(Object object) throws Exception {
        for(String methodName: beforeMethodsNames) {
            callMethod(object, methodName);
        }
    }

    private void runAfter(Object object) {
        for(String methodName: afterMethodsNames) {
            try {
                callMethod(object, methodName);
            } catch (Exception e) {
                System.err.println("Exception in AFTER method " + methodName);
            }
        }
    }

    private void runOneTest(Object object, String testMethodName) throws Exception {
        callMethod(object, testMethodName);
    }

    private void callMethod(Object object, String methodName) throws Exception {
        Method method = null;
        try {
            method = object.getClass().getDeclaredMethod(methodName);
            method.setAccessible(true);
            method.invoke(object);
        } catch (NoSuchMethodException e) {
            System.err.println("No method: " + methodName);
        } catch (IllegalAccessException e) {
            System.err.println("No access to method " + methodName);
        } finally {
            if(method != null && method.canAccess(object)) {
                method.setAccessible(false);
            }
        }
    }

}
