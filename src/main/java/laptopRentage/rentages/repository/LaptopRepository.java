package laptopRentage.rentages.repository;

import laptopRentage.rentages.model.Laptop;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface LaptopRepository extends PagingAndSortingRepository<Laptop,Integer> {
    Page<Laptop> findByLaptopBrand(String laptopBrand, Pageable firstPageWithTwoElements);
}
