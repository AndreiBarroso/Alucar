package tcc.service;

import com.itextpdf.text.BaseColor;
import com.itextpdf.text.DocumentException;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import tcc.domain.Veiculo;
import tcc.repository.VeiculoRepositorio;

import javax.transaction.Transactional;
import java.io.ByteArrayInputStream;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Set;
import java.util.concurrent.atomic.AtomicReference;

@Service
@Transactional
@AllArgsConstructor
public class VeiculoServiceImpl implements VeiculoService {

    private final VeiculoRepositorio veiculoRepositorio;
    private final ReportService reportService;
    private final  AluguelService aluguelService;

    @Override
    public ByteArrayInputStream getReport() throws DocumentException {
        String[] fields = {"Placa", "Marca", "Modelo", "Ano", "Cor", "Quilometragem", "Observação", "Categoria", "Disponibilidade"};
        return reportService.createPDFReport(fillReportRows(), "Veículos", fields, BaseColor.LIGHT_GRAY);
    }

//    @Override
//    public Veiculo desativaVeiculoAoAlugar(Veiculo veiculo) {
//        veiculo.setDisponivel(false);
//        return veiculoRepositorio.save(veiculo);
//    }
//
//    @Override
//    public Veiculo ativaVeiculo(Veiculo veiculo) {
//        veiculo.setDisponivel(true);
//        return veiculoRepositorio.save(veiculo);
//    }

    private List<String> fillReportRows() {
        Set<Veiculo> result = veiculoRepositorio.findAll();
        List<String> cellsValues = new ArrayList<>();
        Date agora= new Date();
     
        result.forEach(veiculo -> {
            boolean disponivel= aluguelService.listaPorVeiculoEDatas(veiculo, agora, agora).isEmpty();
            String disponibilidade = disponivel ? "Disponível" : "Indisponível/Alugado";

            cellsValues.add(veiculo.getPlaca());
            cellsValues.add(veiculo.getMarca());
            cellsValues.add(veiculo.getModelo());
            cellsValues.add(String.valueOf(veiculo.getAnoModelo()));
            cellsValues.add(veiculo.getCor());
            cellsValues.add(Integer.toString(veiculo.getKilometragem()) );
            cellsValues.add(veiculo.getObservacao());
            cellsValues.add(veiculo.getCategoria() != null ? String.valueOf(veiculo.getCategoria().getCodigo()) : "");
            cellsValues.add(disponibilidade);
        });
        

        return cellsValues;
    }
}
