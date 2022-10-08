package tacos.web;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {

	@Override
	public void addViewControllers(ViewControllerRegistry registry) {

		// ViewControllerRegistry is used to register one or more view controllers
		// setViewName() set “home” as the view (home.html) that a request for “/” should be forwarded to
		registry.addViewController("/").setViewName("home");
	}

}
