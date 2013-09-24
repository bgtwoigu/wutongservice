package com.borqs.server.wutong.account2.util;


import org.apache.commons.lang.ObjectUtils;

public class ObjectHelper {
    public static int hashCode(Object... a) {
        int hash = 1;
        if (a != null) {
            for (Object o : a) {
                hash = hash * 31 + ObjectUtils.hashCode(o);
            }
        }
        return hash;
    }
}
