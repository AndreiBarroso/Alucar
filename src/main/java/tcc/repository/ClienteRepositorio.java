
package tcc.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import tcc.domain.Cliente;

@Repository
public interface ClienteRepositorio extends JpaRepository<Cliente, String> {
    
}
