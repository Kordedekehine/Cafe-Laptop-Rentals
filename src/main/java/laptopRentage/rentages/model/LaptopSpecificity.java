package laptopRentage.rentages.model;

import lombok.Data;

@Data
public class LaptopSpecificity {

    private String rentalName;
    private String laptopSelected;
    private Integer numberOfDays;
    private Double priceOfLaptopUsage;
    private String laptopType;
}
