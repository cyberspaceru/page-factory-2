package ru.context.utils;

public class TestUtils {
    public static void sleep(int milliseconds, String explanationMessage) {
        try {
            Thread.sleep(milliseconds);
        } catch (InterruptedException e) {
            throw new AssertionError("InterruptedException occurred while calling Thread.sleep for " + milliseconds + " milliseconds. " + explanationMessage, e);
        }
    }
}
