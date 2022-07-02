package laptopRentage.rentages.service;

import laptopRentage.rentages.exception.LaptopException;
import laptopRentage.rentages.model.*;
import laptopRentage.rentages.repository.LaptopRepository;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;


@Service
@Slf4j
public class LaptopServicesImpl implements LaptopServices {

    LaptopRepository laptopRepository;

    ModelMapper modelMapper =new ModelMapper();

    @Autowired
    public LaptopServicesImpl(LaptopRepository laptopRepository, ModelMapper modelMapper) {
        this.laptopRepository = laptopRepository;
        this.modelMapper = modelMapper;
    }

    @Override
    public List<LaptopDto> findAllLaptops() {
        Pageable firstPageWithTwoElements = PageRequest.of(0,10);
        Page<Laptop> laptopList = laptopRepository.findAll(firstPageWithTwoElements);
        List<LaptopDto> laptopDtoList =laptopList.stream().map(laptop -> modelMapper.map(
                laptop,LaptopDto.class)).collect(Collectors.toList());
        //blocker here,tried to map it with Laptop.class..error..MY Bad! we're listing the dtos not entity
        //that's too expensive and we would't want to expose our entities
       return laptopDtoList;
    }

    @Override
    public LaptopDto addLaptop(LaptopDto laptopDto) throws LaptopException {

        if (laptopDto.getLaptopBrand()==null||laptopDto.getLaptopBrand().toString().equals("")){
            throw new LaptopException("Laptop Brand cannot be null");
        }
        if (laptopDto.getLaptopType().isEmpty()||laptopDto.getLaptopType().isBlank()||laptopDto.getLaptopType()==null){
            throw new LaptopException("Laptop Type cannot be null");
        }
        if (laptopDto.getLaptopColor()==null||laptopDto.getLaptopColor().toString().equals("")){
            throw new LaptopException("Laptop Color cannot be null");
        }
        if (laptopDto.getLaptopCores().isBlank() || laptopDto.getLaptopCores() == null){
            throw new LaptopException("Laptop core cannot be null");
        }
        if (notAType(laptopDto.getLaptopType())){
            throw new LaptopException("Laptop type is invalid");
        }
        Laptop laptop = new Laptop();
        laptop.setLaptopBrand(laptopBrandCheck(laptop.getLaptopCores()));
        laptop.setLaptopColor(laptopDto.getLaptopColor());
        laptop.setLaptopType(laptop.getLaptopType());
        laptop.setLaptopPrice(laptop.getLaptopPrice());
        laptop.setLaptopCores(laptop.getLaptopCores());
        laptopRepository.save(laptop);
        return laptopDto;
    }

    private Double setLaptopPrice(String laptopType){
        laptopType = laptopType.toLowerCase();
        String [] types = laptopType.split(":");
        switch (types[0]){
            case "Better Condition": return 500.00;
            case "Good Condition":return 700.00;
            case "Perfect Condition":return 1000.00;
        }
        return null;
    }


    @Override
    public LaptopSpecificity calculateLaptopPrice(CalcLaptopRentDto calcLaptopRentDto) throws LaptopException {
        if (calcLaptopRentDto.getHours() < 1){
            throw new LaptopException("Hours spent is invalid");
        }
        if (calcLaptopRentDto.getUsername()==null|| calcLaptopRentDto.getUsername().isEmpty() ||
        calcLaptopRentDto.getUsername().isBlank()){
           throw new LaptopException("Username cannot be empty");
        }
        Pageable firstPageWithTwoElements = PageRequest.of(0, findAllLaptops().size());
        Page<Laptop> laptopsList = laptopRepository.findAll(firstPageWithTwoElements);
        Laptop laptop = laptopsList.stream().filter(laptops -> laptops.getLaptopCores().
                equalsIgnoreCase(calcLaptopRentDto.getLaptopSelectedCores())).findFirst().orElseThrow(
                () -> new LaptopException("Laptop with the core does not exist"));
                Integer integer = laptopTypeCheck(laptop.getLaptopType());
       LaptopSpecificity laptopSpecificity = new LaptopSpecificity();
       modelMapper.map(laptop,laptopSpecificity);
       laptopSpecificity.setUserName(calcLaptopRentDto.getUsername());
       laptopSpecificity.setHours(calcLaptopRentDto.getHours());
       laptopSpecificity.setLaptopSelected(laptopSpecificity.getLaptopSelected());
       laptopSpecificity.setPriceOfLaptopUsage(calculateLaptopPriceBasedOnLaptopType(integer,laptop.getLaptopType(),calcLaptopRentDto.getHours()));

       return laptopSpecificity;
    }

   private Integer laptopTypeCheck(String laptopType){
        String type = laptopType;
        String check = type;
        String type2 = " ";
        if (!type.equalsIgnoreCase("Better condition")){
            String[] types = type.split(":");
            type2=types[1];
            check=types[0];
        }
  switch (check){
      case "Good Condition":return 0;
      case "Better Condition":
      case "Perfect Condition":
          return Integer.valueOf(type2.trim());
  }
        return null;
   }

    @Override
    public LaptopPrice findLaptopByCores(String laptopCore) throws LaptopException {
     Pageable firstPageWithTwoElements = PageRequest.of(0, findAllLaptops().size());
     Page<Laptop> laptopList = laptopRepository.findAll(firstPageWithTwoElements);
     Laptop laptopFound =laptopList.stream().filter(laptop -> laptop.getLaptopCores().equalsIgnoreCase(laptopCore)).
             findFirst().orElseThrow(() -> new LaptopException("Laptop with this core does not exist"));
     LaptopPrice laptopDto = new LaptopPrice();

     modelMapper.map(laptopFound,laptopDto);
     return laptopDto;
    }

    private boolean notAType(String laptopType){
        String type = laptopType;
        String check = type;
        if (laptopType.contains(":")){
            String [] types = type.split(":");
            check=types[0].trim();
        }
       if (check.equalsIgnoreCase("Perfect")|| check.equalsIgnoreCase("MacBook")||
       check.equalsIgnoreCase("Mac M1 Air")){
         return false;
       }
       return true;
    }

    private Brand laptopBrandCheck(String laptopBrand){
        if (laptopBrand.equalsIgnoreCase("MacBook")){
            return Brand.MacBook;
        }
        if (laptopBrand.equalsIgnoreCase("Dell")){
            return Brand.Dell;
        }
        if (laptopBrand.equalsIgnoreCase("HP")){
            return Brand.HP;
        }
        if (laptopBrand.equalsIgnoreCase("Asus")){
            return Brand.Asus;
        }
        if (laptopBrand.equalsIgnoreCase("Toshiba")){
            return Brand.Toshiba;
        }
        if (laptopBrand.equalsIgnoreCase("Lenovo")){
            return Brand.Lenovo;
        }
        return null;
    }

    private Double calculateLaptopPriceBasedOnLaptopType(Integer integer, String laptopType,Integer hoursSpent) {
        log.info("Calculator-->{}{}",laptopType,integer);
        laptopType=laptopType.toLowerCase();
        String[] types=laptopType.split(":");
        log.info("Calculator2-->{}{}",laptopType,integer);
        switch (types[0]){
            case "Good Condition":
                return 500.00*hoursSpent;
            case "Better Condition":
                return 700.00*hoursSpent+(integer/2.0);
            case "Perfect Condition":
                return 1000.00*hoursSpent-(integer);
        }
        return null;
    }

}
