package laptopRentage.rentages.userDto;

import laptopRentage.rentages.constant.OtpContext;
import lombok.Builder;
import lombok.Data;
import lombok.extern.jackson.Jacksonized;

@Data
@Builder
@Jacksonized
public class OtpVerificationDto {

   private String email;

   private Integer oneTimePassword; //otp

  private OtpContext otpContext;
}
