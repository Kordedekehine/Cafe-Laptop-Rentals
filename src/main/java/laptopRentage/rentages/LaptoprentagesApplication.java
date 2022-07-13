package laptopRentage.rentages;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import springfox.documentation.swagger2.annotations.EnableSwagger2;


@SpringBootApplication (scanBasePackages = "com.laptopRentage.rentages")
@EnableSwagger2
@EnableWebMvc
public class LaptoprentagesApplication extends SpringBootServletInitializer {

	public static void main(String[] args) {

		SpringApplication.run(LaptoprentagesApplication.class, args);
		System.out.println("Finished running Laptop rentals application");
	}

}
