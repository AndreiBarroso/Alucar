package tcc.service;


import com.itextpdf.text.DocumentException;
import tcc.domain.Cliente;

import java.io.ByteArrayInputStream;
import java.util.Optional;
import java.util.Set;

public interface ClienteService {

    Cliente save(Cliente cliente);

    Cliente update(Cliente cliente);

    Optional<Cliente> find(String cpf);

    Set<Cliente> findAll();

    void delete(String cpf);

    ByteArrayInputStream getReport() throws DocumentException;

}
