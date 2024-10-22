package com.g02.handyShare.Config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {
  @Bean
  public WebMvcConfigurer corsConfigurer() {
      return new WebMvcConfigurer() {
    @Override
    public void addCorsMappings(CorsRegistry registry) {
        // registry.addMapping("/api/v1/payment/**") // Change this path to your API endpoint
        //         .allowedOrigins("http://localhost:3000") // Allowed origin
        //         .allowedMethods("GET", "POST", "PUT", "DELETE", "OPTIONS") 
        //         .allowedHeaders("*") 
        //         .allowCredentials(true); 
        registry.addMapping("/api/**")
                .allowedOrigins("http://localhost:3000")
                .allowedMethods("GET", "POST", "PUT", "DELETE", "OPTIONS") 
                .allowedHeaders("*")
                .allowCredentials(true) 
                .exposedHeaders("Authorization") 
                .maxAge(3600); 
    }
};
  }
}

