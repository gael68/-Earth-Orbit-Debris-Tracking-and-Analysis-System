package com.team22;

import org.junit.jupiter.api.*;
import java.io.*;
import java.nio.file.*;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;

public class OrbitAssessmentTest {

    private static final String CSV_FILE = "data/updated_orbit_status.csv";
    private static final String TXT_FILE = "data/exited_debris_report.txt";

    static class TestSpaceObject extends SpaceObject {
        public TestSpaceObject(String recordId, String satelliteName, String country, String approximateOrbitType,
                String objectType, int launchYear, String launchSite,
                double longitude, double avgLongitude, String geohash,
                int daysOld, int conjunctionCount) {
            super(recordId, satelliteName, country, approximateOrbitType, objectType, launchYear,
                    launchSite, longitude, avgLongitude, geohash, daysOld, conjunctionCount);
        }

        @Override
        public String getObjectType() {
            return objectType;
        }
    }

    @BeforeEach
    void setUp() throws IOException {
        Files.createDirectories(Paths.get("data"));
        Files.deleteIfExists(Paths.get(CSV_FILE));
        Files.deleteIfExists(Paths.get(TXT_FILE));
    }

    @AfterEach
    void tearDown() throws IOException {
        Files.deleteIfExists(Paths.get(CSV_FILE));
        Files.deleteIfExists(Paths.get(TXT_FILE));
    }

    @Test
    void testAssessAndSave_createsExpectedFilesAndContents() throws IOException {
        List<SpaceObject> objects = Arrays.asList(
                new TestSpaceObject("ID1", "SatA", "USA", "LEO", "Debris", 2001, "Site1",
                        45, 30, "geoh1", 12000, 2), // In-orbit, Moderate risk
                new TestSpaceObject("ID2", "SatB", "USA", "unknown", "Debris", 2005, "Site2",
                        0, 0, "geoh2", 16000, 0) // Exited
        );

        OrbitAssessment assessment = new OrbitAssessment(objects);
        assessment.assessAndSave();

        // Confirm CSV file exists and has both entries
        assertTrue(Files.exists(Paths.get(CSV_FILE)), "CSV file should be created.");
        List<String> csvLines = Files.readAllLines(Paths.get(CSV_FILE));
        assertEquals(3, csvLines.size(), "CSV should have header and 2 data rows.");
        assertTrue(csvLines.get(1).contains("ID1"));
        assertTrue(csvLines.get(2).contains("ID2"));

        // Confirm TXT file exists and has only the exited one
        assertTrue(Files.exists(Paths.get(TXT_FILE)), "TXT file should be created.");
        List<String> txtLines = Files.readAllLines(Paths.get(TXT_FILE));
        assertTrue(txtLines.stream().anyMatch(l -> l.contains("SatB")));
        assertFalse(txtLines.stream().anyMatch(l -> l.contains("SatA")));
    }
}
