package kettlebell.weather.servlet.facade;

import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import kettlebell.weather.exception.UserNotFoundException;
import kettlebell.weather.exception.validator.ValidationException;
import kettlebell.weather.repository.localdb.SeanceRepositoryDb;
import kettlebell.weather.repository.localdb.UserRepositoryDb;
import kettlebell.weather.service.SeanceService;
import kettlebell.weather.servlet.BaseServlet;

import java.io.IOException;

@WebServlet(name = "AuthenticationServlet", value = "/authentication")
public class AuthenticationServlet extends BaseServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {

        getTemplateEngine().process("authentication", getWebContext(), resp.getWriter());
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws IOException {

        try {

            Long idUser = getUserService().authenticateByLogin(request.getParameter("login"));

            setSeanceService(new SeanceService(SeanceRepositoryDb.getInstance(), UserRepositoryDb.getInstance()));
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
