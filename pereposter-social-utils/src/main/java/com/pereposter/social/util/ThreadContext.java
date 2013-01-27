package com.pereposter.social.util;

import java.util.ArrayList;
import java.util.List;

public class ThreadContext {

    private static final ThreadLocal<ThreadContext> threadLocal = new ThreadLocal<ThreadContext>();

    private List<String> warnings = new ArrayList<String>();
    private List<String> errors = new ArrayList<String>();

    public static ThreadContext currentContext() {
        ThreadContext context = threadLocal.get();

        if (context == null) {
            context = new ThreadContext();
            threadLocal.set(context);
        }

        return context;
    }

    static void removeContext() {
        threadLocal.remove();
    }

    public void cleanContext() {
        warnings.clear();
        errors.clear();
    }

    public void addWarning(String warning) {
        warnings.add(warning);
    }

    public void addError(String error) {
        errors.add(error);
    }

    public List<String> getWarnings() {
        return warnings;
    }

    public List<String> getErrors() {
        return errors;
    }

    public boolean hasErrors() {
        return errors.size() > 0;
    }

    public boolean hasWarning() {
        return warnings.size() > 0;
    }

}
