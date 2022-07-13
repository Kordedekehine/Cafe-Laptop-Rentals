package laptopRentage.rentages;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ComponentScan(basePackages = {"laptopRentage.rentages.LaptoprentagesApplication",
		"laptopRentage.rentages.controller","laptopRentage.rentages.service"})
public class LaptoprentagesApplication {

	public static void main(String[] args) {

		SpringApplication.run(LaptoprentagesApplication.class, args);
		System.out.println("Finished running Laptop rentals application");
	}

}
