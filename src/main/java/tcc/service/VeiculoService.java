package tcc.service;

import com.itextpdf.text.DocumentException;
import tcc.domain.Veiculo;

import java.io.ByteArrayInputStream;

public interface VeiculoService {

    ByteArrayInputStream getReport() throws DocumentException;

//    Veiculo desativaVeiculoAoAlugar(Veiculo veiculo);
//
//    Veiculo ativaVeiculo(Veiculo veiculo);
}
