package com.moraisluis.split.service;

import java.util.ArrayList;
import java.util.List;

public final class SplitCalculator {
    public static List<Long> splitEqually(long totalCents, int parts) {
        if (parts <= 0) {
            throw new IllegalArgumentException("Number of parts must be greater than zero");
        }

        long baseAmount = totalCents / parts;
        int remainder = (int) (totalCents % parts);


        // Creating a List of Longs using ArrayList
        List<Long> partList = new ArrayList<>();

        // Iterating through the list
        for (int i = 0; i < parts; i++) {
            if (i < remainder) {
                partList.add(baseAmount + 1);
            } else {
                partList.add(baseAmount);
            }
        }

        return partList;
    }
}