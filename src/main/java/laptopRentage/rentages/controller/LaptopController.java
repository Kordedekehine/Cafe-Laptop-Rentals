package laptopRentage.rentages.controller;

//import io.swagger.annotations.Api;
import laptopRentage.rentages.exception.LaptopException;
import laptopRentage.rentages.model.CalcLaptopRentDto;
import laptopRentage.rentages.model.LaptopDto;
import laptopRentage.rentages.model.LaptopPrice;
import laptopRentage.rentages.model.LaptopUsernameAndPrice;
import laptopRentage.rentages.service.LaptopServices;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.EntityModel;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@Slf4j
@RestController
@RequestMapping("/rentage")
//@Api(value = "Korede Rental Laptops Cafe " ,  description= "Laptop that offer free coffee and rents Laptops out to programmers")
public class LaptopController {

    @Autowired
   LaptopServices laptopServices;


   @PostMapping("/add")
    public ResponseEntity<?> add(@RequestBody LaptopDto laptopDto){
       LaptopDto laptopDto1;
        try {
            laptopDto1 = laptopServices.addLaptop(laptopDto);
        }
        catch (LaptopException laptopException){
            return new ResponseEntity<>(laptopException.getMessage(),HttpStatus.BAD_REQUEST);
        }
       return new ResponseEntity<>(laptopDto1, HttpStatus.OK);
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

    @GetMapping("/findLaptopByBrand/{id}")
    public ResponseEntity<?> laptopWithPrice(@PathVariable("id")String id) {
        LaptopPrice laptopPrice;

        try{
            laptopPrice=laptopServices.findLaptopByBrand(id);
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
        LaptopUsernameAndPrice laptopSpecificity;
        try{
            laptopSpecificity = laptopServices.calculateLaptopPrice(calcLaptopRentDto);
        }
        catch (LaptopException laptopException){
            return new ResponseEntity<>(laptopException.getMessage(),HttpStatus.BAD_REQUEST);
        }
        return new ResponseEntity<>(laptopSpecificity,HttpStatus.OK);
    }
}
