package k670;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.FileSystemXmlApplicationContext;

import java.lang.reflect.Field;
import java.util.Random;
import java.util.concurrent.ScheduledThreadPoolExecutor;

import static java.lang.Thread.sleep;
import static org.junit.jupiter.api.Assertions.*;

class AnimalDumperTest {
    ScheduledThreadPoolExecutor executorService;
    AnimalDumper animalDumper;
    @BeforeEach
    void beforeEach() throws NoSuchFieldException, IllegalAccessException {
        String filePath = String.format("%s%s%s", System.getProperty("java.io.tmpdir"), System.getProperty("file.separator"), "spring.xml");
        ApplicationContext applicationContext = new FileSystemXmlApplicationContext(filePath);
        animalDumper = applicationContext.getBean(AnimalDumper.class);
        animalDumper.start();
        Field executorServiceField = animalDumper.getClass().getDeclaredField("executorService");
        executorServiceField.setAccessible(true);
        executorService = (ScheduledThreadPoolExecutor) executorServiceField.get(animalDumper);
    }
    @AfterEach
    void afterEach(){
        animalDumper.end();
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
        animalDumper.end();
        boolean end = executorService.isShutdown();
        assertTrue(end);
    }
}