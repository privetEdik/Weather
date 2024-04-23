package kettlebell.weather.servlet.work;

import jakarta.servlet.ServletException;

import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import kettlebell.weather.exception.AppException;

import java.io.IOException;

@WebServlet(name = "LocationServlet", value = "/location")
public class LocationServlet extends ParentForMainAndLocationServlet {


	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException, AppException {
		String param = request.getParameter("indexLocation");
		Integer indexLocation = param == null ? 0 : Integer.parseInt(param);

		getLocationService().addLocationForUser(getKeySeance(), indexLocation);

		getWebContext().setVariable("login", getLoginUser());
		getTemplateEngine().process("location", getWebContext(),response.getWriter());

	}
	
}
