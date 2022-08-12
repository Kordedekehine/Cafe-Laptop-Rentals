package laptopRentage.rentages.userDto;

import lombok.Builder;
import lombok.Data;
import lombok.extern.jackson.Jacksonized;

@Data
@Builder
@Jacksonized
public class UserLoginRequestDto {

    private String email;

    private String password;
}
