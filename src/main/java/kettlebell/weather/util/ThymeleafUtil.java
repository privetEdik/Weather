package kettlebell.weather.util;

import java.nio.charset.StandardCharsets;

import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.WebContext;
import org.thymeleaf.templatemode.TemplateMode;
import org.thymeleaf.templateresolver.ITemplateResolver;
import org.thymeleaf.templateresolver.WebApplicationTemplateResolver;
import org.thymeleaf.web.IWebApplication;
import org.thymeleaf.web.servlet.IServletWebExchange;
import org.thymeleaf.web.servlet.JakartaServletWebApplication;

import jakarta.servlet.ServletContext;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public enum ThymeleafUtil {
	INSTANCE;
	private static TemplateEngine ENGINE;
	
	public void buildTemplateEngineInstance(ServletContext context) {
		ENGINE = new TemplateEngine();
		IWebApplication application = JakartaServletWebApplication.buildApplication(context);
		ITemplateResolver resolver = buildTemplateResolver(application);
		ENGINE.addTemplateResolver(resolver);
	}
	
//	public static ThymeleafUtil getInstance() {
//		return INSTANCE;
//	}
	
	public TemplateEngine getTemplateEngine() {
		return ENGINE;
	}
	
	public static WebContext buildWebContext(HttpServletRequest request, HttpServletResponse response) {
		ServletContext context = request.getServletContext();
		JakartaServletWebApplication application = JakartaServletWebApplication.buildApplication(context);
		IServletWebExchange webExchange = application.buildExchange(request, response);
		return new WebContext(webExchange);
	}
	

	private static ITemplateResolver buildTemplateResolver(IWebApplication application) {
		WebApplicationTemplateResolver templateResolver = new WebApplicationTemplateResolver(application);
		templateResolver.setTemplateMode(TemplateMode.HTML);
		templateResolver.setCharacterEncoding(StandardCharsets.UTF_8.name());
		templateResolver.setSuffix(".html");
		templateResolver.setCacheTTLMs(0L);
		templateResolver.setPrefix("WEB-INF/templates/");
		
		return templateResolver;
	}
}
