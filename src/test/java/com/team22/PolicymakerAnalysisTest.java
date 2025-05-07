package com.team22;

import org.junit.jupiter.api.*;
import java.io.*;
import java.nio.file.*;

import static org.junit.jupiter.api.Assertions.*;

public class PolicymakerAnalysisTest {

    private static final Path EXITED_REPORT = Paths.get("data/exited_debris_report.txt");
    private static final Path ORBIT_STATUS = Paths.get("data/updated_orbit_status.csv");

    private final ByteArrayOutputStream outContent = new ByteArrayOutputStream();
    private PrintStream originalOut;

    @BeforeEach
    void setup() throws IOException {
        Files.createDirectories(EXITED_REPORT.getParent());
        originalOut = System.out;
        System.setOut(new PrintStream(outContent));
    }

    @AfterEach
    void teardown() throws IOException {
        Files.deleteIfExists(EXITED_REPORT);
        Files.deleteIfExists(ORBIT_STATUS);
        System.setOut(originalOut);
    }

    @Test
    void testReviewImpactReport_withValidFile() throws IOException {
        Files.write(EXITED_REPORT, """
            Exited Debris Report
            Total in orbit: 2
            Total exited: 1
            RecordID: R1, Name: Debris-1, Country: USA, OrbitType: LEO, LaunchYear: 2000, LaunchSite: Cape, Longitude: 100.0, AvgLongitude: 90.0, Geohash: geo1, DaysOld: 300
            """.stripIndent().getBytes());

        PolicymakerAnalysis analysis = new PolicymakerAnalysis();
        analysis.reviewImpactReport();

        String output = outContent.toString();
        assertTrue(output.contains("Exited Debris Report"));
        assertTrue(output.contains("RecordID: R1"));
    }

    @Test
    void testReviewImpactReport_missingFileGracefullyHandled() {
        PolicymakerAnalysis analysis = new PolicymakerAnalysis();
        assertDoesNotThrow(analysis::reviewImpactReport);

        String output = outContent.toString();
        assertTrue(output.contains("No impact report available"), "Should print warning about missing file.");
    }

    @Test
    void testAssessRiskLevels_withValidData() throws IOException {
        Files.write(ORBIT_STATUS, """
            RecordID,SatelliteName,Country,OrbitType,ObjectType,LaunchYear,LaunchSite,Longitude,AvgLongitude,Geohash,DaysOld,ConjunctionCount,StillInOrbit,RiskLevel
            R1,SatA,USA,LEO,DEBRIS,2000,Cape,100.0,90.0,geo1,300,2,true,High
            R2,SatB,RUS,MEO,DEBRIS,2005,Base,120.0,118.0,geo2,150,1,true,Low
            R3,SatC,CHN,LEO,DEBRIS,2010,Site,80.0,78.0,geo3,200,3,true,Moderate
            """.stripIndent().getBytes());

        PolicymakerAnalysis analysis = new PolicymakerAnalysis();
        analysis.assessRiskLevels();

        String output = outContent.toString();
        assertTrue(output.contains("High Risk Objects: 1"));
        assertTrue(output.contains("Moderate Risk Objects: 1"));
        assertTrue(output.contains("Low Risk Objects: 1"));
    }

    @Test
    void testAssessRiskLevels_missingFileGracefullyHandled() {
        PolicymakerAnalysis analysis = new PolicymakerAnalysis();
        assertDoesNotThrow(analysis::assessRiskLevels);

        String output = outContent.toString();
        assertTrue(output.contains("No orbit status data available"), "Should print warning about missing file.");
    }
}
