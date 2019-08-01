import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import static java.lang.Thread.sleep;

public class AnimalDumper {
    private final Logger logger = LoggerFactory.getLogger(AnimalDumper.class);
    private ScheduledExecutorService executorService;
    public final static AnimalDumper animalDumper = new AnimalDumper();

    private AnimalDumper() {
    }

    public void start() {

        Runnable writeToFile = new Runnable() {
            @Override
            public void run() {
                AnimalStorage animalStorage = AnimalStorage.getInstance();
                animalStorage.writeAnimalsToFile();
                logger.info("Write animals to file");
            }
        };
        executorService = Executors.newScheduledThreadPool(4);
        executorService.scheduleAtFixedRate(writeToFile, 1, 1, TimeUnit.MINUTES);
    }

    public void end() {
        executorService.shutdown();
    }

    public static AnimalDumper getInstance() {
        return animalDumper;
    }
}
