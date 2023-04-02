package com.example.barqrxmls;

import android.graphics.Bitmap;
import android.graphics.Color;

import androidx.annotation.ColorInt;

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
    private ArrayList<LatLongPair> latLongPairs;

    private Bitmap myImageRepresentation;
    // Access like nameParts['suffix']

    // Hashes are uniquely identified by their sha256sum

    /**
     * Constructor that builds the hash, which is a string representation of the
     * sha256sum of the scanned data.
     * @param data Scanned data from the Code
     */
    public Code(String data) {
        if (data.length() <= 0) {
            throw new IllegalArgumentException("Code data must be non-empty!");
        }
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
        ArrayList<String> names = new ArrayList<>(Arrays.asList("Tyler", "Anjelica", "Danielle",
                "James", "Gregory", "Greg", "Robin", "Richard", "Peter", "Joe", "Joseph", "Kannan",
                "Sarah", "Morgan"));
        ArrayList<String> suffixArticles = new ArrayList<>(Arrays.asList(", the", ", a", ", an",
                ", PhD in", ", MD in"));
        ArrayList<String> suffixes = new ArrayList<>(Arrays.asList("Tall", "Fat",
                "Tiny", "Strange", "Round", "Stupid", "Smart", "Courageous", "Cowardly"));
        nameParts.put(0, prefixes);
        nameParts.put(1, names);
        nameParts.put(2, suffixArticles);
        nameParts.put(3, suffixes);

        name = generateName(nameParts);
        points = calculateScore();
        latLongPairs = new ArrayList<>();
        myImageRepresentation = generateImage();
    }

    /**
     * Empty constructor, for use with the Database.
     */
    public Code() {

    }

    /**
     * Return the generated bitmap representation of this object.
     * @return
     */
    public Bitmap getMyImageRepresentation() {
        return myImageRepresentation;
    }

    /***
     * Setter for the array list of latitude and longitude pairs (LatLongPair objects).
     * This overrides the existing ArrayList. Use appendLatLongPairs to not override.
     * @param latLongPairs ArrayList of LatLongPair objects.
     */
    public void setLatLongPairs(ArrayList<LatLongPair> latLongPairs) {
        this.latLongPairs = latLongPairs;
    }

    /***
     * Append a new LatLongPair object to the list of existing LatLongPairs. Constructs the new
     * object internally using the given parameters.
     * @param latitude A double representing the latitude
     * @param longitude A double representing the longitude
     */
    public void appendLatLongPairs(Double latitude, Double longitude) {
        latLongPairs.add(new LatLongPair(latitude, longitude));
    }

    /**
     * Get the latitude and longitude pairs list.
     * @return An ArrayList containing LatLongPair objects
     */
    public ArrayList<LatLongPair> getLatLongPairs() {
        return latLongPairs;
    }


    /**
     * Generate a bitmap image, with each pixel set to an ARGB value determined in large part by the
     * Code's hash.
     * The hash is broken into chunks of 4, parsed as an integer, and placed in an array.
     * Each pixel then grabs 4 values from this array and uses that as its ARGB value, modulo 256.
     * @return A bitmap object.
     */
    // Method source: https://www.geeksforgeeks.org/image-processing-in-java-creating-a-random-pixel-image/
    // Color.argb: https://developer.android.com/reference/android/graphics/Color#argb(float,%20float,%20float,%20float)
    public Bitmap generateImage() {
        int width = 80, height = 80;
        Bitmap img = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
        int start = 0, end;
        int step = 4;
        int pixelPart;
        ArrayList<Integer> pixelPortions = new ArrayList<>();
        int digRepr;
        for (int k = 0; k < 16; k++) {
            end = start+step;
            String slice = hash.substring(start, end);
            digRepr = Integer.parseInt(slice, 16);
            try {
                pixelPart = digRepr;
                pixelPortions.add(pixelPart);
            } catch (NullPointerException e) {
                System.out.println(e);
            }
            start = end;
        }
        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                int a = (pixelPortions.get(Math.abs(x+y)%16) + x + y) % 256;
                int r = (pixelPortions.get(Math.abs(x+1+y)%16) + x + y) % 256;
                int g = (pixelPortions.get(Math.abs(x-1+y)%16) + x + y) % 256;
                int b = (pixelPortions.get(Math.abs(x+2+y)%16) + x + y) % 256;
                @ColorInt
                int pixel = Color.argb(a, r, g, b);
                img.setPixel(x, y, pixel);
            }
        }
        return img;
    }

    /**
     * Take the hash and convert it into a relatively unique name.
     * This is achieved by consuming 'n' digits of the has, and adding them together.
     * This gives a unique number; we can then modulo this number by the available bins.
     * @return the generated name
     */
    public String generateName(HashMap<Integer, ArrayList<String>> nameParts) {
        // TODO: Generate tests for this function.
        StringBuilder generatedName = new StringBuilder();
        Integer digRepr;
        int start = 0, end;
        int step = 2;
        int idx;
        for (int k = 0; k < nameParts.size(); k++) {
            end = start+step;
            String slice = hash.substring(start, end);
            digRepr = Integer.parseInt(slice, 16);
            ArrayList<String> options = nameParts.get(k);
            try {
                idx = digRepr % options.size();
                generatedName.append(nameParts.get(k).get(idx));
            } catch (NullPointerException e) {
                System.out.println(e);
            }
            if (k != 1 && k != nameParts.size() - 1) {
                // Don't put a space after name, since we want a comma there instead.
                // Also don't end the name with a space.
                generatedName.append(" ");
            }
            start = end;
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
     * Calculate the score, based on the following rules:
     *  1. If the previous character is the same as the next character, +1 point
     *  2. If the current character is the a digit under 9, add that digit
     *  3. If the current letter is the first letter of a team member's name, add 50 points.
     * @return score
     */
    public Integer calculateScore() {
        int start;
        int sum = 0;
        int stop = 16;
        ArrayList<String> bonusLetters = new ArrayList<>(Arrays.asList("a", "t", "s", "d", "n", "k"));
        for (start = 0; start < stop; start++) {
            String curr = hash.substring(start, start+1);
            String next = hash.substring(start+1, start+2);
            if (curr.equals(next)) {
                sum += 1;
            }
            if (Integer.parseInt(curr, 16) <= 9) {
                sum += Integer.parseInt(curr, 16);
            }
            if (bonusLetters.contains(curr)) {
                sum += 50;
            }
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
