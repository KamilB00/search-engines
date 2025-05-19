package com.pwr.search.engines.elasticsearch;

import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Random;

@Repository
public class WikipediaArticleCategoriesRepository {

    private final List<String> categories = List.of(
            "Culture",
            "Geography",
            "History",
            "Science",
            "Technology",
            "Mathematics",
            "Health",
            "Arts",
            "Politics",
            "Economics",
            "Education",
            "Philosophy",
            "Religion",
            "Sports",
            "Society",
            "Nature",
            "Biography",
            "Transportation",
            "Environment",
            "Law",
            "Language",
            "Media",
            "Military",
            "Music",
            "Literature"
    );

    public String getRandomCategory() {
        int max = categories.size() - 1;
        int min = 0;
        Random rand = new Random();
        int randomInRange = rand.nextInt((max - min) + 1) + min;
        return categories.get(randomInRange);
    }

}
