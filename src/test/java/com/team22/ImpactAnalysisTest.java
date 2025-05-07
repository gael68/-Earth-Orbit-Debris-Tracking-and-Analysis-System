package com.team22;

import org.junit.jupiter.api.*;
import java.io.*;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;

public class ImpactAnalysisTest {

    private final ByteArrayOutputStream outContent = new ByteArrayOutputStream();
    private PrintStream originalOut;
    private InputStream originalIn;

    private List<SpaceObject> mockObjects;

    @BeforeEach
    void setup() {
        originalOut = System.out;
        originalIn = System.in;

        System.setOut(new PrintStream(outContent));

        mockObjects = List.of(
            new Debris("R1", "Debris-1", "USA", "LEO", "DEBRIS", 2000, "Cape", 100.0, 90.0, "geo1", 300, 2),
            new Satellite("R2", "Sat-2", "RUS", "LEO", "SATELLITE", 2005, "Baikonur", 80.0, 79.0, "geo2", 50, 1),
            new Debris("R3", "Debris-2", "USA", "MEO", "DEBRIS", 1990, "VAFB", 60.0, 58.0, "geo3", 700, 0)
        );
    }

    @AfterEach
    void teardown() {
        System.setOut(originalOut);
        System.setIn(originalIn);
    }

    @Test
    void testAnalyzeLongTermImpact_filtersCorrectly() {
        ImpactAnalysis analysis = new ImpactAnalysis(mockObjects);
        analysis.analyzeLongTermImpact();

        String output = outContent.toString();
        assertTrue(output.contains("Record ID: R1"), "Debris-1 should be in the impact report.");
        assertTrue(output.toLowerCase().contains("leo"), "Output should mention LEO.");
    }

    @Test
    void testGenerateDensityReport_withValidInput() {
        String simulatedInput = "50\n110\n";
        System.setIn(new ByteArrayInputStream(simulatedInput.getBytes()));

        ImpactAnalysis analysis = new ImpactAnalysis(mockObjects);
        analysis.generateDensityReport();

        String output = outContent.toString();
        assertTrue(output.contains("Debris Density Report"), "Should include report header.");
        assertTrue(output.contains("Record ID: R2"), "Should include object R2 within range.");
        assertTrue(output.contains("Record ID: R3"), "Should include object R3 within range.");
    }

    @Test
    void testGenerateDensityReport_withInvalidInput() {
        String simulatedInput = "abc\nxyz\n";
        System.setIn(new ByteArrayInputStream(simulatedInput.getBytes()));

        ImpactAnalysis analysis = new ImpactAnalysis(mockObjects);
        analysis.generateDensityReport();

        String output = outContent.toString();
        assertTrue(output.contains("Invalid input"), "Should detect and report invalid number format.");
    }
}
