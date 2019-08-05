package k670;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;

public class AnimalStorage {
    private static final String DEFAULT_FILE_PATH = String.format("%s%s%s", System.getProperty("java.io.tmpdir"),System.getProperty("file.separator"), "animals.json");
    private String filePath;
    private final Logger logger = LoggerFactory.getLogger(AnimalStorage.class);

    private AnimalStorage() {filePath = DEFAULT_FILE_PATH;}
    private AnimalStorage(String filePath) {
        this.filePath = filePath;
    }

    private String animalsToJSON(){
        String jsonResult=null;

        Map<String, String> animalList = new HashMap<>();
        AnimalNameAndSayData data = AnimalNameAndSayData.getInstance();
        Collection<String> animalsName = data.getAll();
        animalsName.forEach(name->{
            animalList.put(name,data.getAnimal(name));
        });
        ObjectMapper mapper = new ObjectMapper();

        try {
            jsonResult = mapper.writerWithDefaultPrettyPrinter().writeValueAsString(animalList);
        } catch (JsonProcessingException e) {
            logger.error("{} :{}",JsonProcessingException.class,e.getMessage());
        }
        return jsonResult;
    }

    public void writeAnimalsToFile(){
        try {
            Files.write(Paths.get(filePath), animalsToJSON().getBytes());
            logger.info("Write animals to file.");
        } catch (IOException e) {
            logger.error("{}: {}",IOException.class,e.getMessage());
        }
    }

    public Map<String, String> readAnimalsFromFile()  {

        Path paths = Paths.get(filePath);
        Map<String, String> animalHashMap = null;
        List<String> stringList;
        try {
            StringBuilder jsonText = new StringBuilder();
            stringList = Files.readAllLines(paths);
            stringList.forEach(jsonText::append);
            TypeReference<HashMap<String, String>> typeRef = new TypeReference<HashMap<String, String>>() {};
            ObjectMapper mapper = new ObjectMapper();
            animalHashMap = mapper.readValue(jsonText.toString(), typeRef);
            logger.info("Read animals from file.");
        } catch (IOException e) {
            logger.error("{}: {}",IOException.class,e.getMessage());
        }
        return animalHashMap;
    }


}
