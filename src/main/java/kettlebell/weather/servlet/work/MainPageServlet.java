package kettlebell.weather.servlet.work;

import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import kettlebell.weather.dto.user.LocationDto;
import kettlebell.weather.dto.user.UserDto;
import kettlebell.weather.exception.validator.ValidationException;
import kettlebell.weather.repository.LocationRepository;
import kettlebell.weather.repository.SeanceRepository;
import kettlebell.weather.service.LocationService;
import kettlebell.weather.service.SeanceService;
import kettlebell.weather.validator.NameTownValidator;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.util.List;

@Slf4j
@WebServlet(name = "MainPageServlet", value = "/main")
public class MainPageServlet extends ParentForMainAndLocationServlet {


    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {

        if (request.getParameter("idLocation") != null) {
            doDelete(request, response);
            return;
        }
        setSeanceService(new SeanceService(SeanceRepository.getInstance(), LocationRepository.getInstance()));
        UserDto userDto = getSeanceService().findUserDtoFromSeance(getKeySeance());

        getWebContext().setVariable("login", userDto.getLogin());
        getWebContext().setVariable("locationDtos", userDto.getLocationDtos());

        getTemplateEngine().process("main", getWebContext(), response.getWriter());
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException, ValidationException {

        String nameTownParam = request.getParameter("nameTown");
        log.info("nameTown : {}" , nameTownParam);
        setLocationService(new LocationService(LocationRepository.getInstance()));
        NameTownValidator.getInstance().isValid(nameTownParam);
        List<LocationDto> locationDtos = getLocationService().getListOfLocationsByCityName(getKeySeance(), nameTownParam);

        getWebContext().setVariable("login", getLoginUser());
        getWebContext().setVariable("locationDtos", locationDtos);
        getTemplateEngine().process("location", getWebContext(), response.getWriter());

    }


    @Override
    protected void doDelete(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        String indexLocation = req.getParameter("idLocation");

        setLocationService(new LocationService(LocationRepository.getInstance()));
        getLocationService().removeLocationWithUser(Long.valueOf(indexLocation));

        resp.sendRedirect("main");

    }

}
