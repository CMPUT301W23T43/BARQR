package com.example.barqrxmls;

/**
 * A container around a String object. Used by the second Constructor in Code.java.
 * This is needed because both the QR code data and the resultant hash are strings,
 * so a constructor that takes a String hash in order to recreate an existing Code object
 * would not work as the input parameters for both constructors would be identical.
 * Now, they are unique.
 */
public class CodeHashContainer {
    private String codeHashString;

    /**
     * Create a new container object; the String given must be the hash of the Code object
     * you want to later store.
     * @param hash A (String) representing the hash object.
     */
    public CodeHashContainer(String hash) {
        this.codeHashString = hash;
    }

    public String getCodeHashString() {
        return codeHashString;
    }
}
