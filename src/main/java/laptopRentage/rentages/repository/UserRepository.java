package laptopRentage.rentages.repository;

import laptopRentage.rentages.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


import java.util.Optional;
import java.util.UUID;

@Repository
public interface UserRepository extends JpaRepository<User, UUID> {


    boolean existsByEmail(String email);
    Optional<User> findUserByEmail(String email);

    Optional<User> findById(int userId);

    Optional<User> findByUsername(int username);
}
