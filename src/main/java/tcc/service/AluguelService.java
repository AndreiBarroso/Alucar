package tcc.service;

import java.io.ByteArrayInputStream;
import java.text.SimpleDateFormat;
import java.time.Duration;
import java.time.Instant;
import java.util.*;
import java.util.concurrent.atomic.AtomicReference;

import com.itextpdf.text.BaseColor;
import com.itextpdf.text.DocumentException;
import lombok.AllArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import tcc.config.AluguelProperties;
import tcc.domain.*;
import tcc.repository.AluguelRepositorio;
import tcc.repository.FuncionarioRepositorio;

@Service
@AllArgsConstructor
public class AluguelService {

    private final AluguelRepositorio repositorio;
    private final EmailService emailService;
    private final ReportService reportService;
    private final FuncionarioRepositorio funcionarioRepositorio;
    private final PrecoService precoService;
    private final AluguelProperties properties;

    public Iterable<Aluguel> listaNaoConcluidos() {
        return repositorio.findAllByConcluidoFalse();
    }

    public List<Aluguel> listaPorVeiculoEDatas (Veiculo veiculo, Date dataInicial, Date dataFinal) {

        return repositorio.listaPorVeiculo (veiculo,  dataInicial, dataFinal);
    }

    public Aluguel findById(int codigo) {
        return repositorio.findById(codigo)
                .orElseThrow(() -> new IllegalArgumentException("Aluguel não encotrado."));
    }

    public void cria(Cliente cliente, Veiculo veiculo,Date dataAluguel, Date dataDevolucaoEsperada) {
        if (Instant.now().isAfter(dataAluguel.toInstant())){
            throw new IllegalArgumentException ("Impedido de realizar, selecione uma data e hora atual .");

        }
        boolean disponivel= listaPorVeiculoEDatas(veiculo, dataAluguel, dataDevolucaoEsperada).isEmpty();
        if (!disponivel){
            throw new IllegalArgumentException("Veiculo indisponivel na data especificada.");
        }
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
//        Date dataAluguel = new Date();
        float preco = precoService.calculaPreco(veiculo.getCategoria(), dataAluguel, dataDevolucaoEsperada);
        float desconto = 0f;
        Plano plano = cliente.getPlano();
        if (plano != null) {
            desconto = plano.getPercentualDescontoAluguel();
        }

        float precoFinal = preco - (preco * desconto);
        float precoSinal = precoFinal * properties.getPercentualSinal();

        String currentUserName = ((UserDetails) principal).getUsername();

        Aluguel aluguel = Aluguel.builder()
                .dataAluguel(dataAluguel)
                .cliente(cliente)
                .veiculo(veiculo)
                .funcionario(funcionarioRepositorio.retornaFuncionario(currentUserName))
                .dataDevolucaoEsperada(dataDevolucaoEsperada)
                .precoFinal(precoFinal)
                .preco(preco)
                .precoSinal(precoSinal)
                .desconto(desconto)
                .quilometragemInicial(veiculo.getKilometragem())
                .quilometragemFinal(veiculo.getKilometragem())
                .build();

        repositorio.save(aluguel);
        emailService.enviarEmailConfirmacaoAluguel(aluguel);
    }

    public void deleta(Integer codigo) {
        Optional<Aluguel> aluguelOptional = repositorio.findByCodigoAndConcluidoFalse(codigo);
        if (aluguelOptional.isPresent()) {
            Aluguel aluguel = aluguelOptional.get();
            aluguel.setConcluido(true);
            repositorio.save(aluguel);
        } else {
            throw new IllegalArgumentException("Aluguel não encontrado");
        }
    }

    public void cancela (Integer codigo) {
        Optional<Aluguel> aluguelOptional = repositorio.findByCodigo(codigo);
        if (aluguelOptional.isPresent()) {
            Date agora = new Date();
            Aluguel aluguel = aluguelOptional.get();
            if (aluguel.getDataAluguel().before(agora)) {
                throw  new IllegalArgumentException("Impossibilitado de realizar, aluguel em andamento.");
            }
            aluguel.setCancelado(true);
            repositorio.save(aluguel);
        } else {
            throw new IllegalArgumentException("Aluguel não encontrado");
        }
    }

