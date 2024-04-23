package kettlebell.weather.servlet.work;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import kettlebell.weather.dto.LocationDto;
import kettlebell.weather.dto.UserDto;
import kettlebell.weather.exception.validator.ValidationException;

import java.io.IOException;
import java.util.List;


@WebServlet(name = "MainPageServlet", value = "/main")
public class MainPageServlet extends ParentForMainAndLocationServlet {

   @Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

		if(request.getParameter("idLocation")!=null) {			
			doDelete(request, response);			
			return;
		}

       UserDto userDto = getSeanceService().findUserDtoFromSeance(getKeySeance());
			getWebContext().setVariable("login", userDto.getLogin()/*getLoginUser()*/);
			getWebContext().setVariable("locationDtos", userDto.getLocationDtos());

			getTemplateEngine().process("main", getWebContext(),response.getWriter());
		}

   @Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException, ValidationException {

		String nameTownParam = request.getParameter("nameTown");

       List<LocationDto> locationDtos = getLocationService().getListOfLocationsByCityName(getKeySeance(), nameTownParam);

	   		getWebContext().setVariable("login",getLoginUser());
			getWebContext().setVariable("locationDtos", locationDtos);
			getTemplateEngine().process("location", getWebContext(),response.getWriter());

	}


	@Override
	protected void doDelete(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		String indexLocation = req.getParameter("idLocation");

		getLocationService().removeLocationWithUser(Long.valueOf(indexLocation));

		resp.sendRedirect("main");

	}

}
