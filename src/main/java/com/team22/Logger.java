package com.team22;

/**
 * Logger utility for tracking all system actions.
 * Appends timestamped entries to a persistent log file.
 */
import java.io.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class Logger {

    private static final String LOG_FILE_PATH = "logs/system_log.txt";
    private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    /**
     * Logs a message to the system log with a timestamp.
     * Creates the log folder and file if they do not exist.
     *
     * @param message the log message to write
     */
    public static void log(String message) {
        try {
            File logDir = new File("logs");
            if (!logDir.exists()) {
                logDir.mkdirs();
            }

            try (BufferedWriter writer = new BufferedWriter(new FileWriter(LOG_FILE_PATH, true))) {
                String timestamp = LocalDateTime.now().format(FORMATTER);
                writer.write("[" + timestamp + "] " + message + "\n");
            }
        } catch (IOException e) {
            System.out.println("Failed to write log: " + e.getMessage());
        }
    }
}
