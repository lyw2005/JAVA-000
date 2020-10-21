package com.example.demo.geek.java.week01;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.lang.reflect.Method;

public class MyClassloader extends ClassLoader {

    public static void main(String[] args) {
        try {
            Class<?> helloClass = new MyClassloader().findClass("Hello");
            if (null != helloClass) {
                Object obj = helloClass.getDeclaredConstructor().newInstance();
                Method method = helloClass.getMethod("hello");
                method.invoke(obj);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    protected Class<?> findClass(String name) {
        try (FileInputStream f = new FileInputStream("D:\\java\\hello\\hello.xlass")) {
            byte[] bytes = new byte[f.available()];
            f.read(bytes);
            for (int i = 0; i < bytes.length; i++) {
                bytes[i] = (byte) (255 - bytes[i]);
            }
            return defineClass(name, bytes, 0, bytes.length);
        } catch (IOException e) {
            System.err.println("I/O Error: " + e);
            return null;
        }
    }
}
