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
                "RecordID,Ignore,SatelliteName,Country,OrbitType,ObjectType,LaunchYear,LaunchSite,Longitude,AvgLongitude,Geohash,Skip1,Skip2,Skip3,Skip4,Skip5,Skip6,Skip7,DaysOld,ConjunctionCount",
                "D001,,DebrisOne,USA,LEO,Debris,2000,Cape,100.0,98.0,G1,_,_,_,_,_,_,_,5000,3",
                "S001,,SatOne,JPN,MEO,Satellite,2010,Tanegashima,80.0,80.0,G2,_,_,_,_,_,_,_,2000,1"));

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

        SpaceObject debris = objects.get(0);
        SpaceObject sat = objects.get(1);

        assertEquals("DebrisOne", debris.satelliteName);
        assertEquals("SatOne", sat.satelliteName);
        assertEquals("Debris", debris.getObjectType());
        assertEquals("Satellite", sat.getObjectType());
        assertEquals(5000, debris.daysOld);
        assertEquals(2000, sat.daysOld);
    }

    @Test
    void testAuthorizeUser_noCrash() {
        // Enough inputs to log in and then back out of each role's menu
        String scientistInput = String.join("\n", "alice", "pass123", "3");
        String adminInput = String.join("\n", "bob", "adminpass", "4");
        String agencyInput = String.join("\n", "carol", "agencypass", "3");
        String policymakerInput = String.join("\n", "dave", "policy123", "3");

        assertDoesNotThrow(
                () -> new MissionControl(TEMP_DATA.toString(), new Scanner(scientistInput)).authorizeUser("Scientist"));
        assertDoesNotThrow(
                () -> new MissionControl(TEMP_DATA.toString(), new Scanner(adminInput)).authorizeUser("Admin"));
        assertDoesNotThrow(
                () -> new MissionControl(TEMP_DATA.toString(), new Scanner(agencyInput)).authorizeUser("Agency"));
        assertDoesNotThrow(() -> new MissionControl(TEMP_DATA.toString(), new Scanner(policymakerInput))
                .authorizeUser("Policymaker"));
    }
}
