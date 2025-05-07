package com.team22;

import org.junit.jupiter.api.*;
import java.io.*;
import java.nio.file.*;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;

public class MissionControlTest {

    private static final Path TEMP_DATA = Paths.get("data/test_space_objects.csv");
    private static final Path TEMP_USERS = Paths.get("data/users.csv");
    private final ByteArrayOutputStream outContent = new ByteArrayOutputStream();
    private PrintStream originalOut;

    @BeforeEach
    void setup() throws IOException {
        Files.createDirectories(Paths.get("data"));
        Files.write(TEMP_DATA, List.of(
                "record_id,satellite_name,country,approximate_orbit_type,object_type,launch_year,launch_site,longitude,avg_longitude,geohash,days_old,conjunction_count",
                "R1,SatA,USA,LEO,Satellite,2001,Cape,100.5,98.5,geo1,5000,2",
                "R2,DebrisX,RUS,MEO,Debris,1999,Baikonur,120.2,110.8,geo2,7000,5"));

        Files.write(TEMP_USERS, List.of(
                "alice,Scientist,pass123",
                "bob,Admin,adminpass",
                "carol,Agency,agencypass",
                "dave,Policymaker,policy123"));

        originalOut = System.out;
        System.setOut(new PrintStream(outContent));
    }

    @AfterEach
    void teardown() throws IOException {
        Files.deleteIfExists(TEMP_DATA);
        Files.deleteIfExists(TEMP_USERS);
        System.setOut(originalOut);
    }

    @Test
    void testLoadData_parsesObjectsCorrectly() {
        MissionControl mc = new MissionControl(TEMP_DATA.toString(), new Scanner(""));
        mc.loadData();

        List<SpaceObject> objects = mc.getSpaceObjects();
        assertEquals(2, objects.size());

        SpaceObject obj1 = objects.get(0);
        SpaceObject obj2 = objects.get(1);

        assertEquals("SatA", obj1.satelliteName);
        assertEquals("DebrisX", obj2.satelliteName);
        assertEquals("Satellite", obj1.getObjectType());
        assertEquals("Debris", obj2.getObjectType());
 
    }

    @Test
    void testLoadData_missingColumnsHandled() throws IOException {
        Files.write(TEMP_DATA, List.of(
                "id,name",
                "1,X"));

        MissionControl mc = new MissionControl(TEMP_DATA.toString(), new Scanner(""));
        assertDoesNotThrow(mc::loadData);
        assertEquals(0, mc.getSpaceObjects().size(), "Should not parse any objects with missing columns");
    }

    @Test
    void testLoadData_emptyFileHandledGracefully() throws IOException {
        Files.write(TEMP_DATA, List.of());

        MissionControl mc = new MissionControl(TEMP_DATA.toString(), new Scanner(""));
        assertDoesNotThrow(mc::loadData);
        assertEquals(0, mc.getSpaceObjects().size());
    }
}
