package tcc.repository;

import java.util.*;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import tcc.domain.Aluguel;
import tcc.domain.Funcionario;
import tcc.domain.Veiculo;



@Repository
public interface AluguelRepositorio extends CrudRepository<Aluguel, Integer> {

    Iterable<Aluguel> findAllByConcluidoFalse();

    Optional<Aluguel> findByCodigoAndConcluidoFalse(int codigo);
    Optional<Aluguel> findByCodigo(int codigo);

    List<Aluguel> findAll();

    Long countByFuncionario(Funcionario funcionario);

    @Query("SELECT al FROM Aluguel al WHERE al.dataDevolucaoReal IS NULL")
    List<Aluguel> listaAndamento ();

    @Query("SELECT al FROM Aluguel al " +
            "WHERE al.veiculo=?1 " +
            "AND al.cancelado = false " +
            "AND al.dataAluguel<=?3 " +
            "AND (" +
            "(al.dataDevolucaoReal IS NULL AND al.dataDevolucaoEsperada>=?2 )" +
            "OR (al.dataDevolucaoReal IS NOT NULL AND al.dataDevolucaoReal>=?2)" +
            ")")
    List<Aluguel> listaPorVeiculo (Veiculo veiculo, Date dataInicial, Date dataFinal);

    @Query("SELECT al FROM Aluguel al WHERE al.dataDevolucaoReal IS NOT NULL")
    List<Aluguel> listaConcluido ();

    @Query("SELECT al FROM Aluguel al WHERE al.dataDevolucaoEsperada <?1 ")
    List<Aluguel> listaAtrasado (Date agora);


    @Query(value="SELECT " +
            "extract(year from  data_aluguel ) AS Ano, " +
            "SUM (preco_final + preco_multa) AS Total " +
            "FROM PUBLIC.aluguel " +
            "GROUP BY Ano " +
            "ORDER BY Ano ",nativeQuery = true)
    Object[][] reportTotalPorAno ();

}

