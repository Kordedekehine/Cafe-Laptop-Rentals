package laptopRentage.rentages.service;

import laptopRentage.rentages.exception.LaptopException;
import laptopRentage.rentages.model.*;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import java.util.List;

@Component
public interface LaptopServices {

    List<LaptopDto> findAllLaptops();
    LaptopDto addLaptop(LaptopDto laptopDto) throws LaptopException;
    LaptopSpecificity calculateLaptopPrice(CalcLaptopRentDto calcLaptopRentDto) throws LaptopException;
    LaptopPrice findLaptopByCore(String laptopCore) throws LaptopException;

}
