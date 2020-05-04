package tcc.domain;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;

@Entity

public class Funcionario {

    @Id
    private String cpf;
    private String nome;
    @Column(unique = true)
    private String login;
    private String senha;
    private boolean gerente;

    public boolean isGerente() {
        return gerente;
    }

    public void setGerente(boolean gerente) {
        this.gerente = gerente;
    }

    public String getCpf() {
        return cpf;
    }

    public void setCpf(String cpf) {
        this.cpf = cpf;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public String getSenha() {
        return senha;
    }

    public void setSenha(String senha) {
        this.senha = senha;
    }

}
