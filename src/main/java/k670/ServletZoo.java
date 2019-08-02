package k670;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Optional;


public class ServletZoo extends HttpServlet {

    private static final Logger logger = LoggerFactory.getLogger(ServletZoo.class);
    private AnimalNameAndSayData data;

    @Override
    public void init() throws ServletException {
        data = AnimalNameAndSayData.getInstance();
        super.init();
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

    }

    //http://localhost:3000/hello/dog?animal=sparrow
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        MDC.put("From", "k670.ServletZoo");

        PrintWriter pw = response.getWriter();
        String animalNameNothing = "nothing";

        Optional<String> animalNameFromPath = Optional.ofNullable(request.getPathInfo());
        Optional<String> animalNameFromParameter = Optional.ofNullable(request.getParameter("animal"));

        String[] splitAnimalNameFromPath = animalNameFromPath.isPresent() ? animalNameFromPath.get().split("/") : new String[0];
        animalNameFromPath = Optional.ofNullable(splitAnimalNameFromPath.length > 1 ? splitAnimalNameFromPath[1] : animalNameNothing);
        String animalSayFromPath = animalNameFromPath.orElse(animalNameNothing).toLowerCase();
        String animalSayFromParameter = animalNameFromParameter.orElse(animalNameNothing).toLowerCase();

        animalSayFromPath = animalSayFromPath + (" say:\t" + data.getAnimal(animalSayFromPath));
        animalSayFromParameter = animalSayFromParameter + (" say:\t" + data.getAnimal(animalSayFromParameter));

        pw.println("<html>");
        for (int i = 0; i < 5; i++) {

            pw.println("<h1> \t" + animalSayFromPath + (i % 3 == 0 ? "?" : "") + " </h1>");
            pw.println("<h1> \t" + animalSayFromParameter + " </h1>");
        }


        pw.println("</html>");

        MDC.put("animalSayFromPath", animalSayFromPath);
        MDC.put("animalSayFromParameter", animalSayFromParameter);

        logger.trace("");
        logger.debug("");
        logger.info("");
        logger.warn("");
        logger.error("");


        MDC.clear();
    }
}
