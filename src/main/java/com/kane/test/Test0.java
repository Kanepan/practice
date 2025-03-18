package com.kane.test;

public class Test0 {
    private final static String costThreshold = "5000";

    private static final String IMCI_HINT_TEMPLATE = "/*+ SET_VAR(cost_threshold_for_imci=%s) */";

    public static void main(String[] args) {
        String imciHint = String.format(IMCI_HINT_TEMPLATE, costThreshold);
        System.out.println(imciHint);
    }
}
