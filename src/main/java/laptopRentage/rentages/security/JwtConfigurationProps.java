package laptopRentage.rentages.security;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "laptoprentage.rentages")
@Data
public class JwtConfigurationProps {

    private JWT jwt = new JWT();

    @Data
    public class JWT{
        private String secretKey;
    }
}
