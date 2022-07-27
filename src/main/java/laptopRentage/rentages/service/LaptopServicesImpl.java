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
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;


@Service
@Slf4j
public class LaptopServicesImpl implements LaptopServices {

    @Autowired
    LaptopRepository laptopRepository;

    @Autowired
    ModelMapper modelMapper = new ModelMapper() ;



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
        if (laptopDto.getLaptopType()==null||laptopDto.getLaptopType().toString().equals("")){
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
        laptop.setLaptopCores(laptopCoreCheck(laptopDto.getLaptopCores()));
       // laptop.setLaptopCores(laptopCoreCheck(laptop.getLaptopCores()));
        laptop.setLaptopColor(laptopColorCheck(laptopDto.getLaptopColor()));
        laptop.setLaptopType(laptopDto.getLaptopType());
        laptop.setLaptopPrice(setLaptopPrice(laptop.getLaptopType()));
        laptop.setLaptopBrand(laptopDto.getLaptopBrand());
        laptopRepository.save(laptop);
        modelMapper.map(laptop,laptopDto);
        return laptopDto;
    }

    private Double setLaptopPrice(String laptopType){
        laptopType = laptopType.toLowerCase();
        String [] types = laptopType.split(":");
        switch (types[0]){
            case "better condition": return 500.00;
            case "good condition":return 700.00;
            case "perfect condition":return 1000.00; //quite a blocker
        }
        return null;
    }

    @Override
    public LaptopUsernameAndPrice calculateLaptopPrice(CalcLaptopRentDto calcLaptopRentDto) throws LaptopException {
        if (calcLaptopRentDto.getHours() < 1){
            throw new LaptopException("Hours spent is invalid");
        }
        if (calcLaptopRentDto.getUsername()==null|| calcLaptopRentDto.getUsername().isEmpty() ||
        calcLaptopRentDto.getUsername().isBlank()){
           throw new LaptopException("Username cannot be empty");
        }
        if (calcLaptopRentDto.getLaptopBrand()==null|| calcLaptopRentDto.getLaptopBrand().isEmpty() ||
                calcLaptopRentDto.getLaptopBrand().isBlank()){
            throw new LaptopException("Username cannot be empty");
        }
        Pageable firstPageWithTwoElements = PageRequest.of(0, findAllLaptops().size());
        Page<Laptop> laptopsList = laptopRepository.findAll(firstPageWithTwoElements);
//        Laptop laptop = laptopsList.stream().filter(laptops -> laptops.getLaptopCores()
//                .equals(calcLaptopRentDto.getLaptopSelectedCores())).findFirst().orElseThrow(
        /**
         * let's use the laptop brand as the search value for now,till we arrive at a reasonable flow
         */
        // TODO: 7/27/2022  
        Laptop laptop = laptopsList.stream().filter(laptops -> laptops.getLaptopBrand().
                equals(calcLaptopRentDto.getLaptopBrand())).findFirst().orElseThrow(
                () -> new LaptopException("Laptop with the core does not exist"));
                Integer integer = laptopTypeCheck(laptop.getLaptopType());
       LaptopUsernameAndPrice laptopSpecificity = new LaptopUsernameAndPrice();
       modelMapper.map(laptop,laptopSpecificity);
       laptopSpecificity.setUserName(calcLaptopRentDto.getUsername());
       laptopSpecificity.setHours(calcLaptopRentDto.getHours());
      // laptopSpecificity.setLaptopSelected(laptopSpecificity.getLaptopSelected());
        laptopSpecificity.setLaptopBrandSelected(calcLaptopRentDto.getLaptopBrand());
       laptopSpecificity.setPriceOfLaptopUsage(calculateLaptopPriceBasedOnLaptopType(integer,laptop.getLaptopType(),calcLaptopRentDto.getHours()));

       return laptopSpecificity;
    }

    @Override
    public LaptopPrice findLaptopByBrand(String laptopBrand) throws LaptopException {
        Pageable firstPageWithTwoElements = PageRequest.of(0, findAllLaptops().size());
        Page<Laptop> laptopList = laptopRepository.findAll(firstPageWithTwoElements);
        Laptop laptopFound =laptopList.stream().filter(laptop -> laptop.getLaptopBrand().equalsIgnoreCase(laptopBrand)).
                findFirst().orElseThrow(() -> new LaptopException("Laptop with this Brand tag does not exist"));
        LaptopPrice laptopDto = new LaptopPrice();

        modelMapper.map(laptopFound,laptopDto);
        return laptopDto;
    }


//    @Override
//    public LaptopPrice findLaptopByCore(String laptopCore) throws LaptopException {
//        Pageable firstPageWithTwoElements = PageRequest.of(0, findAllLaptops().size());
//        Page<Laptop> laptopList = laptopRepository.findAll(firstPageWithTwoElements);
//        Laptop laptopFound =laptopList.stream().filter(laptop -> laptop.getLaptopCores().equals(laptopCore)).
//                findFirst().orElseThrow(() -> new LaptopException("Laptop with this core does not exist"));
//        LaptopPrice laptopDto = new LaptopPrice();
//
//        modelMapper.map(laptopFound,laptopDto);
//        return laptopDto;
//    }

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

//    @Override
//    public LaptopPrice findLaptopByCores(String laptopCore) throws LaptopException {
//     Pageable firstPageWithTwoElements = PageRequest.of(0, findAllLaptops().size());
//     Page<Laptop> laptopList = laptopRepository.findAll(firstPageWithTwoElements);
//     Laptop laptopFound =laptopList.stream().filter(laptop -> laptop.getLaptopCores().equalsIgnoreCase(laptopCore)).
//             findFirst().orElseThrow(() -> new LaptopException("Laptop with this core does not exist"));
//     LaptopPrice laptopDto = new LaptopPrice();
//
//     modelMapper.map(laptopFound,laptopDto);
//     return laptopDto;
//    }

    private boolean notAType(String laptopType){
        String type = laptopType;
        String check = type;
        if (laptopType.contains(":")){
            String [] types = type.split(":");
            check=types[0].trim();
        }
       if (check.equalsIgnoreCase("Perfect Condition")|| check.equalsIgnoreCase("Better Condition")||
       check.equalsIgnoreCase("Good Condition")){
         return false;
       }
       return true;
    }

    private Color laptopColorCheck(String laptopColor){
        if (laptopColor.equalsIgnoreCase("White")){
            return Color.white;
        }
        if (laptopColor.equalsIgnoreCase("Black")){
            return Color.black;
        }
        if (laptopColor.equalsIgnoreCase("Ash")){
            return Color.ash;
        }
        return null;
    }

    private Cores laptopCoreCheck(String laptopCore){
        if (laptopCore.equalsIgnoreCase("macAir")){
            return Cores.macAir;
        }
        if (laptopCore.equalsIgnoreCase("MacPro")){
            return Cores.macBookPro;
        }
        if (laptopCore.equalsIgnoreCase("corei5")){
            return Cores.corei5;
        }
        if (laptopCore.equalsIgnoreCase("corei6")){
            return Cores.corei6;
        }
        if (laptopCore.equalsIgnoreCase("Corei7")){
            return Cores.corei7;
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
