package tcc.domain;

import lombok.Data;

import java.text.NumberFormat;
import javax.persistence.Entity;
import javax.persistence.Id;

@Data
@Entity
public class CategoriaVeiculo {

    @Id
    private Character codigo;  
    private float precoDiaria;
    private float percentualMultaDiaria;
    private String itensDisponiveis;

    public char getCodigo() {
        return codigo;
    }

    public void setCodigo(char codigo) {
        this.codigo = codigo;
    }

    public float getPrecoDiaria() {
        return precoDiaria;
    }

    public String getPrecoDiariaReal() {
        NumberFormat formatter = NumberFormat.getCurrencyInstance();
        return formatter.format(precoDiaria);
    }

    public void setPrecoDiaria(float precoDiaria) {
        this.precoDiaria = precoDiaria;
    }

    public float getPercentualMultaDiaria() {
        return percentualMultaDiaria;
    }
    
     public String getPercentualMultaDiariaa() {
        NumberFormat formatter = NumberFormat.getPercentInstance();
        return formatter.format(percentualMultaDiaria);
    }
    

    public void setPercentualMultaDiaria(float percentualMultaDiaria) {
        this.percentualMultaDiaria = percentualMultaDiaria;
    }
}
