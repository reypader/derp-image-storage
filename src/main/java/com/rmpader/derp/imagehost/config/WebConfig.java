package com.rmpader.derp.imagehost.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

@Configuration
public class WebConfig extends WebMvcConfigurerAdapter {

	private static String resourceBaseURL = "/img/";

	@Override
	public void addResourceHandlers(ResourceHandlerRegistry registry) {
		registry.addResourceHandler(resourceBaseURL + "**")
				.addResourceLocations("file:/home/reynald/public/");
	}

	public static String getResourceBaseURL() {
		return resourceBaseURL;
	}

}
