package pl.poznan.put.planner_endpoints;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * Application class
 */
@SpringBootApplication(scanBasePackages = "pl.poznan.put")
public class PlannerEndpointsApplication {

	/**
	 * Runs application
	 * @param args arguments
	 */
	public static void main(String[] args) {
		SpringApplication.run(PlannerEndpointsApplication.class, args);
	}


}

