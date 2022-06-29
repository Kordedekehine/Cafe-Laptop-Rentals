package laptopRentage.rentages.model;

import lombok.Data;
import org.springframework.hateoas.RepresentationModel;

@Data
public class LaptopPrice extends RepresentationModel<LaptopPrice> {
    private String laptopCore_Gen;
    private String laptopColor;
    private String laptopType;
    private String laptopBrand;
    private Double laptopPrice;
}
