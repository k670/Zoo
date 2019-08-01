
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

public class AnimalDumperListener implements ServletContextListener {

    @Override
    public void contextInitialized(ServletContextEvent sce) {
        AnimalDumper.getInstance().start();
    }

    @Override
    public void contextDestroyed(ServletContextEvent sce) {
        AnimalDumper.getInstance().end();
    }
}
