package tcc.dto;

import tcc.domain.Funcionario;

import javax.validation.constraints.Size;

/**
 * funcionario dto é uma classe para esconder a senha do funcionário e se baseia
 * no padrão de projeto DTO.
 *
 * @author andrei
 */
public class FuncionarioDTO {

    @Size(max = 11, message = "Limite de caracteres permitidos 11")
    private final String cpf;
    private final String nome;
    private final String login;

    public FuncionarioDTO(Funcionario f) {
        cpf = f.getCpf();
        nome = f.getNome();
        login = f.getLogin();
    }

    /**
     * @return the cpf
     */
    public String getCpf() {
        return cpf;
    }

    /**
     * @return the nome
     */
    public String getNome() {
        return nome;
    }

    /**
     * @return the security
     */
    public String getLogin() {
        return login;
    }

}
