package com.team22;

import org.junit.jupiter.api.*;

import java.io.*;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;

public class TrackingSystemTest {

    private final ByteArrayOutputStream outputCaptor = new ByteArrayOutputStream();
    private PrintStream originalOut;

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
    void setUp() {
        originalOut = System.out;
        System.setOut(new PrintStream(outputCaptor));
    }

    @AfterEach
    void tearDown() {
        System.setOut(originalOut);
        System.setIn(System.in);
    }

    @Test
    void testTrackRocketBodyDisplaysMatchingObjects() {
        List<SpaceObject> objects = List.of(
                new TestSpaceObject("ID1", "SatX", "USA", "LEO", "Rocket Body", 2001,
                        "SiteA", 50, 45, "geoh1", 1000, 2),
                new TestSpaceObject("ID2", "SatY", "USA", "LEO", "Debris", 2002,
                        "SiteB", 10, 9, "geoh2", 1500, 1));

        // Simulate choosing "Rocket Body" (option 1), then "Back" (option 5)
        String input = "1\n5\n";
        System.setIn(new ByteArrayInputStream(input.getBytes()));

        TrackingSystem tracker = new TrackingSystem(objects);
        tracker.displayTrackingMenu();

        String output = outputCaptor.toString();

        assertTrue(output.contains("SatX"), "Expected Rocket Body object to be printed.");
        assertFalse(output.contains("SatY"), "Debris object should not appear.");
        assertTrue(output.contains("Tracking: ROCKET BODY"));
    }

    @Test
    void testTrackUnknownWithNoResults() {
        List<SpaceObject> objects = List.of(
                new TestSpaceObject("ID1", "SatX", "USA", "LEO", "Debris", 2001,
                        "SiteA", 50, 45, "geoh1", 1000, 2));

        String input = "4\n5\n"; // "Unknown", then "Back"
        System.setIn(new ByteArrayInputStream(input.getBytes()));

        TrackingSystem tracker = new TrackingSystem(objects);
        tracker.displayTrackingMenu();

        String output = outputCaptor.toString();

        assertTrue(output.contains("No objects found for type: UNKNOWN"));
    }

    @Test
    void testInvalidInputHandledGracefully() {
        List<SpaceObject> objects = Collections.emptyList();

        String input = "invalid\n5\n";
        System.setIn(new ByteArrayInputStream(input.getBytes()));

        TrackingSystem tracker = new TrackingSystem(objects);
        tracker.displayTrackingMenu();

        String output = outputCaptor.toString();
        assertTrue(output.contains("Invalid input."));
    }
}
