package com.team22;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.*;

import java.io.*;
import java.nio.file.*;
import java.util.*;

public class UserManagerTest {

    private static final String TEST_USER_FILE = "data/test_users.csv";
    private static final String TEST_LOG_FILE = "logs/system_log.txt";
    private UserManager userManager;

    @BeforeEach
    void setUp() throws IOException {
        Files.createDirectories(Paths.get("data"));
        Files.write(Paths.get(TEST_USER_FILE), new byte[0]);
        userManager = new UserManager(TEST_USER_FILE);
    }

    @AfterEach
    void tearDown() throws IOException {
        Files.deleteIfExists(Paths.get(TEST_USER_FILE));
    }

    @Test
    void testCreateUser() throws IOException {
        String simulatedInput = "testuser\nScientist\n";
        provideInput(simulatedInput);

        userManager.createUser();

        List<String> lines = Files.readAllLines(Paths.get(TEST_USER_FILE));
        assertEquals(1, lines.size());
        assertTrue(lines.get(0).contains("testuser,Scientist"));

        restoreSystemInput();
    }

    @Test
    void testListUsers() throws IOException {
        Files.write(Paths.get(TEST_USER_FILE), List.of("alice,Admin", "bob,Agency"));

        ByteArrayOutputStream output = captureOutput();
        userManager.listUsers();
        String result = output.toString();

        assertTrue(result.contains("alice,Admin"));
        assertTrue(result.contains("bob,Agency"));

        restoreSystemOutput();
    }

    @Test
    void testDeleteUser_notFound() throws IOException {
        Files.write(Paths.get(TEST_USER_FILE), List.of("alice,Admin"));

        String simulatedInput = "charlie\n";
        provideInput(simulatedInput);

        userManager.deleteUser();

        List<String> lines = Files.readAllLines(Paths.get(TEST_USER_FILE));
        assertEquals(1, lines.size());
        assertEquals("alice,Admin", lines.get(0));

        restoreSystemInput();
    }

    // ------------------ Helpers ------------------

    private void provideInput(String data) {
        System.setIn(new ByteArrayInputStream(data.getBytes()));
    }

    private void restoreSystemInput() {
        System.setIn(System.in);
    }

    private ByteArrayOutputStream captureOutput() {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        System.setOut(new PrintStream(outputStream));
        return outputStream;
    }

    private void restoreSystemOutput() {
        System.setOut(System.out);
    }

    private static void setPrivateStaticField(Class<?> clazz, String fieldName, String newValue) {
        try {
            var field = clazz.getDeclaredField(fieldName);
            field.setAccessible(true);
            field.set(null, newValue);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
