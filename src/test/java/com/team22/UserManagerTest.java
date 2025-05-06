package com.team22;

import org.junit.jupiter.api.*;
import java.io.*;
import java.nio.file.*;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;

public class UserManagerTest {

    private static final Path TEMP_USERS = Paths.get("data/users.csv");

    @BeforeEach
    void setup() throws IOException {
        Files.createDirectories(TEMP_USERS.getParent());
        Files.write(TEMP_USERS, List.of(
                "alice,Scientist,pass123",
                "bob,Admin,adminpass"));
    }

    @AfterEach
    void teardown() throws IOException {
        Files.deleteIfExists(TEMP_USERS);
    }

    @Test
    void testVerifyLogin_successful() {
        UserManager um = new UserManager();
        Scanner scanner = new Scanner("alice\npass123\n");
        assertTrue(um.verifyLogin("Scientist", scanner));
    }

    @Test
    void testVerifyLogin_wrongPassword() {
        UserManager um = new UserManager();
        Scanner scanner = new Scanner("alice\nwrongpass\n");
        assertFalse(um.verifyLogin("Scientist", scanner));
    }

    @Test
    void testVerifyLogin_wrongRole() {
        UserManager um = new UserManager();
        Scanner scanner = new Scanner("alice\npass123\n");
        assertFalse(um.verifyLogin("Admin", scanner));
    }

    @Test
    void testVerifyLogin_userNotFound() {
        UserManager um = new UserManager();
        Scanner scanner = new Scanner("charlie\nnopass\n");
        assertFalse(um.verifyLogin("Scientist", scanner));
    }
}
