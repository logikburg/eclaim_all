package hk.org.ha.eclaim.config;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.ServletRegistration;

import org.springframework.web.WebApplicationInitializer;
import org.springframework.web.context.ContextLoaderListener;
import org.springframework.web.context.support.AnnotationConfigWebApplicationContext;
import org.springframework.web.servlet.DispatcherServlet;

import hk.org.ha.eclaim.core.config.AppConfig;

public class SpringWebAppInitializer implements WebApplicationInitializer {
	
	@Override
    public void onStartup(ServletContext servletContext) throws ServletException {
		
		AnnotationConfigWebApplicationContext rootContext = new AnnotationConfigWebApplicationContext();  
        rootContext.register(AppConfig.class);  
        servletContext.addListener(new ContextLoaderListener(rootContext));  
           
        AnnotationConfigWebApplicationContext webContext = new AnnotationConfigWebApplicationContext();  
        webContext.register(WebMvcConfig.class); 
        webContext.setServletContext(servletContext);
		
        // Dispatcher Servlet
        ServletRegistration.Dynamic dispatcher = servletContext.addServlet("dispatcher", new DispatcherServlet(webContext));
        dispatcher.setLoadOnStartup(1);
        dispatcher.addMapping("/");
        
    }
	
}
