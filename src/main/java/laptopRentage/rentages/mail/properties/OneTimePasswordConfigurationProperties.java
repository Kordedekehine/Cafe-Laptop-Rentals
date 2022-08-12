package laptopRentage.rentages.mail.properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

@Data
@ConfigurationProperties(prefix = "laptoprentage.rentages")
public class OneTimePasswordConfigurationProperties {

    private OTP otp = new OTP();

    @Data
    public class OTP {
        private Integer expirationMinutes;
    }

}