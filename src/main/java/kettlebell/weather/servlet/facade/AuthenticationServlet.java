package kettlebell.weather.servlet.facade;

import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import kettlebell.weather.exception.UserNotFoundException;
import kettlebell.weather.exception.validator.ValidationException;
import kettlebell.weather.repository.SeanceRepository;
import kettlebell.weather.repository.UserRepository;
import kettlebell.weather.service.SeanceService;
import kettlebell.weather.servlet.BaseServlet;
import kettlebell.weather.validator.LoginValidator;

import java.io.IOException;

@WebServlet(name = "AuthenticationServlet", value = "/authentication")
public class AuthenticationServlet extends BaseServlet {
    //private final LoginValidator loginValidator = LoginValidator.getInstance();
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {

        getTemplateEngine().process("authentication", getWebContext(), resp.getWriter());
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws IOException {

        try {
            String login = request.getParameter("login");
            LoginValidator.getInstance().isValid(login);
            Long idUser = getUserService().authenticateByLogin(login);

            setSeanceService(new SeanceService(SeanceRepository.getInstance(), UserRepository.getInstance()));
            String keySeance = getSeanceService().startSeanceAndGetKey(idUser);
            Cookie cookie = new Cookie("keySeance", keySeance);
            response.addCookie(cookie);
            cookie.setMaxAge(-1);

        } catch (ValidationException | UserNotFoundException e) {
            getWebContext().setVariable("error", e.getError());

            getTemplateEngine().process("authentication", getWebContext(), response.getWriter());
            return;
        }

        response.sendRedirect("main");
    }

}
