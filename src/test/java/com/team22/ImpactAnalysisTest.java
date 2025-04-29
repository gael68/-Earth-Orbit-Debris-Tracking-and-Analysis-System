package com.team22;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.*;
import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.util.*;

public class ImpactAnalysisTest {

    private final ByteArrayOutputStream outputStreamCaptor = new ByteArrayOutputStream();
    private PrintStream originalOut;

    // Concrete subclass for testing purposes
    static class TestSpaceObject extends SpaceObject {
        public TestSpaceObject(String recordId, String satelliteName, String country,
                String approximateOrbitType,
                String objectType, int launchYear, String launchSite,
                double longitude, double avgLongitude, String geohash,
                int daysOld, int conjunctionCount) {
            super(recordId, satelliteName, country, approximateOrbitType, objectType,
                    launchYear,
                    launchSite, longitude, avgLongitude, geohash, daysOld, conjunctionCount);
        }

        @Override
        public String getObjectType() {
            return this.objectType != null ? this.objectType : "UNKNOWN";
        }
    }

    @BeforeEach
    void setUp() {
        originalOut = System.out;
        System.setOut(new PrintStream(outputStreamCaptor));
    }

    @AfterEach
    void tearDown() {
        System.setOut(originalOut);
    }

    @Test
    void testAnalyzeLongTermImpactPrintsTop10Oldest() {
        List<SpaceObject> spaceObjects = new ArrayList<>();
        for (int i = 0; i < 15; i++) {
            spaceObjects.add(new TestSpaceObject(
                    "ID" + i, "Satellite" + i, "Country" + i, "LEO",
                    "Satellite", 2000 + i, "Site" + i, i, i + 0.5,
                    "geohash" + i, i * 100, i));
        }
        ImpactAnalysis analysis = new ImpactAnalysis(spaceObjects);

        analysis.analyzeLongTermImpact();
        String output = outputStreamCaptor.toString();

        // Verify that the 10 oldest objects are printed
        assertTrue(output.contains("Satellite14"), "Expected to find Satellite14(oldest) in output.");
        assertTrue(output.contains("Satellite5"), "Expected to find Satellite5 (tentholdest) in output.");
        assertFalse(output.contains("Satellite4"), "Did not expect to find Satellite4(11th oldest) in output.");
    }

    @Test
    void testGenerateDensityReportCountsCorrectly() {
        List<SpaceObject> spaceObjects = Arrays.asList(
                new TestSpaceObject("ID1", "Sat1", "USA", "LEO",
                        "Satellite", 2010, "SiteA", 0, 0.5, "geohash1", 500, 2),
                new TestSpaceObject("ID2", "Sat2", "USA", "MEO",
                        "Satellite", 2011, "SiteB", 1, 1.5, "geohash2", 400, 3),
                new TestSpaceObject("ID3", "Sat3", "USA", "LEO",
                        "Satellite", 2012, "SiteC", 2, 2.5, "geohash3", 300, 1),
                new TestSpaceObject("ID4", "Sat4", "USA", null,
                        "Debris", 2013, "SiteD", 3, 3.5, "geohash4", 200, 4),
                new TestSpaceObject("ID5", "Sat5", "USA", "HEO",
                        "Debris", 2014, "SiteE", 4, 4.5, "geohash5", 100, 5));
        ImpactAnalysis analysis = new ImpactAnalysis(spaceObjects);

        analysis.generateDensityReport();
        String output = outputStreamCaptor.toString();

        // Verify that the counts match
        assertTrue(output.contains("LEO: 2"), "Expected 2 LEO objects in report.");
        assertTrue(output.contains("MEO: 1"), "Expected 1 MEO object in report.");
        assertTrue(output.contains("HEO: 1"), "Expected 1 HEO object in report.");
        assertTrue(output.contains("UNKNOWN: 1"), "Expected 1 UNKNOWN object in report.");
    }
}