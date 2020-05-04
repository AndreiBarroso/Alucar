package tcc.service;

import com.itextpdf.text.BaseColor;
import com.itextpdf.text.DocumentException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import tcc.domain.Funcionario;
import tcc.repository.FuncionarioRepositorio;

import javax.transaction.Transactional;
import java.io.ByteArrayInputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@Service
@Transactional
public class FuncionarioServiceImpl implements FuncionarioService {

    private final FuncionarioRepositorio repositorio;
    private final ReportService reportService;
    private final AluguelService aluguelService;

    @Autowired
    public FuncionarioServiceImpl(FuncionarioRepositorio repositorio, ReportService reportService, AluguelService aluguelService) {
        this.repositorio = repositorio;
        this.reportService = reportService;
        this.aluguelService = aluguelService;
    }

    public ByteArrayInputStream getReport() throws DocumentException {
        String[] fields = {"CPF", "Nome", "Quantidade de alugueis realizados"};
        return reportService.createPDFReport(fillReportRows(), "Funcionários", fields, BaseColor.LIGHT_GRAY);
    }

    private List<String> fillReportRows() {
        Set<Funcionario> result = repositorio.findAll();
        List<String> cellsValues = new ArrayList<>();

        result.forEach(data -> {
            cellsValues.add(data.getCpf());
            cellsValues.add(data.getNome());
            cellsValues.add(String.valueOf(aluguelService.getAluguelCountByFuncionario(data)));
        });

        return cellsValues;
    }
}
