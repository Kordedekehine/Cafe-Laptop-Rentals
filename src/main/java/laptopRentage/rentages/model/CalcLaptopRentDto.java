package laptopRentage.rentages.model;

import lombok.Data;

@Data
public class CalcLaptopRentDto {
    private String username;
    private String laptopSelectedCores;
    private String laptopBrand;
    private Integer hours;
}
