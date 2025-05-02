package com.team22;

import org.junit.jupiter.api.*;
import java.io.*;
import java.nio.file.*;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;

public class PolicymakerAnalysisTest {

    private static final Path TXT_PATH = Paths.get("data/exited_debris_report.txt");
    private static final Path CSV_PATH = Paths.get("data/updated_orbit_status.csv");

    private final ByteArrayOutputStream outputCaptor = new ByteArrayOutputStream();
    private PrintStream originalOut;

    @BeforeEach
    void setUp() throws IOException {
        Files.createDirectories(Paths.get("data"));
        Files.deleteIfExists(TXT_PATH);
        Files.deleteIfExists(CSV_PATH);
        originalOut = System.out;
        System.setOut(new PrintStream(outputCaptor));
    }

    @AfterEach
    void tearDown() throws IOException {
        Files.deleteIfExists(TXT_PATH);
        Files.deleteIfExists(CSV_PATH);
        System.setOut(originalOut);
    }

    @Test
    void testReviewImpactReport_whenFileMissing() {
        PolicymakerAnalysis analysis = new PolicymakerAnalysis();
        analysis.reviewImpactReport();

        String output = outputCaptor.toString();
        assertTrue(output.contains("No impact report available"));
    }

    @Test
    void testReviewImpactReport_displaysContents() throws IOException {
        List<String> lines = List.of(
                "Exited Debris Report",
                "Total in orbit: 1",
                "Total exited: 1",
                "RecordID: ID1, Name: SatX, Country: USA...");
        Files.write(TXT_PATH, lines);

        PolicymakerAnalysis analysis = new PolicymakerAnalysis();
        analysis.reviewImpactReport();

        String output = outputCaptor.toString();
        assertTrue(output.contains("Exited Debris Report"));
        assertTrue(output.contains("Total in orbit: 1"));
        assertTrue(output.contains("SatX"));
    }

    @Test
    void testAssessRiskLevels_whenFileMissing() {
        PolicymakerAnalysis analysis = new PolicymakerAnalysis();
        analysis.assessRiskLevels();

        String output = outputCaptor.toString();
        assertTrue(output.contains("No orbit status data available"));
    }

    @Test
    void testAssessRiskLevels_countsCorrectly() throws IOException {
        List<String> lines = List.of(
                "Header,Header,Header,...,RiskLevel",
                "a,a,a,a,a,a,a,a,a,a,a,a,true,High",
                "b,b,b,b,b,b,b,b,b,b,b,b,true,Moderate",
                "c,c,c,c,c,c,c,c,c,c,c,c,true,Low");
        Files.write(CSV_PATH, lines);

        PolicymakerAnalysis analysis = new PolicymakerAnalysis();
        analysis.assessRiskLevels();

        String output = outputCaptor.toString();
        assertTrue(output.contains("High Risk Objects: 1"));
        assertTrue(output.contains("Moderate Risk Objects: 1"));
        assertTrue(output.contains("Low Risk Objects: 1"));
    }
}
