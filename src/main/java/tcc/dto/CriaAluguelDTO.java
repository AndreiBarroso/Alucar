package tcc.dto;

import java.util.Date;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import javax.validation.constraints.Size;

@Data
public class CriaAluguelDTO {

    @Size(max = 11, message = "Limite de caracteres permitidos 11")
    private String clienteCpf;
    private String placaVeiculo;    
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date dataDevolucaoEsperada;
    private float precoPago;

}
