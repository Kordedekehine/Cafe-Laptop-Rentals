package laptopRentage.rentages.model;

import lombok.Data;

@Data
public class LaptopUsernameAndPrice {

    private String userName;
    private String laptopBrandSelected;
    private Integer hours;
    private Double priceOfLaptopUsage;
    private String laptopType;
}
