package k670;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Field;
import java.util.Random;
import java.util.concurrent.ScheduledThreadPoolExecutor;

import static java.lang.Thread.sleep;
import static org.junit.jupiter.api.Assertions.*;

class AnimalDumperTest {
    ScheduledThreadPoolExecutor executorService;
    @BeforeEach
    void beforeEach() throws NoSuchFieldException, IllegalAccessException {

        AnimalDumper animalDumper = AnimalDumper.get2Instance();
        animalDumper.start();
        Field executorServiceField = animalDumper.getClass().getDeclaredField("executorService");
        executorServiceField.setAccessible(true);
        executorService = (ScheduledThreadPoolExecutor) executorServiceField.get(animalDumper);
    }
    @AfterEach
    void afterEach(){
        AnimalDumper.get2Instance().end();
    }

    @Test
    void poolSize() {
        int count = executorService.getCorePoolSize();
        assertEquals(4,count);
    }
    @Test
    void start() throws InterruptedException {
        int minutes = (new Random()).nextInt(4)+1;
        sleep(minutes*60*1000);
        long count = executorService.getCompletedTaskCount();
        assertEquals(minutes-1,count);
    }

    @Test
    void end() {
        AnimalDumper.get2Instance().end();
        boolean end = executorService.isShutdown();
        assertTrue(end);
    }
}