package com.pwr.search.engines.wikipedia;


import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.val;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

import static java.util.Objects.requireNonNull;

class DirectoryReader {

    private static final String JSON_FILE_NAME = "text.json";

    private static final String DIRECTORY_TO_SKIP = "img";

    private final Logger log = LoggerFactory.getLogger(DirectoryReader.class);

    private final String rootDirectoryPath;

    DirectoryReader(String rootDirectoryPath) {
        this.rootDirectoryPath = requireNonNull(rootDirectoryPath);
    }

    public Set<WikipediaArticleDTO> read() {
        Set<WikipediaArticleDTO> articles = new HashSet<>();
        File rootDirectory = new File(rootDirectoryPath);
        if (rootDirectory.isDirectory()) {
            readSubDirectories(rootDirectory, articles);
        }
        return articles;
    }

    private void readSubDirectories(File directory, Set<WikipediaArticleDTO> acc) {
        File[] fieldsAndDirectories = directory.listFiles();
        if (fieldsAndDirectories != null) {
            for (File file : fieldsAndDirectories) {
                if (isDirectoryThatShouldNotBeSkipped(file)) {
                    readSubDirectories(file, acc);
                } else {
                    if (file.getName().equals(JSON_FILE_NAME)) {
                        val deserialized = readJsonFile(file);
                        deserialized.ifPresent(acc::add);
                    }
                }
            }
        }
    }

    private boolean isDirectoryThatShouldNotBeSkipped(File file) {
        return file.isDirectory() && !file.getName().equals(DIRECTORY_TO_SKIP);
    }

    private Optional<WikipediaArticleDTO> readJsonFile(File jsonFile) {
        try {
            String json = Files.readString(Paths.get(jsonFile.getAbsolutePath()));
            return Optional.ofNullable(new ObjectMapper().readValue(preprocessJSON(json), WikipediaArticleDTO.class));
        } catch (IOException e) {
            log.error("Could not read file", e);
        }
        return Optional.empty();
    }

    private String preprocessJSON(String json) {
        if (json.startsWith("\"") && json.endsWith("\"")) {
            json = json.substring(1, json.length() - 1);
        }
        json = json.replace("\\\"", "\"")
                .replace("\\\\", "\\");
        return json;
    }

}


