package kettlebell.weather.servlet;

import jakarta.servlet.ServletContextEvent;
import jakarta.servlet.ServletContextListener;
import jakarta.servlet.annotation.WebListener;
import kettlebell.weather.service.LoadingBasicDataService;
import kettlebell.weather.service.SeanceService;
import kettlebell.weather.util.ScheduledExecutorServiceUtil;
import kettlebell.weather.util.ThymeleafUtil;

@WebListener
public class ApplicationListener implements ServletContextListener {

   public void contextInitialized(ServletContextEvent sce)  { 
   	ThymeleafUtil.INSTANCE.buildTemplateEngineInstance(sce.getServletContext());
	   SeanceService.getInstance().deleteExpiredSeances();
	   LoadingBasicDataService.getInstance().loadingBasicData();
   }

    public void contextDestroyed(ServletContextEvent sce)  {
   	 ScheduledExecutorServiceUtil.closeScheduled();
    }
	
}
