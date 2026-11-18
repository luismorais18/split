package com.moraisluis.split.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.util.List;

import org.junit.jupiter.api.Test;

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
    void splitUnequally_withoutRemainder_givesCentToFirst() {
        assertEquals(List.of(1L, 0L, 0L), SplitCalculator.splitEqually(1, 3));
    }

    @Test
    void splitUnequally_withoutRemainder_givesCentToFirstTwo() {
        assertEquals(List.of(1L, 1L, 0L), SplitCalculator.splitEqually(2, 3));
    }

    @Test
    void splitUnequally_withRemainder_gives3CentsToFirstTwo() {
        assertEquals(List.of(3L, 3L, 2L), SplitCalculator.splitEqually(8, 3));
    }

    @Test
    void splitUnequally_withRemainder_gives20CentsforFirstTwo() {
        assertEquals(List.of(3L, 3L, 3L, 3L, 3L, 3L, 2L), SplitCalculator.splitEqually(20, 7));
    }


}
