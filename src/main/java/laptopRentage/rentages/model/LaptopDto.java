package laptopRentage.rentages.model;

import lombok.Data;
import org.springframework.hateoas.RepresentationModel;

/**
 *Representational model is the  Base class for DTOs to collect links.
 * The core problem it tries to address is link creation and representation assembly.
 */

//RepresentationModel – is a container for a collection of Links and provides APIs to add those links to the model.
//EntityModel – represents RepresentationModel containing only single entity and related links
@Data
public class LaptopDto extends RepresentationModel<LaptopDto> {

    private String laptopCores; //core 5 to 7
    private String laptopType; //6th gen to 8th gen
    private String laptopColor; //white -> black -> Ash
    private String laptopBrand; //we have a specifics for that
}
