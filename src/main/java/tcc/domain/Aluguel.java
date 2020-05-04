package tcc.domain;

import java.text.NumberFormat;
import static javax.persistence.TemporalType.TIMESTAMP;

import java.util.Date;
import javax.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;

@Entity
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Aluguel {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer codigo;

    @ManyToOne
    @JoinColumn(name = "cliente_id")
    private Cliente cliente;

    @ManyToOne
    @JoinColumn(name = "veiculo_id")
    private Veiculo veiculo;

    @ManyToOne
    @JoinColumn(name = "funcionario_id")
    private Funcionario funcionario;

    @Temporal(TIMESTAMP)
    @DateTimeFormat(pattern = "dd-MM-yyyy'T'HH:mm")
    private Date dataAluguel;

    @Temporal(TIMESTAMP)
    @DateTimeFormat(pattern = "dd-MM-yyyy'T'HH:mm")
    private Date dataDevolucaoEsperada;

    @Temporal(TIMESTAMP)
    @DateTimeFormat(pattern = "dd-MM-yyyy'T'HH:mm")
    private Date dataDevolucaoReal;
    
    private boolean cancelado;

    private boolean retirado;

    private float precoSinal;

    private float preco;

    private float desconto;

    private float precoFinal;

    private float precoMulta;

    private boolean sinalPago;

    private boolean pago;

    private boolean multaPaga;

    private boolean concluido;
    
    private int quilometragemInicial;
    
    private int quilometragemFinal;

    private float valRestituir;
    
    

    public String getPrecoFinalStr() {
        NumberFormat formatter = NumberFormat.getCurrencyInstance();
        return formatter.format(precoFinal);
    }

    public String getPrecoStr() {
        NumberFormat formatter = NumberFormat.getCurrencyInstance();
        return formatter.format(preco);
    }

    public String getDescontoStr() {
        NumberFormat formatter = NumberFormat.getPercentInstance();
        return formatter.format(desconto);
    }
    
    
    public String getPrecoMultaStr() {
        NumberFormat formatter = NumberFormat.getCurrencyInstance();
        return formatter.format(precoMulta);
    }

    public String getValRestituirStr() {
        NumberFormat formatter = NumberFormat.getCurrencyInstance();
        return formatter.format(valRestituir);
    }


//    public String getValorFinalizacao(){
//        NumberFormat formatter = NumberFormat.getCurrencyInstance();
//        return formatter.format(valRestituir - precoMulta);
//    }

    public String getPrecoSinalStr() {
        NumberFormat formatter = NumberFormat.getCurrencyInstance();
        return formatter.format(precoSinal);
    }
    public float getPrecoRestante() {
        return precoFinal - precoSinal;
    }

    public String getPrecoRestanteStr() {
        NumberFormat formatter = NumberFormat.getCurrencyInstance();
        return formatter.format(getPrecoRestante());
    }



}
