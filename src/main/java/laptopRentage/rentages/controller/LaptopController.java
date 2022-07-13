package laptopRentage.rentages.controller;

import laptopRentage.rentages.exception.LaptopException;
import laptopRentage.rentages.model.CalcLaptopRentDto;
import laptopRentage.rentages.model.LaptopDto;
import laptopRentage.rentages.model.LaptopPrice;
import laptopRentage.rentages.model.LaptopSpecificity;
import laptopRentage.rentages.service.LaptopServices;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.EntityModel;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

//@Slf4j
@RestController
@RequestMapping("/laptops")
public class LaptopController {

    @Autowired
   LaptopServices laptopServices;

   @PostMapping("/add")
    public ResponseEntity<?> add(@RequestBody LaptopDto laptopDto){
        try {
            return new ResponseEntity<>(laptopServices.addLaptop(laptopDto), HttpStatus.OK);
        }catch (LaptopException laptopException){
            return new ResponseEntity<>(laptopException.getMessage(),HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping("/listAll")
    public ResponseEntity<?> findAllLaptops(){
        List<LaptopDto> laptop = laptopServices.findAllLaptops().stream().map(
                laptopDto -> laptopDto.add(linkTo(methodOn(LaptopController.class).
                        laptopWithPrice(laptopDto.getLaptopCores())).withRel("Check Laptop info"),
                        linkTo(methodOn(LaptopController.class).findAllLaptops()).withSelfRel())).
                collect(Collectors.toList());
        return new ResponseEntity<>(laptop,HttpStatus.OK);
    }

    @GetMapping("/findLaptopById/{id}")
    public ResponseEntity<?> laptopWithPrice(@PathVariable("id")String id) {
        LaptopPrice laptopPrice;

        try{
            laptopPrice=laptopServices.findLaptopByCore(id);
            EntityModel<LaptopPrice> laptopDtoEntity=EntityModel.of(
                    laptopPrice,linkTo(methodOn(LaptopController.class).findAllLaptops()
                            ).withRel("All Laptops"));
            return new ResponseEntity<>(laptopDtoEntity, HttpStatus.OK);
        }
        catch (LaptopException laptopException){
            return new ResponseEntity<>(laptopException.getMessage(),HttpStatus.BAD_REQUEST);

        }
    }

    @PostMapping("/rent")
    public ResponseEntity<?> rentLaptop(@RequestBody CalcLaptopRentDto calcLaptopRentDto){
        LaptopSpecificity laptopSpecificity;
        try{
            laptopSpecificity = laptopServices.calculateLaptopPrice(calcLaptopRentDto);
        }
        catch (LaptopException laptopException){
            return new ResponseEntity<>(laptopException.getMessage(),HttpStatus.BAD_REQUEST);
        }
        return new ResponseEntity<>(laptopSpecificity,HttpStatus.OK);
    }
}
