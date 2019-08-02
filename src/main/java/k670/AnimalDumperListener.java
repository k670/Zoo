package k670;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.FileSystemXmlApplicationContext;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

public class AnimalDumperListener implements ServletContextListener {

    private AnimalDumper getAnimalDumper(){
        try {
            String filePath = String.format("%s%s%s", System.getProperty("java.io.tmpdir"), System.getProperty("file.separator"), "spring.xml");
            ApplicationContext applicationContext = new FileSystemXmlApplicationContext(filePath);
            return applicationContext.getBean(AnimalDumper.class);
        }catch (Exception e){
            Logger logger = LoggerFactory.getLogger("AnimalDumper");
            logger.error(e.getMessage());
        }
        return null;
    }
    @Override
    public void contextInitialized(ServletContextEvent sce) {
        getAnimalDumper().start();
    }

    @Override
    public void contextDestroyed(ServletContextEvent sce) {
        getAnimalDumper().end();
    }
}
