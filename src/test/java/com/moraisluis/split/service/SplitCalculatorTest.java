package com.moraisluis.split.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.List;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

class SplitCalculatorTest {

    @Test
    void splitEqually_withRemainder_givesExtraCentToFirst() {
        assertEquals(List.of(334L, 333L, 333L), SplitCalculator.splitEqually(1000, 3));
    }

    @Test
    void splitEqually_withoutRemainder_givesEqualParts() {
        assertEquals(List.of(500L, 500L), SplitCalculator.splitEqually(1000, 2));
    }

    @Test
    void splitEqually_withSinglePart_returnsThatPart() {
        assertEquals(List.of(1000L), SplitCalculator.splitEqually(1000, 1));
    }

    @Test
    void splitEqually_with0Parts_throwsIllegalArgumentException() {
        assertThrows(IllegalArgumentException.class, () -> SplitCalculator.splitEqually(1000, 0));
    }
    
    @Test
    void splitEqually_1CentBy3Parts_givesCentToFirstOnly() {
        assertEquals(List.of(1L, 0L, 0L), SplitCalculator.splitEqually(1, 3));
    }

    @Test
    void splitEqually_2CentsBy3Parts_givesCentToFirstTwo() {
        assertEquals(List.of(1L, 1L, 0L), SplitCalculator.splitEqually(2, 3));
    }

    @Test
    void splitEqually_8CentsBy3Parts_givesExtraCentToFirstTwo() {
        assertEquals(List.of(3L, 3L, 2L), SplitCalculator.splitEqually(8, 3));
    }

    @Test
    void splitEqually_20CentsBy7Parts_givesExtraCentToFirstSix() {
        assertEquals(List.of(3L, 3L, 3L, 3L, 3L, 3L, 2L), SplitCalculator.splitEqually(20, 7));
    }

    @ParameterizedTest
    @CsvSource({"1000, 3", "8, 3", "20, 7", "9999, 7", "1, 5"})
    @DisplayName("The sum of the parts should equal the total amount")
    void sumOfPartsEqualsTotalAmount(Long input, int expected) {
        long totalAmount = input;
        int numberOfParts = expected;
        List<Long> parts = SplitCalculator.splitEqually(totalAmount, numberOfParts);
        long sumOfParts = parts.stream().mapToLong(Long::longValue).sum();
        assertEquals(totalAmount, sumOfParts, "The sum of the parts should equal the total amount");
    }

    @ParameterizedTest
    @CsvSource({"1000, 3", "8, 3", "20, 7", "9999, 7", "1, 5"})
    @DisplayName("The difference between the max and min parts should be at most 1")
    void differenceBetweenMaxAndMinPartsIsAtMostOne(Long input, int expected) {
        long totalAmount = input;
        int numberOfParts = expected;
        List<Long> parts = SplitCalculator.splitEqually(totalAmount, numberOfParts);
        long max = parts.stream().mapToLong(Long::longValue).max().orElse(0L);
        long min = parts.stream().mapToLong(Long::longValue).min().orElse(0L);
        long difference = max - min;
        assertTrue(difference <= 1, "The difference between the max and min parts should be at most 1");
    }
}
