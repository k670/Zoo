package k670;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.FileSystemXmlApplicationContext;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import java.util.Optional;

public class AnimalDumperListener implements ServletContextListener {

    private AnimalDumper getAnimalDumper(){
        Logger logger = LoggerFactory.getLogger("AnimalDumper");
        try {
            String filePath = String.format("%s%s%s", System.getProperty("java.io.tmpdir"), System.getProperty("file.separator"), "spring.xml");
            ApplicationContext applicationContext = new FileSystemXmlApplicationContext(filePath);
            AnimalDumper animalDumper = null;
            try {

                animalDumper= applicationContext.getBean(AnimalDumper.class);
            }catch (Exception e){
                logger.error(e.getMessage());
            }

            return animalDumper;
        }catch (Exception e){
            logger.error(e.getMessage());
        }
        return null;
    }
    @Override
    public void contextInitialized(ServletContextEvent sce) {
        Optional<AnimalDumper> animalDumper =  Optional.ofNullable(getAnimalDumper());
        if (animalDumper.isPresent()) {
            animalDumper.get().start();
        }
    }

    @Override
    public void contextDestroyed(ServletContextEvent sce) {

        Optional<AnimalDumper> animalDumper =  Optional.ofNullable(getAnimalDumper());
        if (animalDumper.isPresent()) {
            animalDumper.get().end();
        }
    }
}
