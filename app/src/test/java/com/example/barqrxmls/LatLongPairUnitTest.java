package com.example.barqrxmls;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

import java.util.ArrayList;

/**
 * tests the LatLongPairs container
 */

public class LatLongPairUnitTest {

    /**
     * tests the creation of a LatLongPair
     */
    @Test
    public void create() {
        LatLongPair test = new LatLongPair(1.9,1.8);
        double first = test.first;
        double second = test.second;
        assertEquals(1.9, first);
        assertEquals(1.8, second);
    }

    /**
     * tests the ability to add a LatLongPair to the Code
     * tests the ability to get a LatLongPair from the Code
     */
    @Test
    public void checkAddableToCode() {
        LatLongPair test = new LatLongPair(1.9,1.8);
        ArrayList<LatLongPair> store = new ArrayList<>();
        store.add(test);
        Code code = new Code("test");
        code.setLatLongPairs(store);
        assertEquals(store,code.getLatLongPairs());
    }
}
