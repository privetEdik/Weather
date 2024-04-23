package kettlebell.weather.servlet.work;

import java.io.IOException;
import java.util.Optional;
import java.util.stream.Stream;

import kettlebell.weather.exception.APIException;
import kettlebell.weather.exception.EntityAlreadyExistsException;
import kettlebell.weather.exception.KeySeanceNotFountException;
import kettlebell.weather.exception.SeanceEndedException;
import kettlebell.weather.exception.validator.ValidationException;
import kettlebell.weather.service.LocationService;
import kettlebell.weather.servlet.BaseServlet;
import kettlebell.weather.storage.LocationStorageInstance;
import kettlebell.weather.util.ThymeleafUtil;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

import jakarta.servlet.ServletConfig;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Getter
@Slf4j
public abstract class ParentForMainAndLocationServlet extends BaseServlet {

	private LocationService locationService;
	private String keySeance;
	private String loginUser;

	@Override
	public void init(ServletConfig config) throws ServletException {
		locationService = LocationService.getInstance();		
		super.init(config);
	}

	@Override
	protected void service(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

		try {
			getKeySeanceFromCookie(req);
			
			if (req.getParameter("logout")==null) {
				loginUser = updateSeance(keySeance);
			}else {
				doLogout(resp);
				return;
			}

			super.service(req, resp);
		}catch (KeySeanceNotFountException e){
			log.debug("key not found in cookies");
			resp.sendRedirect("authentication");
		}catch (SeanceEndedException e){
			log.info("seance ended");
			setWebContext(ThymeleafUtil.buildWebContext(req, resp));
			getWebContext().setVariable("error", e.getError());
			getTemplateEngine().process("authentication", getWebContext(), resp.getWriter());			
		}catch (ValidationException | APIException | EntityAlreadyExistsException e) {
			log.info("error validation or "+e.getError());
			getWebContext().setVariable("login",loginUser);
			getWebContext().setVariable("error", e.getError());
			getTemplateEngine().process("location", getWebContext(), resp.getWriter());

		}catch (Exception e) {
			log.info("unidentified error: "+e.getMessage());
			throw e;
		}
	
	}
	
	protected void doLogout(HttpServletResponse resp) throws IOException {

		getSeanceService().forceDelete(keySeance);
		LocationStorageInstance.INSTANCE.clearStorage(keySeance);

		resp.sendRedirect("authentication");

	}

	protected void getKeySeanceFromCookie(HttpServletRequest req) throws ValidationException{

		this.keySeance = Optional.ofNullable(req.getCookies())
				.map(cookies -> Stream.of(cookies)
						.filter(s -> s.getName().equals("keySeance"))
						.findFirst()
					)
				.map(cookie -> cookie.get().getValue())
				.orElseThrow(KeySeanceNotFountException::new);
				
	}
	
	protected String updateSeance(String keySeance) throws SeanceEndedException {

		return getSeanceService().updateSeance(keySeance);

	}

}
