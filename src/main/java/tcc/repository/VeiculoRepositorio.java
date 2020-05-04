
package tcc.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import tcc.domain.Veiculo;

import java.util.Set;

@Repository
public interface VeiculoRepositorio extends CrudRepository<Veiculo, String> {

//    Iterable<Veiculo> findAllByDisponivelTrue();

    Set<Veiculo> findAll();

}
