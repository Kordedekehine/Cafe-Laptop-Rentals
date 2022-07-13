package laptopRentage.rentages;

import org.modelmapper.ModelMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import springfox.documentation.spring.data.rest.configuration.SpringDataRestConfiguration;

//model mapper blocker ..FIXED-> annotated the service with component and add scanBasePackages to the main
@Configuration
@Import(SpringDataRestConfiguration.class)
public class ModelMapperConfiguration {

    @Bean
    public ModelMapper modelMapper(){
        return new ModelMapper();
    }
}
