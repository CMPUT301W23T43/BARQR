package com.example.barqrxmls;

import android.util.Pair;

import java.io.Serializable;

public class LatLongPair implements Serializable {
    public Double first, second;
    /**
     * Constructor for a Pair.
     *
     * @param first  the first object in the Pair
     * @param second the second object in the pair
     */
    public LatLongPair(Double first, Double second) {
        this.first = first;
        this.second = second;
    }

    public LatLongPair() {
//        super(0.0, 0.0);
    }
}
