package laptopRentage.rentages.security;


import lombok.experimental.UtilityClass;
import org.springframework.security.core.userdetails.User;

import java.util.List;

@UtilityClass
public class SecurityUtils {

    public User Convert(laptopRentage.rentages.model.User user){
        return new User(user.getEmail(), user.getPassword(), List.of());
    }


}
