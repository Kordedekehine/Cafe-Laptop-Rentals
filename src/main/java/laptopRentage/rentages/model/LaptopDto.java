package laptopRentage.rentages.model;

import lombok.Data;
import org.springframework.hateoas.RepresentationModel;

/**
 *Representational model is the  Base class for DTOs to collect links.
 * The core problem it tries to address is link creation and representation assembly.
 */
@Data
public class LaptopDto extends RepresentationModel<LaptopDto> {

    private String laptopCores;
    private String laptopType;
    private String laptopColor;
    private String laptopBrand;
}
