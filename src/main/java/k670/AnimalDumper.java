package k670;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;


public class AnimalDumper {
    private final Logger logger = LoggerFactory.getLogger(AnimalDumper.class);
    private ScheduledExecutorService executorService;
    private   static AnimalDumper animalDumper;
    private int poolSize;
    private AnimalDumper() {
        int i = 0;
        i++;
    }

    public AnimalDumper( final int poolSize) {
        this.poolSize = poolSize;
    }

    public void start() {
        logger.info("pool size is {}", poolSize);
        Runnable writeToFile = new Runnable() {
            @Override
            public void run() {
                AnimalStorage animalStorage = AnimalStorage.getInstance();
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

    public static AnimalDumper get2Instance() {
        if(animalDumper == null){
            synchronized (AnimalStorage.class) {
                if(animalDumper == null){
                    animalDumper = new AnimalDumper(4);
                }
            }
        }
        return animalDumper;
    }

    public void setPoolSize(int poolSize) {
        this.poolSize = poolSize;
    }
}

