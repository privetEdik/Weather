package kettlebell.weather.servlet;

import jakarta.servlet.ServletConfig;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

import kettlebell.weather.repository.localdb.UserRepositoryDb;
import kettlebell.weather.service.SeanceService;
import kettlebell.weather.service.UserService;
import kettlebell.weather.util.ThymeleafUtil;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.WebContext;

@Slf4j
@Getter
public class BaseServlet extends HttpServlet {
    private TemplateEngine templateEngine;
    @Setter
    private WebContext webContext;
    private UserService userService;
    @Setter
    private SeanceService seanceService;


    @Override
    public void init(ServletConfig config) throws ServletException {
        templateEngine = ThymeleafUtil.INSTANCE.getTemplateEngine();
        userService = new UserService(UserRepositoryDb.getInstance());

        super.init(config);
    }

    @Override
    protected void service(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        webContext = ThymeleafUtil.buildWebContext(req, resp);

        super.service(req, resp);
    }

    public BaseServlet() {

        super();
    }

}
