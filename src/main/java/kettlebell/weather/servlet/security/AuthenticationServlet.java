package kettlebell.weather.servlet.security;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import kettlebell.weather.exception.UserNotFoundException;
import kettlebell.weather.exception.validator.ValidationException;
import kettlebell.weather.service.SeanceService;
import kettlebell.weather.servlet.BaseServlet;

import java.io.IOException;
@WebServlet(name = "AuthenticationServlet", value = "/authentication")
public class AuthenticationServlet extends BaseServlet {

	private final SeanceService seanceService = SeanceService.getInstance();

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

		getTemplateEngine().process("authentication", getWebContext(), resp.getWriter());
	}
	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		try {
			
			Long idUser = getUserService().authenticateByLogin(request.getParameter("login"));
						
			String keySeance = seanceService.startSeanceAndGetKey(idUser);
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
