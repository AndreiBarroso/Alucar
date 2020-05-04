package tcc.service;

import org.springframework.stereotype.Service;
import tcc.domain.CategoriaVeiculo;
import tcc.repository.CategoriaVeiculoRepositorio;
import tcc.service.CategoriaVeiculoService;

import javax.transaction.Transactional;
import java.util.*;

@Service
@Transactional
public class CategoriaVeiculoServiceImpl implements CategoriaVeiculoService {

    private static final String[] fields = {"Codigo", "Preço Diária", "Percentual Multa Diária"};

    private final CategoriaVeiculoRepositorio categoriaVeiculoRepositorio;

    public CategoriaVeiculoServiceImpl(CategoriaVeiculoRepositorio categoriaVeiculoRepositorio) {
        this.categoriaVeiculoRepositorio = categoriaVeiculoRepositorio;
    }

    @Override
    public CategoriaVeiculo save(CategoriaVeiculo categoriaVeiculo) {
        return categoriaVeiculoRepositorio.save(categoriaVeiculo);
    }

    @Override
    public CategoriaVeiculo update(CategoriaVeiculo categoriaVeiculo) {
        return categoriaVeiculoRepositorio.save(categoriaVeiculo);
    }

    @Override
    public Optional<CategoriaVeiculo> find(Character codigo) {
        return categoriaVeiculoRepositorio.findById(codigo);
    }

    @Override
    public Iterable<CategoriaVeiculo> findAll() {
        return categoriaVeiculoRepositorio.findAll();
    }

    @Override
    public Iterable<CategoriaVeiculo> findAllFiltradoPorPreco(float precoDiariaMaiorQue, float precoDiariaMenorQue) {
        return categoriaVeiculoRepositorio.findAllFiltradoPorPreco(precoDiariaMaiorQue, precoDiariaMenorQue);
    }

    @Override
    public void delete(Character codigo) {
        categoriaVeiculoRepositorio.deleteById(codigo);
    }

}
