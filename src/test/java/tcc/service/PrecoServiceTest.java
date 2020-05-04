package tcc.service;

import static junit.framework.TestCase.assertEquals;

import java.time.Instant;
import java.util.Date;
import org.junit.Test;
import tcc.domain.CategoriaVeiculo;

public class PrecoServiceTest {

    private static final float PRECO_DIARIA_DE_TESTE = 100F;

    private PrecoService precoService = new PrecoService();
    private CategoriaVeiculo categoria;

    public PrecoServiceTest() {
        categoria = new CategoriaVeiculo();
        categoria.setPrecoDiaria(PRECO_DIARIA_DE_TESTE);
    }

    @Test
    public void testa24Horas_eRetorna1Diaria() {
        // Formato ISO8601
        Date dataInicial = Date.from(Instant.parse("2020-01-10T10:00:00.00Z"));
        Date dataFinal   = Date.from(Instant.parse("2020-01-11T10:00:00.00Z"));

        float resposta = precoService.calculaPreco(categoria, dataInicial, dataFinal);
        assertEquals(PRECO_DIARIA_DE_TESTE, resposta);
    }

    @Test
    public void testa24HorasE1Segundo_eRetorna2Diarias() {
        // Formato ISO8601
        Date dataInicial = Date.from(Instant.parse("2020-01-10T10:00:00.00Z"));
        Date dataFinal   = Date.from(Instant.parse("2020-01-11T10:00:01.00Z"));

        float resposta = precoService.calculaPreco(categoria, dataInicial, dataFinal);
        assertEquals(2 * PRECO_DIARIA_DE_TESTE, resposta);
    }

    @Test
    public void testa48Horas_eRetorna2Diarias() {
        // Formato ISO8601
        Date dataInicial = Date.from(Instant.parse("2020-01-10T10:00:00.00Z"));
        Date dataFinal   = Date.from(Instant.parse("2020-01-12T10:00:00.00Z"));

        float resposta = precoService.calculaPreco(categoria, dataInicial, dataFinal);
        assertEquals(2 * PRECO_DIARIA_DE_TESTE, resposta);
    }

}