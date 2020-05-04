package tcc.controlador;

import java.util.Optional;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import tcc.domain.Plano;
import tcc.repository.PlanoRepositorio;

@Controller
public class PlanoController {

    private final PlanoRepositorio repositorio;

    public PlanoController(PlanoRepositorio repo) {
        repositorio = repo;

    }

    @GetMapping(path = "/planos")
    public String lista(Model model, Authentication authentication
    ) {

        Iterable<Plano> planos = repositorio.findAll();
        
        model.addAttribute("authentication", authentication);
        model.addAttribute("planos", planos);

        return "planos/lista";
    }

    @PostMapping(path = "/planos")
    public String cria(RedirectAttributes redirectAttributes, float percentualDescontoAluguel, float mensalidade, String nome, String descricao) {

        Plano novoplano = new Plano();
        novoplano.setDescricao(descricao);
        novoplano.setMensalidade(mensalidade);
        novoplano.setNome(nome);
        novoplano.setPercentualDescontoAluguel(percentualDescontoAluguel);

        redirectAttributes.addAttribute("novoCadastro", "");
        repositorio.save(novoplano);
        return "redirect:/planos";

    }

    @GetMapping(path = "/planos/{codigo}")
    public String atualizaForm(@PathVariable Integer codigo,
            Model model) {
        Plano plano = repositorio.findById(codigo).get();
        model.addAttribute("plano", plano);
        return "planos/edita";
    }

    @PostMapping(path = "/planos/{codigo}" )
    public String atualiza(@PathVariable Integer codigo, float percentualDescontoAluguel, float mensalidade, String nome, String descricao) {

        Optional<Plano> planoOptional = repositorio.findById(codigo);
        if (planoOptional.isPresent()) {
            Plano plano = planoOptional.get();
            plano.setDescricao(descricao);
            plano.setMensalidade(mensalidade);
            plano.setNome(nome);
            plano.setPercentualDescontoAluguel(percentualDescontoAluguel);

            repositorio.save(plano);
        }

        return "redirect:/planos";
    }

    @GetMapping(path = "/planos/{codigo}/delete")
    public String deleta(@PathVariable Integer codigo) {
        repositorio.deleteById(codigo);
        return "redirect:/planos";

    }

}
