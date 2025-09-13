package auth.repositories;

import auth.domains.models.Client;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ClientRepository extends JpaRepository<Client, Integer> {
    boolean existsByUsername(String username);
    Optional<Client> findByUsername(String username);
    void deleteByUsername(String username);
}
