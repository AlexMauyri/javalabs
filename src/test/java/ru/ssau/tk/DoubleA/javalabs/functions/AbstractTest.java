package ru.ssau.tk.DoubleA.javalabs.functions;

import java.io.IOException;
import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.Path;

public abstract class AbstractTest {
    protected final static double EPSILON = 1E-2 * 5;

    protected static void cleanUpDirectory(Path directoryPath) {
        try (DirectoryStream<Path> files = Files.newDirectoryStream(directoryPath)) {
            for (Path file : files) {
                if (Files.isDirectory(file)) cleanUpDirectory(file);
                Files.delete(file);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
