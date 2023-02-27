package com.example.barqrxmls;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Code {

    private String hash;
    private String points;
    private String name;





    /**
     *
     * @return
     */
    public String getHash() {
        return hash;
    }

    /**
     *
     * @param hash
     */
    public void setHash(String hash) {
        this.hash = hash;
    }

    /**
     *
     * @return
     */
    public String getPoints() {
        return points;
    }

    /**
     *
     * @param points
     */
    public void setPoints(String points) {
        this.points = points;
    }

    /**
     *
     * @return
     */
    public String getName() {
        return name;
    }

    /**
     *
     * @param name
     */
    public void setName(String name) {
        this.name = name;
    }

    public int compareName(Object o) {
        Code code = (Code) o;
        return this.name.compareTo(code.getName());
    }

    public int comparePoints(Object o) {
        Code code = (Code) o;
        return this.points.compareTo(code.getPoints());
    }

    public int compareHash(Object o) {
        Code code = (Code) o;
        return this.hash.compareTo(code.getHash());
    }


}
