package tcc.service;

import java.util.Locale;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.thymeleaf.context.Context;
import org.thymeleaf.spring5.SpringTemplateEngine;
import tcc.domain.Aluguel;
import tcc.domain.Cliente;
import tcc.domain.Plano;
import tcc.domain.Veiculo;

/**
 * Componente para montar o email e disparar usando SendGrid.
 */
@Slf4j
@Component
public class EmailService {

    private static final String TEMPLATE_CADASTRO_CLIENTE = "mail/cadastroCliente";
    private static final String TITULO_CADASTRO_CLIENTE = "Cadastro realizado com sucesso";

    private static final String TEMPLATE_CONFIRMACAO_ALUGUEL = "mail/confirmaAluguel";
    private static final String TITULO_CONFIRMACAO_ALUGUEL = "Obrigado por Alugar com a Alucar";

    private static final String TEMPLATE_CONFIRMACAO_DEVOLUCAO = "mail/confirmaDevolucao";
    private static final String TITULO_CONFIRMACAO_DEVOLUCAO = "Aluguel Encerrado";

    private static final String TEMPLATE_ENVIO_FATURA = "mail/envioDeFatura";
    private static final String TITULO_ENVIO_FATURA = "Pagamento mensal do plano";



    private static final Locale LOCALE = new Locale("pt", "BR");


    private final SpringTemplateEngine templateEngine;
    private final SendGridEmailService sendGridEmailService;

    public EmailService(SendGridEmailService sendGridEmailService, SpringTemplateEngine templateEngine) {
        this.sendGridEmailService = sendGridEmailService;
        this.templateEngine = templateEngine;
    }
    @Async

    public void enviarEmailCadastroCliente(Cliente cliente) {
        Context context = new Context(LOCALE);
        context.setVariable("nome", cliente.getNome());
        context.setVariable("cpf", cliente.getCpf());
        context.setVariable("telefone", cliente.getTelefone());
        String content = templateEngine.process(TEMPLATE_CADASTRO_CLIENTE, context);
        sendGridEmailService.sendEmail(cliente.getEmail(), TITULO_CADASTRO_CLIENTE, content);
    }

    @Async

    public void enviarEmailConfirmacaoAluguel(Aluguel aluguel) {
        Context context = new Context(LOCALE);
        context.setVariable("nome", aluguel.getCliente().getNome());
        context.setVariable("dataAluguel", aluguel.getDataAluguel());
        context.setVariable("dataDevolucaoEsperada", aluguel.getDataDevolucaoEsperada());
        context.setVariable("placa", aluguel.getVeiculo().getPlaca());
        String content = templateEngine.process(TEMPLATE_CONFIRMACAO_ALUGUEL, context);
        sendGridEmailService.sendEmail(aluguel.getCliente().getEmail(), TITULO_CONFIRMACAO_ALUGUEL, content);
    }
    @Async
    public void enviarEmailConfirmacaoDevolucao(Aluguel aluguel) {
        Context context = new Context(LOCALE);
        context.setVariable("nome", aluguel.getCliente().getNome());
        context.setVariable("valRestituir", aluguel.getValRestituirStr());
        context.setVariable("dataDevolucaoReal", aluguel.getDataDevolucaoReal());
        context.setVariable("precoMulta", aluguel.getPrecoMultaStr());
        String content = templateEngine.process(TEMPLATE_CONFIRMACAO_DEVOLUCAO, context);
        sendGridEmailService.sendEmail(aluguel.getCliente().getEmail(), TITULO_CONFIRMACAO_DEVOLUCAO, content);
    }

    @Async
    public void enviarEmailDeFatura (Cliente cliente) {
        Context context = new Context(LOCALE);
        context.setVariable("nome", cliente.getNome());
        context.setVariable("cpf", cliente.getCpf());
        context.setVariable("telefone", cliente.getTelefone());
        String content = templateEngine.process(TEMPLATE_ENVIO_FATURA, context);
        sendGridEmailService.sendEmail(cliente.getEmail(), TITULO_ENVIO_FATURA, content);
    }

//    public void enviarEmailConfirmacaoDevolucao(Cliente cliente, float valor) {
//
//    }

    public void enviarEmailAvisoAtraso(Cliente cliente, float valor) {

    }

//    public void enviarEmailFaturaMensalidadePlano(Cliente cliente, Plano plano) {
//
//    }

}