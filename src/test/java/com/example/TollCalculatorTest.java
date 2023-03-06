package com.example;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.PrintStream;

import static org.junit.jupiter.api.Assertions.*;

class TollCalculatorTest {

    private TollCalculator tollCalculator;

    @BeforeEach
    void setUp() {
        try {
            tollCalculator = new TollCalculator();
        } catch (IOException e) {
            fail("Failed to initialize TollCalculator: " + e.getMessage());
        }
    }

    @Test
    void testCostOfTripCase1() {
        String origin = "QEW";
        String destination = "Bronte Road";
        String expectedOutput = "Distance(km): 14.062\nCost: $3.516".replaceAll("\\n|\\r\\n",
                System.getProperty("line.separator"));
        ;
        assertEquals(expectedOutput, getOutput(() -> tollCalculator.costOfTrip(origin, destination)));
    }

    @Test
    void testCostOfTripCase2() {
        String origin = "Bronte Road";
        String destination = "QEW";
        String expectedOutput = "Distance(km): 14.062\nCost: $3.516".replaceAll("\\n|\\r\\n",
                System.getProperty("line.separator"));
        assertEquals(expectedOutput, getOutput(() -> tollCalculator.costOfTrip(origin, destination)));
    }

    @Test
    void testCostOfTripCase3() {
        String origin = "Appleby Line";
        String destination = "Mavis Road";
        String expectedOutput = "Distance(km): 33.633\nCost: $8.409".replaceAll("\\n|\\r\\n",
                System.getProperty("line.separator"));
        assertEquals(expectedOutput, getOutput(() -> tollCalculator.costOfTrip(origin, destination)));
    }

    @Test
    void testCostOfTripCase4() {
        String origin = "Bronte Road";
        String destination = "Bronte Road";
        String expectedOutput = "Distance(km): 0\nCost: $0".replaceAll("\\n|\\r\\n",
                System.getProperty("line.separator"));
        assertEquals(expectedOutput, getOutput(() -> tollCalculator.costOfTrip(origin, destination)));
    }

    @Test
    void testCostOfTripCase5() {
        String origin = "First Road";
        String destination = "Second Road";
        String expectedOutput = "Undefined Interchange(s)".replaceAll("\\n|\\r\\n",
                System.getProperty("line.separator"));
        assertEquals(expectedOutput, getOutput(() -> tollCalculator.costOfTrip(origin, destination)));
    }

    @Test
    void testCostOfTripCase6() {
        String origin = "Bronte Road";
        String destination = "Second Road";
        String expectedOutput = "Undefined Interchange(s)".replaceAll("\\n|\\r\\n",
                System.getProperty("line.separator"));
        assertEquals(expectedOutput, getOutput(() -> tollCalculator.costOfTrip(origin, destination)));
    }

    @Test
    void testCostOfTripCase7() {
        String origin = "First Road";
        String destination = "Bronte Road";
        String expectedOutput = "Undefined Interchange(s)".replaceAll("\\n|\\r\\n",
                System.getProperty("line.separator"));
        assertEquals(expectedOutput, getOutput(() -> tollCalculator.costOfTrip(origin, destination)));
    }

    private String getOutput(Runnable runnable) {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        System.setOut(new PrintStream(outputStream));
        runnable.run();
        return outputStream.toString().trim();
    }
}