    public void confirmaPagamento(Integer codigo, boolean sinalPago, boolean restantePago, boolean retirado) {
        Optional<Aluguel> aluguelOptional = repositorio.findByCodigoAndConcluidoFalse(codigo);
        if (aluguelOptional.isPresent()) {
            Aluguel aluguel = aluguelOptional.get();
            if (!aluguel.isSinalPago())
                aluguel.setSinalPago(sinalPago);
            if (!aluguel.isPago())
                aluguel.setPago(sinalPago && restantePago);
            Date dataAluguel = new Date(aluguel.getDataAluguel().getTime());
            if (retirado &&  dataAluguel.toInstant().isAfter(Instant.now())) {
                throw new IllegalArgumentException("veiculo nao pode ser retirado, antes da inicializacao.");
            }
            if (!aluguel.isRetirado())
                aluguel.setRetirado(retirado);
//            aluguel.setRetirado(retirado);
            repositorio.save(aluguel);
        } else {
            throw new IllegalArgumentException("Aluguel não encontrado");
        }
    }

    public void finalizaAluguel(Integer codigo) {
        Optional<Aluguel> aluguelOptional = repositorio.findByCodigoAndConcluidoFalse(codigo);
        if (aluguelOptional.isPresent()) {
            Aluguel aluguel = aluguelOptional.get();
            Date agora = new Date();
            Date dataDevolucaoEsperada=new Date(aluguel.getDataDevolucaoEsperada().getTime());

            if (dataDevolucaoEsperada.before(agora)) {
                float diariasExcedentes = precoService.calculaPreco(aluguel.getVeiculo().getCategoria(), dataDevolucaoEsperada, agora);
                float multaTotal = diariasExcedentes * (1 + aluguel.getVeiculo().getCategoria().getPercentualMultaDiaria());
                aluguel.setPrecoMulta(multaTotal);
            } else {
                Date dataInicialOuAgora= new Date(Math.max(aluguel.getDataAluguel().getTime(), agora.getTime()));
                Duration diferenca= Duration.between(dataInicialOuAgora.toInstant(),dataDevolucaoEsperada.toInstant());
                float valor= diferenca.toDays()*aluguel.getVeiculo().getCategoria().getPrecoDiaria()*(1-aluguel.getDesconto());
                float valorMaximoRetornavel = aluguel.getPrecoFinal()-aluguel.getPrecoSinal();
                aluguel.setValRestituir(Math.min(valor,valorMaximoRetornavel));
            }


            repositorio.save(aluguel);
        } else {
            throw new IllegalArgumentException("Aluguel não encontrado");
        }
    }

    public void confirmaDevolucao(Integer codigo, int kilometragem, String observacao) {
        Optional<Aluguel> aluguelOptional = repositorio.findByCodigoAndConcluidoFalse(codigo);
        if (aluguelOptional.isPresent()) {
            Aluguel aluguel = aluguelOptional.get();
            aluguel.setMultaPaga(true);

            if (aluguel.getVeiculo() != null) {
                aluguel.getVeiculo().setKilometragem(kilometragem);
                aluguel.getVeiculo().setObservacao(observacao);
            }

            // Ativa veiculo ao finalizar aluguel
//            aluguel.setVeiculo(veiculoService.ativaVeiculo(aluguel.getVeiculo()));

            aluguel.setDataDevolucaoReal(new Date());
            repositorio.save(aluguel);
            emailService.enviarEmailConfirmacaoDevolucao(aluguel);

        } else {
            throw new IllegalArgumentException("Aluguel não encontrado");
        }
    }

    public Long getAluguelCountByFuncionario(Funcionario funcionario) {
        return repositorio.countByFuncionario(funcionario);
    }

