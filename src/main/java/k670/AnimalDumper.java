package k670;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.FileSystemXmlApplicationContext;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;


public class AnimalDumper {
    private final Logger logger = LoggerFactory.getLogger(AnimalDumper.class);
    private ScheduledExecutorService executorService;
    private int poolSize;

    private AnimalDumper() {
        poolSize = 4;
    }

    private AnimalDumper(int poolSize) {
        this.poolSize = poolSize;
    }

    public void start() {
        logger.info("pool size is {}", poolSize);
        Runnable writeToFile = new Runnable() {
            @Override
            public void run() {
                String filePath = String.format("%s%s%s", System.getProperty("java.io.tmpdir"), System.getProperty("file.separator"), "spring.xml");
                ApplicationContext applicationContext = new FileSystemXmlApplicationContext(filePath);
                AnimalStorage animalStorage = applicationContext.getBean(AnimalStorage.class);//null?

                animalStorage.writeAnimalsToFile();
                logger.info("Write animals to file");
            }
        };
        executorService = Executors.newScheduledThreadPool(poolSize);
        executorService.scheduleAtFixedRate(writeToFile, 1, 1, TimeUnit.MINUTES);
    }

    public void end() {
        executorService.shutdown();
    }

}

