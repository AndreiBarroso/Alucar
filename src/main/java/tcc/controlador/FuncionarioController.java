package tcc.controlador;

import com.itextpdf.text.DocumentException;
import java.io.ByteArrayInputStream;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import javax.servlet.http.HttpServletResponse;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import tcc.domain.Funcionario;
import tcc.dto.FuncionarioDTO;
import tcc.repository.FuncionarioRepositorio;
import tcc.service.FuncionarioService;

@Controller
public class FuncionarioController {

    private final FuncionarioRepositorio repositorio;
    private final FuncionarioService funcionarioService;

    public FuncionarioController(FuncionarioRepositorio repo, FuncionarioService funcionarioService) {
        this.repositorio = repo;
        this.funcionarioService = funcionarioService;
    }

    @GetMapping(path = "/funcionarios")
    public String lista(Model model, Authentication authentication) {
        Iterable<Funcionario> funcionarios = repositorio.findAll();
        List<FuncionarioDTO> funcionariosDTO = new ArrayList<>();
        for (Funcionario f : funcionarios) {
            FuncionarioDTO fdto = new FuncionarioDTO(f);
            funcionariosDTO.add(fdto);
        }
        model.addAttribute("authentication", authentication);
        model.addAttribute("funcionarios", funcionariosDTO);
        return "funcionarios/lista";
    }

    @PostMapping(path = "/funcionarios")
    public String cria(String cpf, String nome, String login, String senha, boolean gerente) {
        Funcionario funcionarioExistente = repositorio.retornaFuncionario(login);
        if (funcionarioExistente != null) {
            return "redirect:/funcionarios?error=Login existente";
        }
        Funcionario novoFunc = new Funcionario();
        novoFunc.setCpf(cpf);
        novoFunc.setLogin(login);
        novoFunc.setNome(nome);
        novoFunc.setSenha(senha) ;
        novoFunc.setGerente(gerente);
        repositorio.save(novoFunc);
        return "redirect:/funcionarios";
    }

    @GetMapping(path = "/funcionarios/{cpf}")
    public String atualizaForm(@PathVariable String cpf,
                               Model model) {
        Funcionario funcionario = repositorio.findById(cpf).get();
        FuncionarioDTO funDTO = new FuncionarioDTO(funcionario);
        model.addAttribute("funcionario", funDTO);
        return "funcionarios/edita";
    }

    @PostMapping(path = "/funcionarios/{cpf}")
    public String atualiza(@PathVariable String cpf, String nome, String login,
                           String senha) {
        Optional<Funcionario> funcionarioOptional = repositorio.findById(cpf);
        if (funcionarioOptional.isPresent()) {
            Funcionario funcionario = funcionarioOptional.get();
            funcionario.setCpf(cpf);
            funcionario.setLogin(login);
            funcionario.setNome(nome);
            funcionario.setSenha(senha);
            repositorio.save(funcionario);
        }
        return "redirect:/funcionarios";
    }

    @GetMapping(path = "/funcionarios/{cpf}/delete")
    public String deleta(@PathVariable String cpf) {
        repositorio.deleteById(cpf);
        return "redirect:/funcionarios";

    }

    @GetMapping(value = "/funcionarios/report")
    public @ResponseBody
    ResponseEntity<InputStreamResource> downloadReport(HttpServletResponse response) throws DocumentException {
        ByteArrayInputStream result = funcionarioService.getReport();
        HttpHeaders headers = new HttpHeaders();
        String documentName = "funcionario-" + LocalDate.now().toString() + ".pdf";
        headers.add("Content-Disposition", "inline; filename=" + documentName);

        return ResponseEntity
                .ok()
                .headers(headers)
                .contentType(MediaType.APPLICATION_PDF)
                .body(new InputStreamResource(result));
    }

}
