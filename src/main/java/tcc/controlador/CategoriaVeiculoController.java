package tcc.controlador;

import java.util.Optional;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import tcc.domain.CategoriaVeiculo;
import tcc.repository.CategoriaVeiculoRepositorio;

@Controller
public class CategoriaVeiculoController {

    private final CategoriaVeiculoRepositorio repositorio;

    public CategoriaVeiculoController(CategoriaVeiculoRepositorio repo) {
        repositorio = repo;
//        criacategorias();
    }

    @GetMapping(path = "/categorias")
    public String lista(Model model,
            @RequestParam(required = false, defaultValue = "0") Float precoDiariaMaiorQue,
            @RequestParam(required = false, defaultValue = "999999") Float precoDiariaMenorQue) {
        Iterable<CategoriaVeiculo> categorias = repositorio.findAllFiltradoPorPreco(precoDiariaMaiorQue, precoDiariaMenorQue);
        model.addAttribute("categorias", categorias);
        return "categorias/lista";
    }

    @PostMapping(path = "/categorias")
    public String cria(RedirectAttributes redirectAttributes, char classe, float precoDiaria, float multaPercentual, String itensDisponiveis) {
        CategoriaVeiculo categoria = new CategoriaVeiculo();
        categoria.setCodigo(classe);
        categoria.setItensDisponiveis(itensDisponiveis);
        categoria.setPrecoDiaria(precoDiaria);
        categoria.setPercentualMultaDiaria(multaPercentual);
        repositorio.save(categoria);

        redirectAttributes.addAttribute("novoCadastro", "");
        return "redirect:/categorias";
    }

    @GetMapping(path = "/categorias/{classe}")
    public String atualizaForm(@PathVariable Character classe,
            Model model) {
        CategoriaVeiculo categoria = repositorio.findById(classe).get();
        model.addAttribute("categoria", categoria);
        return "categorias/edita";
    }

    @PostMapping(path = "/categorias/{classe}")
    public String atualiza(@PathVariable Character classe,
            float precoDiaria,
            float multaPercentual) {
        Optional<CategoriaVeiculo> categoriaOptional = repositorio.findById(classe);
        if (categoriaOptional.isPresent()) {
            CategoriaVeiculo categoria = categoriaOptional.get();
            categoria.setPrecoDiaria(precoDiaria);
            categoria.setPercentualMultaDiaria(multaPercentual);
            repositorio.save(categoria);
        }
        return "redirect:/categorias";
    }

    @GetMapping(path = "/categorias/{classe}/delete")
    public String deleta(@PathVariable Character classe
    ) {
        repositorio.deleteById(classe);
        return "redirect:/categorias";
    }

//    private void criacategorias() {
//        CategoriaVeiculo cat1 = new CategoriaVeiculo();
//        cat1.setCodigo('A');
//        cat1.setPrecoDiaria(300);
//        cat1.setPercentualMultaDiaria(0.10f);
//
//        CategoriaVeiculo cat2 = new CategoriaVeiculo();
//        cat2.setCodigo('B');
//        cat2.setPrecoDiaria(200);
//        cat2.setPercentualMultaDiaria(0.10f);
//
//        CategoriaVeiculo cat3 = new CategoriaVeiculo();
//        cat3.setCodigo('C');
//        cat3.setPrecoDiaria(100);
//        cat3.setPercentualMultaDiaria(0.10f);
//
//        repositorio.save(cat1);
//        repositorio.save(cat2);
//        repositorio.save(cat3);
//    }
    @ExceptionHandler(Exception.class)
    public String handleException(Exception error) throws Exception {
        return "redirect:/categorias?error=";

    }

}
