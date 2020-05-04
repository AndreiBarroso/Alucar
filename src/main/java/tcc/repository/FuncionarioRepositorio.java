package tcc.repository;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import tcc.domain.Funcionario;

import java.util.Set;

@Repository
public interface FuncionarioRepositorio extends CrudRepository<Funcionario, String> {

    @Query("FROM Funcionario WHERE login = ?1")
    Funcionario retornaFuncionario(String login);

    Set<Funcionario> findAll();

}
