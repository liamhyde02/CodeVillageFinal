package org.codevillage.fetching;

import lombok.extern.log4j.Log4j2;

import java.io.File;
import java.io.IOException;
import java.nio.file.*;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.ArrayList;

@Log4j2
public class LocalDataFetcher implements DataFetcher {
    @Override
    public ArrayList<File> downloadPackage(String url, boolean isLambdaEnvironment) {
        ArrayList<File> downloadedFiles = new ArrayList<>();

        try {
            // Remove the "file://" prefix if it exists
            if (url.startsWith("file://")) {
                url = url.substring(7);
            }

            // Define the source directory as Path
            Path sourceDirectory = new File(url).toPath();

            // Handle Lambda environment specific path
            String targetPath = isLambdaEnvironment ? "/tmp" : "temp";
            Path targetDirectory = new File(targetPath).toPath();
            if (!Files.exists(targetDirectory)) {
                Files.createDirectories(targetDirectory);
            }

            // Copy the source directory and its contents to the target directory
            Path finalTargetDirectory = targetDirectory;
            Files.walkFileTree(sourceDirectory, new SimpleFileVisitor<>() {
                @Override
                public FileVisitResult preVisitDirectory(Path dir, BasicFileAttributes attrs) throws IOException {
                    Path targetDir = finalTargetDirectory.resolve(sourceDirectory.relativize(dir));
                    Files.createDirectories(targetDir);
                    return FileVisitResult.CONTINUE;
                }

                @Override
                public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) throws IOException {
                    Path targetFile = finalTargetDirectory.resolve(sourceDirectory.relativize(file));
                    Files.copy(file, targetFile, StandardCopyOption.REPLACE_EXISTING);
                    downloadedFiles.add(targetFile.toFile());
                    return FileVisitResult.CONTINUE;
                }
            });
        } catch (IOException e) {
            log.error("An error occurred while copying the file: ", e);
        }

        return downloadedFiles;
    }
}
