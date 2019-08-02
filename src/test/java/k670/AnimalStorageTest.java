package k670;

import k670.AnimalNameAndSayData;
import k670.AnimalStorage;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Map;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

class AnimalStorageTest {

    private String animalSay;
    private String animalName;
    private final String filePath = String.format("%s%s%s", System.getProperty("java.io.tmpdir"),System.getProperty("file.separator"), "test.json");
    private AnimalStorage animalStorage;

    @BeforeEach
    void beforEach() {
        animalSay = UUID.randomUUID().toString();
        animalName = UUID.randomUUID().toString();
        this.animalStorage = AnimalStorage.getInstance(filePath);
        String jsonBody = "{\n" +
                "  \"" + animalName + "\" : \"" + animalSay + "\"\n" +
                "}";
        try {
            Files.deleteIfExists(Paths.get(filePath));
            Files.write(Paths.get(filePath), jsonBody.getBytes());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Test
    void writeAnimalsToFile() throws IOException {
        AnimalNameAndSayData data = AnimalNameAndSayData.getInstance();
        data.deleteAll(data.getAll());
        data.setAnimal(animalName, animalSay);
         animalStorage.writeAnimalsToFile();
        Map<String, String> animalsMap = animalStorage.readAnimalsFromFile();
        assertEquals(animalSay, animalsMap.get(animalName));
    }

    @Test
    void readAnimalsFromFile() throws IOException {
        Map<String, String> animalHashMap =  animalStorage.readAnimalsFromFile();
        assertEquals(animalSay, animalHashMap.get(animalName));
    }

    @Test
    void readAnimalsFromFileIOException() throws IOException {
        Files.deleteIfExists(Paths.get(filePath));
        Files.write(Paths.get(filePath), animalSay.getBytes());
        assertNull(animalStorage.readAnimalsFromFile());
    }

    @AfterAll
    static void afterAll() throws IOException {

        Files.deleteIfExists(Paths.get(System.getProperty("java.io.tmpdir") + "\\test.json"));
    }
}