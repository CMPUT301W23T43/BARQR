package com.example.barqrxmls;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.function.Executable;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;

/**
 * tests the Code model
 */
public class CodeUnitTest {

    /**
     *tests the creation of a new code
     */
    @Test
    public void codeCreationTest() {
        Code myCode = new Code("Test!");
        assertTrue(myCode.getHash().length() > 0);
        assertTrue(myCode.getName().length() > 0);
        assertTrue(myCode.getPoints() > 0);
    }

    /**
     * tests the incorrect initialization of a new code
     */
    @Test
    public void codeCreationBlankStringTest() {
        // Format from https://howtodoinjava.com/junit5/expected-exception-example/
        // (specifically, wrapping the code with () -> {...}
        IllegalArgumentException e = assertThrows(IllegalArgumentException.class, () -> {
            new Code("");
        });
    }

    /**
     * tests Code.getHash
     */
    @Test
    public void hashStringTest(){
        String str = "Test string!";
        // Generated from `echo -n "Test string!" | sha256sum -`
        // The -n flag is extremely important, as echo appends a newline without it.
        String expectedHash = "70d88e24d1aba6714387b412e687f608d521f0c86e6f8264fbcbd07c525a526b";
        Code myCode = new Code(str);
        assertEquals(expectedHash, myCode.getHash());
    }

    /**
     * tests a codes ability to calculate points from a hash
     */
    @Test
    public void calculatePointTest() {
        String str = "Test string!";
        Code myCode = new Code(str);
        // The expected score is:
        // 70d88e24d1aba671 is the hash, so the score is:
        // 1 + 7 + 8 + 8 + 2 + 4 + 1 + 6 + 7 + 1 + 50 + 50 + 50 + 50
        Integer expectedScore = 1 + 7 + 8 + 8 + 2 + 4 + 1 + 6 + 7 + 1 + 50 + 50 + 50 + 50;
        assertEquals(expectedScore, myCode.getPoints());
    }

    /**
     * tests code.getName and the code's ability to generate a name
     */
    @Test
    public void nameTest() {
        // Order is Prefix, Name, SuffixArticles, Suffix
        // prefixes: 16; names: 14; suffixArticles: 5; suffixes: 9;
        // For the hash "70d88e24...", we get 112, 216, 142, 36, which corresponds to
        // 0, 6, 2, 0; this in turn is "Doctor" "Courageous" "a" "Robin";
        // In order, that is "Doctor Courageous a Robin"
        String str = "Test string!";
        Code myCode = new Code(str);
        String expectedName = "Doctor Robin, an Tall";
        assertEquals(expectedName, myCode.getName());
    }

    /**
     * tests creating a code from a hash
     * tests the effectiveness of CodeHashContainer
     */
    @Test
    public void recreateObjectTest() {
        Code myCode = new Code("This is a test object!");
        String myCodeHash = myCode.getHash();
        String myCodeName = myCode.getName();
        Integer myCodePoints = myCode.getPoints();

        CodeHashContainer container = new CodeHashContainer(myCodeHash);
        Code recreatedCode = new Code(container);

        assertEquals(myCodeHash, recreatedCode.getHash());
        assertEquals(myCodeName, recreatedCode.getName());
        assertEquals(myCodePoints, recreatedCode.getPoints());
    }
}
