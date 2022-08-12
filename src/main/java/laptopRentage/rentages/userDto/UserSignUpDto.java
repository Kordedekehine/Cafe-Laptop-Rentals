package laptopRentage.rentages.userDto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.extern.jackson.Jacksonized;

import javax.persistence.Column;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;

@Builder
@Jacksonized
@Data
public class UserSignUpDto {

    private String firstname;

    private String lastname;

    private String username;

    private String phoneNumber;

    @Email
    @NotBlank
    private String email;

    @NotBlank
    private String password;

    private String confirmPassword;
}
