package tcc.service;

import tcc.domain.CategoriaVeiculo;

import java.util.Optional;
import java.util.Set;

public interface CategoriaVeiculoService {

    CategoriaVeiculo save(CategoriaVeiculo categoriaVeiculo);

    CategoriaVeiculo update(CategoriaVeiculo categoriaVeiculo);

    Optional<CategoriaVeiculo> find(Character codigo);

    Iterable<CategoriaVeiculo> findAll();

    Iterable<CategoriaVeiculo> findAllFiltradoPorPreco(float precoDiariaMaiorQue, float precoDiariaMenorQue);

    void delete(Character codigo);

}
