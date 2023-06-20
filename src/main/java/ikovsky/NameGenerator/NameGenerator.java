package ikovsky.NameGenerator;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.stream.Stream;

public class NameGenerator {

    private static final int ADJECTIVES_LENGTH = 28479;
    private static final int ADVERBS_LENGTH = 6276;
    private static final int NOUN_LENGTH = 90921;

    private int randomBetween(int min, int max) {
        return min + (int) (Math.random() * ((max - min) + 1));
    }

    private String randomNoun() {
        int n = randomBetween(0, NOUN_LENGTH);
        return fetchLineFromFile(n, "src/main/resources/nouns.txt");
    }

    private String randomAdverb() {
        int n = randomBetween(0, ADVERBS_LENGTH);
        return fetchLineFromFile(n, "src/main/resources/adverbs.txt");
    }

    private String randomAdjective() {
        int n = randomBetween(0, ADJECTIVES_LENGTH);
        return fetchLineFromFile(n, "src/main/resources/adjectives.txt");
    }

    private String fetchLineFromFile(int n, String filename) {
        try (Stream<String> lines = Files.lines(Paths.get(filename))) {
            return lines.skip(n).findFirst().get();
        } catch (IOException e) {
            System.out.println(e);
            return "";
        }
    }

    private String generateFullName() {
        var songStructure = randomBetween(1, 8);
        if (songStructure == 1) {
            return randomAdjective() + " " + randomAdjective() + " " + randomNoun();
        } else if (songStructure == 2) {
            return randomAdjective() + " " + randomAdverb() + " " + randomNoun();
        } else if (songStructure == 3) {
            return randomAdverb() + " " + randomAdjective() + " " + randomNoun();
        } else if (songStructure == 4) {
            return randomAdjective() + " " + randomNoun();
        } else if (songStructure == 5) {
            return randomAdverb() + " " + randomNoun();
        } else if (songStructure == 6) {
            return randomAdverb();
        } else if (songStructure == 7) {
            return randomAdjective();
        } else {
            return randomNoun();
        }
    }

    public String generateName() {
        while (true) {
            String songTitle = generateFullName();

            //Check that the length of the name can be safely displayed on the interface.
            if (songTitle.length() <= 30) {
                return songTitle;
		    }
        }
    }
}
