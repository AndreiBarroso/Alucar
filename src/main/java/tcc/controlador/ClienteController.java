package tcc.controlador;

import java.io.ByteArrayInputStream;
import java.time.LocalDate;
import java.util.Optional;

import com.itextpdf.text.DocumentException;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import tcc.service.ClienteService;

import javax.servlet.http.HttpServletResponse;
import tcc.domain.Cliente;
import tcc.domain.Plano;
import tcc.repository.PlanoRepositorio;

@Controller
public class ClienteController {

    private final ClienteService clienteService;
    private final PlanoRepositorio planoRepo;

    public ClienteController(ClienteService clienteService, PlanoRepositorio planoRepo) {
        this.clienteService = clienteService;
        this.planoRepo = planoRepo;

    }

    @GetMapping(path = "/clientes")
    public String lista(Model model
    ) {

        Iterable<Cliente> cliente = clienteService.findAll();

        model.addAttribute("clientes", cliente);
        model.addAttribute("planos", planoRepo.findAll());

        return "clientes/lista";
    }

    @PostMapping(path = "/clientes")
    public String cria(RedirectAttributes redirectAttributes, String cpf, Integer plano, String endereco, String nome, String email,
                       String dataNascimento, String telefone, String profissao) {

        Optional<Plano> planoOptional = planoRepo.findById(plano);

        Cliente novocliente = new Cliente();
        novocliente.setPlano(planoOptional.get());
        novocliente.setCpf(cpf);  
        novocliente.setDataNascimento(dataNascimento);
        novocliente.setEmail(email);
        novocliente.setEndereco(endereco);
        novocliente.setNome(nome);
        novocliente.setProfissao(profissao);
        novocliente.setTelefone(telefone);

        redirectAttributes.addAttribute("novoCadastro", "");
        clienteService.save(novocliente);
        return "redirect:/clientes";

    }

    @GetMapping(path = "/clientes/{cpf}")
    public String atualizaForm(@PathVariable String cpf,
            Model model) {
        Cliente cliente = clienteService.find(cpf).get();
        model.addAttribute("cliente", cliente);
        return "clientes/edita";
    }

    @PostMapping(path = "/clientes/{cpf}")
    public String atualiza(@PathVariable String cpf, String endereco, String nome, String email,
            String dataNascimento, String telefone, String profissao) {

        Optional<Cliente> clienteOptional = clienteService.find(cpf);
        if (clienteOptional.isPresent()) {
            Cliente cliente = clienteOptional.get();
            cliente.setCpf(cpf);
            cliente.setDataNascimento(dataNascimento);
            cliente.setEmail(email);
            cliente.setEndereco(endereco);
            cliente.setNome(nome);
            cliente.setProfissao(profissao);
            cliente.setTelefone(telefone);

            clienteService.save(cliente);
        }

        return "redirect:/clientes";
    }

    @GetMapping(path = "/clientes/{cpf}/delete")
    public String deleta(@PathVariable String cpf) {
        clienteService.delete(cpf);
        return "redirect:/clientes";

    }

    @GetMapping(value = "/clientes/report")
    public @ResponseBody
    ResponseEntity<InputStreamResource> downloadReport(HttpServletResponse response) throws DocumentException {
        ByteArrayInputStream result = clienteService.getReport();
        HttpHeaders headers = new HttpHeaders();
        String documentName = "cliente-" + LocalDate.now().toString() + ".pdf";
        headers.add("Content-Disposition", "inline; filename=" + documentName);

        return ResponseEntity
                .ok()
                .headers(headers)
                .contentType(MediaType.APPLICATION_PDF)
                .body(new InputStreamResource(result));
    }

    @ExceptionHandler(Exception.class)
    public String handleException(Exception error) throws Exception {
        return "redirect:/clientes?error=CPF Nulo";

    }

}
