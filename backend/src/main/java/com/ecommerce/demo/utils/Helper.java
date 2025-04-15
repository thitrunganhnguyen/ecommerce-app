package com.ecommerce.demo.utils;

// While using Helper.notNull() can make sense in some scenarios
// where you want to centralize null checks or apply some custom logic around it (like logging or throwing an exception),
// it's unnecessary here if all you're doing is checking for null.
public class Helper {
    public static boolean notNull(Object obj) {
        return obj != null;
    }
}
