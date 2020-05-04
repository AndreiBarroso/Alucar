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
import tcc.domain.CategoriaVeiculo;
import tcc.domain.Veiculo;
import tcc.repository.CategoriaVeiculoRepositorio;

import tcc.repository.VeiculoRepositorio;
import tcc.service.VeiculoService;

import javax.servlet.http.HttpServletResponse;
import org.springframework.security.core.Authentication;

@Controller
public class VeiculoController {

    private final VeiculoRepositorio repositorio;
    private final VeiculoService veiculoService;
    private final CategoriaVeiculoRepositorio repocateg;

    public VeiculoController(VeiculoRepositorio repo, VeiculoService veiculoService, CategoriaVeiculoRepositorio repocat) {
        repositorio = repo;
        this.veiculoService = veiculoService;
        repocateg = repocat;

    }

    @GetMapping(path = "/veiculos")
    public String lista(Model model, Authentication authentication
    ) {
        model.addAttribute("authentication", authentication);
        model.addAttribute("veiculos", repositorio.findAll());
        model.addAttribute("categorias", repocateg.findAll());

        return "veiculos/lista";
    }

    @PostMapping(path = "/veiculos")
    public String cria(String placa, Character categoria, String marca, String modelo, int anoModelo, String cor,
                       int kilometragem, String observacao, boolean disponivel) {

        Optional<CategoriaVeiculo> categoriaOptional = repocateg.findById(categoria);

        if (!categoriaOptional.isPresent()) {
            throw new IllegalArgumentException("Categoria não existente");
        }

        Veiculo novoveiculo = new Veiculo();
        novoveiculo.setCategoria(categoriaOptional.get());
        novoveiculo.setAnoModelo(anoModelo);
        novoveiculo.setCor(cor);
//        novoveiculo.setDisponivel(disponivel);
        novoveiculo.setMarca(marca);
        novoveiculo.setModelo(modelo);
        novoveiculo.setPlaca(placa);
        novoveiculo.setKilometragem(kilometragem);
        novoveiculo.setObservacao(observacao);

        repositorio.save(novoveiculo);
        return "redirect:/veiculos";

    }

    @GetMapping(path = "/veiculos/{placa}")
    public String atualizaForm(@PathVariable String placa,
                               Model model) {
        Veiculo veiculo = repositorio.findById(placa).get();
        model.addAttribute("veiculo", veiculo);
        return "veiculos/edita";
    }

    @PostMapping(path = "/veiculos/{placa}")
    public String atualiza(@PathVariable String placa, String marca, String modelo, int anoModelo, String cor,
                           int kilometragem, String observacao, boolean disponivel) {

        Optional<Veiculo> veiculoOptional = repositorio.findById(placa);
        if (veiculoOptional.isPresent()) {
            Veiculo veiculo = veiculoOptional.get();
            veiculo.setAnoModelo(anoModelo);
            veiculo.setCor(cor);
//            veiculo.setDisponivel(disponivel);
            veiculo.setMarca(marca);
            veiculo.setModelo(modelo);
            veiculo.setPlaca(placa);
            veiculo.setKilometragem(kilometragem);
            veiculo.setObservacao(observacao);

            repositorio.save(veiculo);
        }

        return "redirect:/veiculos";
    }

    @GetMapping(path = "/veiculos/{placa}/delete")
    public String deleta(@PathVariable String placa) {
        repositorio.deleteById(placa);
        return "redirect:/veiculos";

    }

    @ExceptionHandler(Exception.class)

    public String handleException(Exception error) throws Exception {
        return "redirect:/veiculos?error";

    }

    @GetMapping(value = "/veiculos/report")
    public @ResponseBody
    ResponseEntity<InputStreamResource> downloadReport(HttpServletResponse response) throws DocumentException {
        ByteArrayInputStream result = veiculoService.getReport();
        HttpHeaders headers = new HttpHeaders();
        String documentName = "veiculos-" + LocalDate.now().toString() + ".pdf";
        headers.add("Content-Disposition", "inline; filename=" + documentName);

        return ResponseEntity
                .ok()
                .headers(headers)
                .contentType(MediaType.APPLICATION_PDF)
                .body(new InputStreamResource(result));
    }
}
