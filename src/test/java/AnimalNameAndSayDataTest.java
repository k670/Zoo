import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.*;
import java.util.UUID;

class AnimalNameAndSayDataTest {

    private final AnimalNameAndSayData data = AnimalNameAndSayData.getInstance();
    private String animalSay;
    private String animalName;

    @BeforeEach
    public void before() {
        animalSay = UUID.randomUUID().toString();
        animalName = UUID.randomUUID().toString();
        data.setAnimal(animalName, animalSay);
    }

    @AfterEach
    void after() {
        data.deleteAll(data.getAll());
    }

    @Test
    void getAnimalNotExist() {
        animalName = UUID.randomUUID().toString();
        assertNull(data.getAnimal(animalName));
    }

    @Test
    void getAnimal() {
        assertEquals(animalSay, data.getAnimal(animalName));
    }

    @Test
    void setAnimalTwice() {
        String animalSay0 = UUID.randomUUID().toString();
        data.setAnimal(animalName, animalSay0);
        String answ = data.getAnimal(animalName);
        assertEquals(animalSay0, answ);
    }

    @Test
    @DisplayName("Instance not null")
    void getInstanceNotNull() {
        assertNotNull(data);
    }

    @Test
    void getInstanceOnlyOne() {
        assertEquals(AnimalNameAndSayData.getInstance(), data);
    }

    @Test
    void removeAnimalNotExist() {
        String animalNameNotExist = UUID.randomUUID().toString();
        data.removeAnimal(animalNameNotExist);
        assertNull(data.getAnimal(animalNameNotExist));
    }

    @Test
    @DisplayName("remove(existing animal) = true")
    void removeAnimal() {
        data.removeAnimal(animalName);
        assertNull(data.getAnimal(animalName));
    }

    @Test
    void getAll() {
        assertTrue(data.getAll().contains(animalName));
    }

    @Test
    void deleteAll() {
        data.deleteAll(data.getAll());
        assertEquals(0, data.getAll().size());
    }

}