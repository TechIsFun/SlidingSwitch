package com.github.techisfun.slidingswitch;

/**
 * Class to log errors in production
 */
final class CrashLibrary {

    private CrashLibrary() {
        // not instantiable
    }

    static void log(int priority, String tag, String message) {

    }

    static void logError(Throwable throwable) {

    }

    static void logWarning(Throwable throwable) {

    }
}