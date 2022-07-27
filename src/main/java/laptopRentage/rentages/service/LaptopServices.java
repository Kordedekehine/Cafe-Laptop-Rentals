package laptopRentage.rentages.service;

import laptopRentage.rentages.exception.LaptopException;
import laptopRentage.rentages.model.CalcLaptopRentDto;
import laptopRentage.rentages.model.LaptopDto;
import laptopRentage.rentages.model.LaptopPrice;
import laptopRentage.rentages.model.LaptopUsernameAndPrice;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public interface LaptopServices {

    List<LaptopDto> findAllLaptops();
    LaptopDto addLaptop(LaptopDto laptopDto) throws LaptopException;
    LaptopUsernameAndPrice calculateLaptopPrice(CalcLaptopRentDto calcLaptopRentDto) throws LaptopException;
    LaptopPrice findLaptopByBrand(String laptopBrand) throws LaptopException;

    //LaptopPrice findLaptopByCore(String laptopCore) throws LaptopException;
}
