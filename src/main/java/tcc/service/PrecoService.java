package tcc.service;

import java.time.Duration;
import java.util.Date;
import org.springframework.stereotype.Component;
import tcc.domain.CategoriaVeiculo;

@Component
public class PrecoService {

    public float calculaPreco(CategoriaVeiculo categoriaVeiculo, Date dataInicial, Date dataFinal) {
        Duration duracao = Duration.between(dataInicial.toInstant(), dataFinal.toInstant());
        float qtdDias = (float) Math.ceil((double) duracao.getSeconds() / Duration.ofDays(1).getSeconds());
        return categoriaVeiculo.getPrecoDiaria() * qtdDias;
    }

}
