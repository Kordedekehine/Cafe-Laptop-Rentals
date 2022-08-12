package laptopRentage.rentages.mail.properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Data
@Configuration
@ConfigurationProperties(prefix = "spring.mail")
public class EmailConfigurationProperties {

	private String username;
	private String password;

}