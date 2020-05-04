package tcc.service;

import com.itextpdf.text.BaseColor;
import com.itextpdf.text.DocumentException;
import java.io.ByteArrayInputStream;
import java.util.*;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import tcc.domain.Cliente;
import tcc.repository.ClienteRepositorio;

@Service
@AllArgsConstructor
public class ClienteServiceImpl implements ClienteService {

    private final ClienteRepositorio clienteRepository;
    private final ReportService reportService;
    private final EmailService emailService;

    @Override
    public Cliente save(Cliente cliente) {
        Cliente clienteSalvo = clienteRepository.save(cliente);
        emailService.enviarEmailCadastroCliente(clienteSalvo);
        emailService.enviarEmailDeFatura(clienteSalvo);

        return clienteSalvo;
    }

    @Override
    public Cliente update(Cliente cliente) {
        return clienteRepository.save(cliente);
    }

    @Override
    public Optional<Cliente> find(String cpf) {
        return clienteRepository.findById(cpf);
    }

    @Override
    public Set<Cliente> findAll() {
        return new HashSet<Cliente>(clienteRepository.findAll());
    }

    @Override
    public void delete(String cpf) {
        clienteRepository.deleteById(cpf);
    }

    @Override
    public ByteArrayInputStream getReport() throws DocumentException {
        String[] fields = {"CPF", "Nome", "Telefone", "E-mail"};
        return reportService.createPDFReport(fillReportRows(), "Clientes", fields, BaseColor.LIGHT_GRAY);
    }

    private List<String> fillReportRows() {
        Set<Cliente> result = findAll();
        List<String> cellsValues = new ArrayList<>();

        result.forEach(data -> {
            cellsValues.add(data.getCpf());
            cellsValues.add(data.getNome());
            cellsValues.add(data.getTelefone());
            cellsValues.add(data.getEmail());
        });

        return cellsValues;
    }

}
