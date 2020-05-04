package tcc.domain;

import java.text.NumberFormat;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class Plano {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer codigo;

    private String nome;

    private String descricao;

    private float mensalidade;

    private float percentualDescontoAluguel;

    public Integer getCodigo() {
        return codigo;
    }

    public void setCodigo(Integer codigo) {
        this.codigo = codigo;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public String getMensalidade() {
        NumberFormat formatter = NumberFormat.getCurrencyInstance();
        return formatter.format(mensalidade);
    }

    public void setMensalidade(float mensalidade) {
        this.mensalidade = mensalidade;
    }

    public float getPercentualDescontoAluguel() {
        return percentualDescontoAluguel;
    }

    public String getDescontoo() {
        NumberFormat formatter = NumberFormat.getPercentInstance();
        return formatter.format(percentualDescontoAluguel);
    }

    public void setPercentualDescontoAluguel(float percentualDescontoAluguel) {
        this.percentualDescontoAluguel = percentualDescontoAluguel;
    }
}
