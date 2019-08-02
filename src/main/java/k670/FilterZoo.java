package k670;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Optional;

@WebFilter(filterName = "k670.FilterZoo")
public class FilterZoo implements Filter {
    private static final Logger logger = LoggerFactory.getLogger(FilterZoo.class);
    private AnimalNameAndSayData data;

    public void doFilter(ServletRequest req, ServletResponse resp, FilterChain chain) throws ServletException, IOException {
        MDC.put("From", "k670.FilterZoo");
        Optional<String> animalNameFromPath = Optional.ofNullable(((HttpServletRequest) req).getPathInfo());
        String[] splitAnimalNameFromPath = animalNameFromPath.map(s -> s.split("/")).orElseGet(() -> new String[0]);

        if (splitAnimalNameFromPath.length > 1 && data.getAnimal(splitAnimalNameFromPath[1]) == null) {
            ((HttpServletResponse) resp).sendError(400);
            logger.error("List don't contain animal from path");
            return;
        }

        if (req.getParameter("animal") != null && data.getAnimal(req.getParameter("animal")) == null) {
            ((HttpServletResponse) resp).sendError(400);
            logger.error("List don't contain animal from query");
            return;
        }

        logger.trace("");
        logger.debug("");
        logger.info("");
        logger.warn("");
        logger.error("");
        MDC.clear();

        chain.doFilter(req, resp);
    }

    public void init(FilterConfig config) throws ServletException {
        data = AnimalNameAndSayData.getInstance();
    }

    @Override
    public void destroy() {
        
    }
}