    public ByteArrayInputStream getReport(TipoRelatorio tipoRelatorio) throws DocumentException {
        String[] fields = {"Código", "Funcionário", "Status", "Veículo", "Cliente", "Data de locação", "Data devolução esperada", "Data de encerramento"};
        return reportService.createPDFReport(geraRelatorio(tipoRelatorio), "Aluguéis", fields, BaseColor.LIGHT_GRAY);
    }

    //    private List<String> fillReportRows() {
//        Set<Aluguel> result = repositorio.findAll();
//        List<String> cellsValues = new ArrayList<>();
//        SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
//        Date agora = new Date();
//
//        result.forEach(data -> {
//            AtomicReference<String> status = new AtomicReference<>("");
//
//            if (data.getDataDevolucaoEsperada().before(agora)) {
//                status.set("DevoluçAo atrasada");
//            }
//
//            if (data.getDataDevolucaoReal() != null) {
//                status.set("Finalizado");
//            } else {
//                status.set("Em andamento");
//            }
//
//            cellsValues.add(data.getCodigo().toString());
//            cellsValues.add(data.getFuncionario().getNome());
//            cellsValues.add(status.get());
//            cellsValues.add(data.getVeiculo().getPlaca().concat(" - ").concat(data.getVeiculo().getModelo()));
//            cellsValues.add(data.getCliente().getCpf().concat(" - ").concat(data.getCliente().getNome()));
//            cellsValues.add(data.getDataAluguel() != null ? dateFormat.format(data.getDataAluguel()) : "");
//            cellsValues.add(data.getDataDevolucaoEsperada() != null ? dateFormat.format(data.getDataDevolucaoEsperada()) : "");
//            cellsValues.add(data.getDataDevolucaoReal() != null ? dateFormat.format(data.getDataDevolucaoReal()) : "");
//        });
//
//        return cellsValues;
//    }
    private List<String> geraRelatorio(TipoRelatorio tipoRelatorio) {
        List<String> cellsValues = new ArrayList<>();
        Date agora = new Date();
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm");
        List<Aluguel> result ;
        if ( tipoRelatorio.equals(TipoRelatorio.ATRASADO)){

            result= repositorio.listaAtrasado(agora);
        } else if ( tipoRelatorio.equals(TipoRelatorio.CONCLUIDO)) {
            result= repositorio.listaConcluido();
        } else if ( tipoRelatorio.equals(TipoRelatorio.EM_ANDAMENTO)) {
            result= repositorio.listaAndamento();
        } else {
            result=repositorio.findAll();
        }

        result.forEach(data -> {
            AtomicReference<String> status = new AtomicReference<>("");

            if (data.getDataDevolucaoEsperada().before(agora)) {
                status.set("DevoluçAo atrasada");
            }

            if (data.getDataDevolucaoReal() != null) {
                status.set("Finalizado");
            } else {
                status.set("Em andamento");
            }

            cellsValues.add(data.getCodigo().toString());
            cellsValues.add(data.getFuncionario().getNome());
            cellsValues.add(status.get());
            cellsValues.add(data.getVeiculo().getPlaca().concat(" - ").concat(data.getVeiculo().getModelo()));
            cellsValues.add(data.getCliente().getCpf().concat(" - ").concat(data.getCliente().getNome()));
            cellsValues.add(data.getDataAluguel() != null ? dateFormat.format(data.getDataAluguel()) : "");
            cellsValues.add(data.getDataDevolucaoEsperada() != null ? dateFormat.format(data.getDataDevolucaoEsperada()) : "");
            cellsValues.add(data.getDataDevolucaoReal() != null ? dateFormat.format(data.getDataDevolucaoReal()) : "");

        });


        return cellsValues;
    }

    public ByteArrayInputStream geraRelatorioFinancasPorAno () throws DocumentException {
        String[] fields = { "Ano", "Total R$"};
        List<String> cellsValues = new ArrayList<>();
        Object[][] map = repositorio.reportTotalPorAno();
        for (Object[] obj : map) {
            cellsValues.add(String.valueOf(((Double) obj[0]).intValue()));
            cellsValues.add(obj[1].toString());
        }
        return reportService.createPDFReport(cellsValues, "Finanças", fields, BaseColor.LIGHT_GRAY);

    }

}
