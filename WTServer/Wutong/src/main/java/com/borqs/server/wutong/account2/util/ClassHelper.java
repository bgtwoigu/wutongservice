package com.borqs.server.wutong.account2.util;


import com.borqs.server.ServerException;
import com.borqs.server.base.BaseErrors;

import java.lang.reflect.Method;

public class ClassHelper {
    public static Class forName(String className) {
        try {
            return Class.forName(className);
        } catch (ClassNotFoundException e) {
            throw new ServerException(BaseErrors.PLATFORM_CLASS_NOT_FOUND_ERROR, e);
        }
    }

    public static Class forNameSafe(String className) {
        try {
            return Class.forName(className);
        } catch (ClassNotFoundException e) {
            return null;
        }
    }

    public static Object newInstance(Class clazz) {
        try {
            return clazz.newInstance();
        } catch (InstantiationException e) {
            throw new ServerException(BaseErrors.PLATFORM_CLASS_NOT_FOUND_ERROR, e);
        } catch (IllegalAccessException e) {
            throw new ServerException(BaseErrors.PLATFORM_CLASS_NOT_FOUND_ERROR, e);
        }
    }

    public static Object newInstance(String className) {
        return newInstance(forName(className));
    }


    public static boolean classExists(String className) {
        try {
            Class.forName(className);
            return true;
        } catch (ClassNotFoundException e) {
            return false;
        }
    }

    public static Method getMethod(Class clazz, String method, Class<?>... paramTypes) {
        try {
            return clazz.getMethod(method, paramTypes);
        } catch (NoSuchMethodException e) {
            throw new ServerException(BaseErrors.PLATFORM_CLASS_NOT_FOUND_ERROR, e);
        }
    }

    public static Method getMethodNoThrow(Class clazz, String method, Class<?>... paramTypes) {
        try {
            return clazz.getMethod(method, paramTypes);
        } catch (NoSuchMethodException e) {
            return null;
        }
    }

    public static Object invoke(Method method, Object instance, Object... params) {
        try {
            return method.invoke(instance, params);
        } catch (Throwable t) {
            throw new ServerException(BaseErrors.PLATFORM_CLASS_NOT_FOUND_ERROR, t);
        }
    }

    public static Object invokeNoThrow(Method method, Object instance, Object... params) {
        try {
            return method.invoke(instance, params);
        } catch (Throwable t) {
            return null;
        }
    }
}
