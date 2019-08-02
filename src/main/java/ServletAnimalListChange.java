
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@WebServlet(name = "ServletAnimalListChange")
public class ServletAnimalListChange extends HttpServlet {
    private AnimalNameAndSayData data;
    private static final Logger logger = LoggerFactory.getLogger(ServletAnimalListChange.class);

    @Override
    public void init() throws ServletException {
        data = AnimalNameAndSayData.getInstance();
        super.init();
        MDC.put("From", ServletAnimalListChange.class.toString());
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        PrintWriter pw = response.getWriter();
        String body = request.getReader().lines().collect(Collectors.joining(System.lineSeparator()));
        logger.trace("Read body from POST");
        try {
            Map<String, String> animalHashMap;
            TypeReference<HashMap<String, String>> typeRef = new TypeReference<HashMap<String, String>>() { };
            ObjectMapper mapper = new ObjectMapper();
            animalHashMap = mapper.readValue(body, typeRef);

            if (animalHashMap.get("name") != null && animalHashMap.get("say") != null) {
                logger.info("Successfully created new animal from POST");
                pw.println("{\"status\": \"" + (data.setAnimal(animalHashMap.get("name"), animalHashMap.get("say")) == null ? "Successfully created" : "Value changed") + "\"}");
                ((HttpServletResponse) response).setStatus(201);
            } else {
                logger.error("Name or say parameter is missing from POST");
                ((HttpServletResponse) response).sendError(400, "wrong data");
            }
        } catch (IOException e) {
            logger.error("Can't parse body to JSON from POST");
            ((HttpServletResponse) response).sendError(400, "not json format\n" + e.getLocalizedMessage());
        }
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        PrintWriter pw = response.getWriter();
        pw.println(animalsToJSON());
        logger.trace("Write animals' list");
    }

    private String animalsToJSON() {
        String jsonResult = null;
        Map<String, String> animalList = new HashMap<>();
        AnimalNameAndSayData data = AnimalNameAndSayData.getInstance();
        Collection<String> animalsName = data.getAll();
        animalsName.forEach(name -> {
            animalList.put(name, data.getAnimal(name));
        });
        ObjectMapper mapper = new ObjectMapper();
        try {
            jsonResult = mapper.writerWithDefaultPrettyPrinter().writeValueAsString(animalList);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        return jsonResult;
    }

    @Override
    protected void doDelete(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        PrintWriter pw = response.getWriter();
        Optional<String> animalNameFromParameter = Optional.ofNullable(request.getParameter("name"));
        if (animalNameFromParameter.isPresent()) {
            data.removeAnimal(animalNameFromParameter.get());
            logger.info("Successfully remove new animal from DELETE use query");
        } else {
            String body = request.getReader().lines().collect(Collectors.joining(System.lineSeparator()));
            logger.trace("Read body from DELETE");
            try {
                Map<String, String> animalHashMap;
                TypeReference<HashMap<String, String>> typeRef = new TypeReference<HashMap<String, String>>() {      };
                ObjectMapper mapper = new ObjectMapper();
                animalHashMap = mapper.readValue(body, typeRef);
                if (animalHashMap.get("name") != null) {
                    if (data.getAnimal(animalHashMap.get("name")) == null) {
                        pw.println("{\"status\": \"List don't contain this animal\"}");
                        logger.info("List don't contain this animal DELETE");

                    } else {
                        data.removeAnimal(animalHashMap.get("name"));
                        pw.println("{\"status\": \"Successfully remove\"}");
                        logger.info("Successfully remove new animal from DELETE use body");
                    }
                } else {
                    ((HttpServletResponse) response).sendError(400);
                    logger.error("Name or say parameter is missing from DELETE");
                }
            } catch (IOException e) {
                logger.error("Body format isn't JSON from DELETE");
                ((HttpServletResponse) response).sendError(400, "not json format\n" + e.getLocalizedMessage());
            }
        }
    }

    @Override
    public void destroy() {
        MDC.clear();
        super.destroy();
    }
}
