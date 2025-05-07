package com.team22;

import org.junit.jupiter.api.*;

import java.io.*;
import java.nio.file.*;

import static org.junit.jupiter.api.Assertions.*;

public class LoggerTest {

    private static final String TEST_LOG_DIR = "logs";
    private static final String TEST_LOG_FILE = TEST_LOG_DIR + "/system_log.txt";

    @BeforeEach
    void setUp() throws IOException {
        Files.createDirectories(Paths.get(TEST_LOG_DIR));
        Files.write(Paths.get(TEST_LOG_FILE), new byte[0]); // Clear log file
    }

    @AfterEach
    void tearDown() throws IOException {
        Files.deleteIfExists(Paths.get(TEST_LOG_FILE));
    }

    @Test
    void testLogWritesTimestampedMessage() throws IOException {
        String message = "Test logging event";
        Logger.log(message);

        String logLine = Files.readAllLines(Paths.get(TEST_LOG_FILE)).get(0);

        // Check timestamp and message format
        assertTrue(logLine.contains(message), "Log should contain the message");
        assertTrue(logLine.matches("\\[\\d{4}-\\d{2}-\\d{2} \\d{2}:\\d{2}:\\d{2}] " + message),
                "Log line format should match timestamped format");
    }

    @Test
    void testLogAppendsMultipleMessages() throws IOException {
        Logger.log("First");
        Logger.log("Second");

        var lines = Files.readAllLines(Paths.get(TEST_LOG_FILE));
        assertEquals(2, lines.size());
        assertTrue(lines.get(0).contains("First"));
        assertTrue(lines.get(1).contains("Second"));
    }

    @Test
    void testLogDirectoryCreatedIfMissing() throws IOException {
        // Delete the log folder and file if they exist
        Files.deleteIfExists(Paths.get(TEST_LOG_FILE));
        Files.deleteIfExists(Paths.get(TEST_LOG_DIR));

        Logger.log("Recreate test");

        assertTrue(Files.exists(Paths.get(TEST_LOG_FILE)), "Log file should be recreated");
        String logLine = Files.readAllLines(Paths.get(TEST_LOG_FILE)).get(0);
        assertTrue(logLine.contains("Recreate test"));
    }
}
