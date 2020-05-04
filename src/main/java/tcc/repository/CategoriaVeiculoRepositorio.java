
package tcc.repository;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import tcc.domain.CategoriaVeiculo;

@Repository
public interface CategoriaVeiculoRepositorio extends CrudRepository<CategoriaVeiculo, Character> {

    @Query("FROM CategoriaVeiculo WHERE preco_diaria > :precoDiariaMaiorQue AND preco_diaria < :precoDiariaMenorQue")
    Iterable<CategoriaVeiculo> findAllFiltradoPorPreco(float precoDiariaMaiorQue, float precoDiariaMenorQue);

}
