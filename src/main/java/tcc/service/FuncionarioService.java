package tcc.service;

import com.itextpdf.text.DocumentException;

import java.io.ByteArrayInputStream;

public interface FuncionarioService {


    ByteArrayInputStream getReport() throws DocumentException;
}
