package tcc.domain;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.ManyToOne;

@Entity
public class Veiculo {

    @Id
    private String placa;
    private String marca;
    private String modelo;
    private int anoModelo;
    private String cor;
//    private boolean disponivel;
    private int kilometragem;
    private String observacao;
    @ManyToOne
    private CategoriaVeiculo categoria;

    public String getPlaca() {
        return placa;
    }

    public void setPlaca(String placa) {
        this.placa = placa;
    }

    public String getMarca() {
        return marca;
    }

    public void setMarca(String marca) {
        this.marca = marca;
    }

    public String getModelo() {
        return modelo;
    }

    public void setModelo(String modelo) {
        this.modelo = modelo;
    }

    public int getAnoModelo() {
        return anoModelo;
    }

    public void setAnoModelo(int anoModelo) {
        this.anoModelo = anoModelo;
    }

    public String getCor() {
        return cor;
    }

    public void setCor(String cor) {
        this.cor = cor;
    }

//    public boolean isDisponivel() {
//        return disponivel;
//    }
//
//    public void setDisponivel(boolean disponivel) {
//        this.disponivel = disponivel;
//    }

    public int getKilometragem() {
        return kilometragem;
    }

    public void setKilometragem(int kilometragem) {
        this.kilometragem = kilometragem;
    }

    public String getObservacao() {
        return observacao;
    }

    public void setObservacao(String observacao) {
        this.observacao = observacao;
    }

    public CategoriaVeiculo getCategoria() {
        return categoria;
    }

    public void setCategoria(CategoriaVeiculo categoria) {
        this.categoria = categoria;
    }

}
