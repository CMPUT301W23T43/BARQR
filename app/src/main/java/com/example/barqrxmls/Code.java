package com.example.barqrxmls;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;

public class Code {

    private String hash;
    private Integer points;
    private String name;

    // Access like nameParts['suffix']

    // Hashes are uniquely identified by their sha256sum

    /**
     * Constructor that builds the hash, which is a string representation of the
     * sha256sum of the scanned data.
     * @param data Scanned data from the Code
     */
    public Code(String data) {
        // From www.baeldung.com
        // https://www.baeldung.com/sha-256-hashing-java
        // written by "baeldung", accessed March 6 2023
        MessageDigest digest = null;
        try {
            digest = MessageDigest.getInstance("SHA-256");
        } catch (NoSuchAlgorithmException e) {
            System.out.println("No SHA-256 algorithm?");
        }
        byte[] encodedHash = digest.digest(data.getBytes(StandardCharsets.UTF_8));

        StringBuilder hexStr = new StringBuilder(2 * encodedHash.length);
        for (byte b : encodedHash) {
            String hex = Integer.toHexString(0xff & b);
            if (hex.length() == 1) {
                hexStr.append('0');
            }
            hexStr.append(hex);
        }

        hash = hexStr.toString();


        HashMap<Integer, ArrayList<String>> nameParts = new HashMap<>();
        // Format will be like "Doctor James Smith the Fat"
        ArrayList<String> prefixes = new ArrayList<>((Arrays.asList("Doctor", "Professor",
                "Teacher", "King", "Queen", "The Honourable", "The", "Sir", "Madam", "Dog", "Guy",
                "That", "Mr", "Mrs", "Ms", "Real G")));
        ArrayList<String> suffixes = new ArrayList<>(Arrays.asList("Tall", "Fat",
                "Tiny", "Strange", "Round", "Stupid", "Smart", "Courageous", "Cowardly"));
        ArrayList<String> suffixArticles = new ArrayList<>(Arrays.asList("the", "a", "an",
                "PhD in", "MD in"));
        ArrayList<String> names = new ArrayList<>(Arrays.asList("Tyler", "Anjelica", "Danielle",
                "James", "Gregory", "Greg", "Robin", "Richard", "Dick", "Joe", "Joseph", "Kannan",
                "Sarah", "Morgan"));
        nameParts.put(0, prefixes);
        nameParts.put(1, suffixes);
        nameParts.put(2, suffixArticles);
        nameParts.put(3, names);

        name = generateName(nameParts);
        points = calculateScore();
    }

    /**
     * Take the hash and convert it into a relatively unique name.
     * This is achieved by consuming 'n' digits of the has, and adding them together.
     * This gives a unique number; we can then modulo this number by the available bins.
     * @return the generated name
     */
    public String generateName(HashMap<Integer, ArrayList<String>> nameParts) {
        // 5891b 5b522 d5df0 86d0f f0b110fbd9d 21bb4fc7163af34d08286a2e846f6be03
        // TODO: Generate tests for this function.
        StringBuilder generatedName = new StringBuilder();
        Integer digRepr;
        int start, end;
        int step = 8;
        int i = 0;
        int idx;
        for (start = 0; start < nameParts.size(); start+=step) {
            end = start+step;
            String slice = hash.substring(start, end);
            digRepr = Integer.parseInt(slice, 16);
            ArrayList<String> options = nameParts.get(i);
            try {
                idx = digRepr % options.size();
                generatedName.append(nameParts.get(i).get(idx));
            } catch (NullPointerException e) {
                System.out.println(e);
            }
        }
        return generatedName.toString();
    }

    /**
     *
     * @return hash
     */
    public String getHash() {
        return hash;
    }

    /**
     * Setter for the unique hash corresponding to this object.
     * @param hash The base-16 hash for this code.
     */
    public void setHash(String hash) {
        this.hash = hash;
    }

    /**
     * Getter for points
     * @return points
     */
    public Integer getPoints() {
        return this.points;
    }

    /**
     * Calculate the score, as the flat sum of the first 8*numParts.size() digits.
     * @return score
     */
    public Integer calculateScore() {
        //TODO: Create a scoring scheme and function
        // That is more complex than simple sum.
        int start, end;
        int step = 8;
        int sum = 0;
        int digRepr;
        for (start = 0; start < 4; start+=step) {
            end = start+step;
            String slice = hash.substring(start, end);
            digRepr = Integer.parseInt(slice, 16);
            sum += digRepr;
        }
        return sum;
    }

    /**
     * Setter for points
     * @param points the Integer value for points to become.
     */
    public void setPoints(Integer points) {
        this.points = points;
    }

    /**
     * Getter for name.
     * @return name
     */
    public String getName() {
        return name;
    }

    /**
     * Setter for name
     * @param name the name to assume.
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
