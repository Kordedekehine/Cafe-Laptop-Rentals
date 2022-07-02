package laptopRentage.rentages.model;

import lombok.Data;

@Data
public class LaptopSpecificity {

    private String userName;
    private String laptopSelected;
    private Integer hours;
    private Double priceOfLaptopUsage;
    private String laptopType;
}
