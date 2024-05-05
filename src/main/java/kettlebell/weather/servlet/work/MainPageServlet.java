package kettlebell.weather.servlet.work;

import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import kettlebell.weather.dto.LocationDto;
import kettlebell.weather.dto.UserDto;
import kettlebell.weather.exception.validator.ValidationException;
import kettlebell.weather.repository.http.LocationRepositoryHttp;
import kettlebell.weather.repository.localdb.LocationRepositoryDb;
import kettlebell.weather.repository.localdb.SeanceRepositoryDb;
import kettlebell.weather.service.LocationService;
import kettlebell.weather.service.SeanceService;

import java.io.IOException;
import java.util.List;


@WebServlet(name = "MainPageServlet", value = "/main")
public class MainPageServlet extends ParentForMainAndLocationServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {

        if (request.getParameter("idLocation") != null) {
            doDelete(request, response);
            return;
        }
        setSeanceService(new SeanceService(SeanceRepositoryDb.getInstance(), LocationRepositoryHttp.getInstance()));
        UserDto userDto = getSeanceService().findUserDtoFromSeance(getKeySeance());

        getWebContext().setVariable("login", userDto.getLogin());
        getWebContext().setVariable("locationDtos", userDto.getLocationDtos());

        getTemplateEngine().process("main", getWebContext(), response.getWriter());
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException, ValidationException {

        String nameTownParam = request.getParameter("nameTown");

        setLocationService(new LocationService(LocationRepositoryHttp.getInstance()));
        List<LocationDto> locationDtos = getLocationService().getListOfLocationsByCityName(getKeySeance(), nameTownParam);

        getWebContext().setVariable("login", getLoginUser());
        getWebContext().setVariable("locationDtos", locationDtos);
        getTemplateEngine().process("location", getWebContext(), response.getWriter());

    }


    @Override
    protected void doDelete(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        String indexLocation = req.getParameter("idLocation");

        setLocationService(new LocationService(LocationRepositoryDb.getInstance()));
        getLocationService().removeLocationWithUser(Long.valueOf(indexLocation));

        resp.sendRedirect("main");

    }

}
