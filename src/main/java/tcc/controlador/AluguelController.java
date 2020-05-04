package tcc.controlador;

import java.io.ByteArrayInputStream;
import java.time.LocalDate;
import java.util.Date;

import com.itextpdf.text.DocumentException;
import org.springframework.core.io.InputStreamResource;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import tcc.domain.Aluguel;
import tcc.domain.CategoriaVeiculo;
import tcc.domain.Cliente;
import tcc.domain.Veiculo;
import tcc.dto.AluguelDTO;
import tcc.repository.ClienteRepositorio;
import tcc.repository.VeiculoRepositorio;
import tcc.service.AluguelService;
import tcc.service.TipoRelatorio;

import javax.servlet.http.HttpServletResponse;

@Controller
public class AluguelController {

    private final AluguelService aluguelService;
    private final ClienteRepositorio repositorioCliente;
    private final VeiculoRepositorio repositorioVeiculo;

    public AluguelController(AluguelService aluguelService,
                             ClienteRepositorio repositorioCliente,
                             VeiculoRepositorio repositorioVeiculo) {
        this.aluguelService = aluguelService;
        this.repositorioCliente = repositorioCliente;
        this.repositorioVeiculo = repositorioVeiculo;
    }

    @GetMapping(path = "/alugueis")
    public String lista(Model model) {
        model.addAttribute("alugueis", aluguelService.listaNaoConcluidos());
        model.addAttribute("veiculos", repositorioVeiculo.findAll());
        model.addAttribute("clientes", repositorioCliente.findAll());
        model.addAttribute("aluguel", new AluguelDTO());
        model.addAttribute("agora", new Date());
        return "alugueis/lista";
    }

    @PostMapping(path = "/alugueis")
    public String cria(String clienteCpf,
                       String placaVeiculo,
                       @DateTimeFormat(pattern = "yyyy-MM-dd'T'HH:mm") Date dataAluguel,
                       @DateTimeFormat(pattern = "yyyy-MM-dd'T'HH:mm") Date dataDevolucaoEsperada,
                       Model model) {

        Cliente cliente = repositorioCliente.findById(clienteCpf)
                .orElseThrow(() -> new IllegalArgumentException("Cliente não existente"));

        Veiculo veiculo = repositorioVeiculo.findById(placaVeiculo)
                .orElseThrow(() -> new IllegalArgumentException("Veiculo não existente"));

        try {
            aluguelService.cria(cliente, veiculo, dataAluguel, dataDevolucaoEsperada);
        } catch (Exception ex){
            model.addAttribute("errorr", "Veículo Indisponivel.");
            throw ex;
        }
        return "redirect:/alugueis";
    }

    @GetMapping(path = "/alugueis/{codigo}/pagamento")
    public String pagamento(@PathVariable Integer codigo, Model model) {
        model.addAttribute("aluguel", aluguelService.findById(codigo));
        return "alugueis/pagamento";
    }

    @PostMapping(path = "/alugueis/{codigo}/pagamento")
    public String confirmaPagamento(@PathVariable Integer codigo, boolean sinalPago, boolean retirado, boolean restantePago) {
        aluguelService.confirmaPagamento(codigo, sinalPago, restantePago, retirado);
        return "redirect:/alugueis";
    }

    @GetMapping(path = "/alugueis/{codigo}/devolucao")
    public String devolucao(@PathVariable Integer codigo, Model model) {
        Aluguel aluguel =  aluguelService.findById(codigo);
        model.addAttribute("kilometragem", 0L);
        model.addAttribute("observacao", "");
        model.addAttribute("aluguel", aluguel);
        aluguelService.finalizaAluguel(codigo);
        return "alugueis/devolucao";
    }

    @PostMapping(path = "/alugueis/{codigo}/devolucao")
    public String confirmaDevolucao(@PathVariable Integer codigo, int kilometragem, String observacao, Model model) {
        Aluguel aluguel =  aluguelService.findById(codigo);

        if (aluguel.getVeiculo() != null && kilometragem < aluguel.getVeiculo().getKilometragem()) {
            model.addAttribute("errorMessage", "Quilometragem não pode ser menor que a Quilometragem inicial do veículo.");
            model.addAttribute("aluguel", aluguel);
            model.addAttribute("kilometragem", kilometragem);
            model.addAttribute("observacao", observacao);
            return "alugueis/devolucao";
        }

        aluguelService.confirmaDevolucao(codigo, kilometragem, observacao);
        return "redirect:/alugueis";
    }

    @GetMapping(path = "/alugueis/{codigo}/delete")
    public String deleta(@PathVariable Integer codigo) {
        aluguelService.deleta(codigo);
        return "redirect:/alugueis";
    }

    @GetMapping(path = "/alugueis/{codigo}/cancelamento")
    public String cancelado(@PathVariable Integer codigo) {
        aluguelService.cancela(codigo);
        return "redirect:/alugueis";

    }

//    @GetMapping(path = "/alugueis/{codigo}/retirar")
//    public String veiculoRetirado(@PathVariable Integer codigo) {
//        aluguelService.veiculoRetirado(codigo);
//        return "redirect:/alugueis";
//
//    }


    @ExceptionHandler(Exception.class)
    public String handleException(Exception error) throws Exception {
        return "redirect:/alugueis?error="+error.getMessage();
    }

    @GetMapping(value = "/alugueis/report/{tipoRelatorio}")
    public @ResponseBody
    ResponseEntity<InputStreamResource> downloadReport(HttpServletResponse response,
                                                       @PathVariable TipoRelatorio tipoRelatorio ) throws DocumentException {
        ByteArrayInputStream result = aluguelService.getReport(tipoRelatorio);
        HttpHeaders headers = new HttpHeaders();
        String documentName = "aluguel-" + LocalDate.now().toString() + ".pdf";
        headers.add("Content-Disposition", "inline; filename=" + documentName);

        return ResponseEntity
                .ok()
                .headers(headers)
                .contentType(MediaType.APPLICATION_PDF)
                .body(new InputStreamResource(result));
    }


    @GetMapping(value = "/alugueis/reportFinancasTotal")
    public @ResponseBody
        ResponseEntity<InputStreamResource> relatorioFinancas() throws DocumentException {
        ByteArrayInputStream result = aluguelService.geraRelatorioFinancasPorAno();
        HttpHeaders headers = new HttpHeaders();
        String documentName = "fincancas-" + LocalDate.now().toString() + ".pdf";
        headers.add("Content-Disposition", "inline; filename=" + documentName);

        return ResponseEntity
                .ok()
                .headers(headers)
                .contentType(MediaType.APPLICATION_PDF)
                .body(new InputStreamResource(result));
    }
}
