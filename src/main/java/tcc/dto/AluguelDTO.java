package tcc.dto;

import java.util.Date;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import javax.validation.constraints.Size;

@Data
public class AluguelDTO {


    private Integer codigo;
    @Size(max = 11, message = "Limite de caracteres permitidos 11")
    private String clienteCpf;
    private String placaVeiculo;
    @DateTimeFormat(pattern = "dd-MM-yyyy'T'HH:mm")
    private Date dataDevolucaoEsperada;
    @DateTimeFormat(pattern = "dd-MM-yyyy'T'HH:mm")
    private Date dataDevolucaoReal;
    private float precoPago;
    private float precoMultaPaga;
    private boolean desativado;

    public AluguelDTO() {
    }
}
