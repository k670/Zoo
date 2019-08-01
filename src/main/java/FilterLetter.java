import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Optional;

@WebFilter(filterName = "FilterLetter")
public class FilterLetter implements Filter {
    public void destroy() {
    }

    public void doFilter(ServletRequest req, ServletResponse resp, FilterChain chain) throws ServletException, IOException {

        Optional<String> animal1 = Optional.ofNullable(((HttpServletRequest) req).getPathInfo());
        String[] pathAnimal1 = animal1.isPresent() ? animal1.get().split("/") : new String[0];

        if (pathAnimal1.length > 1 && pathAnimal1[1].length()%2 == 0) {
            ((HttpServletResponse) resp).sendError(500);
            return;
        }

        if (req.getParameter("animal") != null && req.getParameter("animal").length()%2 == 0) {
            ((HttpServletResponse) resp).sendError(500);
            return;
        }

        chain.doFilter(req, resp);
    }

    public void init(FilterConfig config) throws ServletException {

    }

}
