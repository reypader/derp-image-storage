package com.rmpader.derp.imagehost.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

import com.rmpader.derp.imagehost.property.ImageHostProperty;

@Configuration
public class WebConfig extends WebMvcConfigurerAdapter {

    @Autowired
    private ImageHostProperty   imageHostProperties;

    private static final String resourceBaseURL = "/img/";

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler(resourceBaseURL + "**").addResourceLocations(
                "file:" + imageHostProperties.getUploadLocation());
    }

    public static String getResourceBaseURL() {
        return resourceBaseURL;
    }

}
