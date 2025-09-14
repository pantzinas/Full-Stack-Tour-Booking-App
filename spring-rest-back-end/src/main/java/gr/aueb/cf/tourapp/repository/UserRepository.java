package gr.aueb.cf.tourapp.repository;

import gr.aueb.cf.tourapp.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long>, JpaSpecificationExecutor<User> {

    Optional<User> findByVat(String vat);
    Optional<User> findByUsername(String username);

    Optional<User> findByLastname(String lastname);

    Optional<User> findByRole(String role);
}
