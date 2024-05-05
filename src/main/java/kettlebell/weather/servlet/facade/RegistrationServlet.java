package kettlebell.weather.servlet.facade;

import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import kettlebell.weather.dto.CreateUserDto;
import kettlebell.weather.exception.EntityAlreadyExistsException;
import kettlebell.weather.exception.validator.ValidationException;
import kettlebell.weather.servlet.BaseServlet;

import java.io.IOException;

@WebServlet(name = "RegistrationServlet", value = "/registration")
public class RegistrationServlet extends BaseServlet {

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {

        getTemplateEngine().process("registration", getWebContext(), response.getWriter());
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
        CreateUserDto createUserDto = CreateUserDto.builder()
                .login(request.getParameter("login"))
                .password(request.getParameter("password"))
                .build();
        try {
            getUserService().create(createUserDto);
            getWebContext().setVariable("success", "Successful registration!");

        } catch (ValidationException | EntityAlreadyExistsException e) {
            getWebContext().setVariable("error", e.getError());

        }
        doGet(request, response);
    }

}