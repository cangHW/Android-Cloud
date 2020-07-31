package com.proxy.service.api;

import java.lang.annotation.Annotation;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.lang.reflect.GenericArrayType;
import java.lang.reflect.Method;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.lang.reflect.TypeVariable;
import java.lang.reflect.WildcardType;
import java.util.ArrayList;

/**
 * @author : cangHX
 * on 2020/07/28  6:01 PM
 */
public class Test {

    @Target(ElementType.METHOD)
    @Retention(RetentionPolicy.RUNTIME)
    public @interface A {
    }

    @Target(ElementType.PARAMETER)
    @Retention(RetentionPolicy.RUNTIME)
    public @interface B {
        String value();
    }

    @Target(ElementType.PARAMETER)
    @Retention(RetentionPolicy.RUNTIME)
    public @interface C {
        String value();
    }

    public static class CCC{
        static {
            System.out.println("CCC static");
        }
        public CCC(){
            System.out.println("new");
        }
    }

    public static void main(String[] args) {
//        checkMethod();
//        try {
//            checkType();
//        } catch (NoSuchMethodException e) {
//            e.printStackTrace();
//        }

        new CCC();
        new CCC();
        new CCC();
    }

    private static void checkType() throws NoSuchMethodException {
        ArrayList<String> strings = new ArrayList<>();
        System.out.println(strings.getClass().getTypeParameters()[0].getTypeName());
        System.out.println(strings.getClass().getTypeName());


        ArrayList<Test> tests = new ArrayList<>();

        Method method = Test.class.getDeclaredMethod("xx");
        Class<?> tClass = method.getReturnType();
        System.out.println(tClass);
        Type type = method.getGenericReturnType();
        System.out.println(type);
        if (type instanceof Class){
            System.out.println("class");
        }else if (type instanceof ParameterizedType){
            System.out.println("ParameterizedType");
            ParameterizedType parameterizedType = (ParameterizedType) type;
            System.out.println(parameterizedType.getActualTypeArguments()[0].getTypeName());
            System.out.println(parameterizedType.getOwnerType());
            System.out.println(parameterizedType.getRawType());

        }else if (type instanceof GenericArrayType){
            System.out.println("GenericArrayType");
        }else if (type instanceof TypeVariable){
            System.out.println("TypeVariable");
        }else if (type instanceof WildcardType){
            System.out.println("WildcardType");
        }
    }

    private static <T extends Test> T xx(){
        return null;
    }

    private static void checkMethod(){
        try {
            Method[] methods = Test.class.getMethods();
            Method method = null;
            for (Method method1 : methods){
                if (method1.getName().equals("XXX")){
                    method = method1;
                    break;
                }
            }

            Class<?> returnClass = method.getReturnType();
            System.out.println("returnClass : " + returnClass.getCanonicalName());
            System.out.println("  ");

            Annotation[] annotations = method.getAnnotations();
            for (Annotation annotation : annotations) {
                System.out.println("Annotation[] : " + annotation.toString());
            }
            System.out.println("  ");

            Type[] types = method.getGenericParameterTypes();
            for (Type type : types) {
                System.out.println("Type[] : " + type.toString());
            }
            System.out.println("  ");

            Annotation[][] annotations1 = method.getParameterAnnotations();
            for (Annotation[] annotations2 : annotations1) {
                System.out.println("Annotation[][] : start");
                for (Annotation annotation : annotations2) {
                    System.out.println("Annotation[][] : " + annotation.toString());
                }
            }
            System.out.println("  ");

            Class<?>[] classes = method.getParameterTypes();
            for (Class aClass : classes) {
                System.out.println("Class<?>[] : " + aClass.toString());
            }
            System.out.println("  ");
        } catch (Throwable e) {
            e.printStackTrace();
        }
    }

    @A
    public static void XXX(@B("asd") String asd, @C("qwe") String qwe) {
    }

}
