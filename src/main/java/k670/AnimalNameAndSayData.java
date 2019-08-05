package k670;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.FileSystemXmlApplicationContext;

import java.util.*;

public class AnimalNameAndSayData {
    private final static AnimalNameAndSayData data = new AnimalNameAndSayData();
    private Map<String, String> animalList;

    private AnimalNameAndSayData() {

        //to xml?
        String filePath = String.format("%s%s%s", System.getProperty("java.io.tmpdir"), System.getProperty("file.separator"), "spring.xml");
        ApplicationContext applicationContext = new FileSystemXmlApplicationContext(filePath);
        AnimalStorage animalStorage = applicationContext.getBean(AnimalStorage.class);//null?
        //to xml

        Optional<Map<String, String>> optionalAnimals = Optional.ofNullable(animalStorage.readAnimalsFromFile());
        animalList = optionalAnimals.orElse(new HashMap<>());

    }

    public static AnimalNameAndSayData getInstance() {
        return data;
    }

    public String getAnimal(String name) {
        return animalList.get(name);
    }

    public String setAnimal(String key, String value) {
        return animalList.put(key, value);
    }

    public void removeAnimal(String key) {
        animalList.remove(key);
    }

    public Collection<String> getAll() {
        return new ArrayList<>(animalList.keySet());
    }

    public void deleteAll(Collection<String> animals) {
        animals.forEach(animalList::remove);
    }

}
