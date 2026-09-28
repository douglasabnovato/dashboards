// Cliente HTTP da API REST; converte erros de rede em ApiIndisponivelException e 409 em WipExcedidoException
package com.learntech.dashboards.web.api;

import com.learntech.dashboards.core.kanban.Etapa;
import com.learntech.dashboards.core.kanban.FiltroTarefas;
import com.learntech.dashboards.core.kanban.Projeto;
import com.learntech.dashboards.core.kanban.Quadro;
import com.learntech.dashboards.core.kanban.Tarefa;
import com.learntech.dashboards.core.kanban.WipExcedidoException;
import com.learntech.dashboards.core.metricas.PainelMetricas;
import com.learntech.dashboards.core.simulador.ParametrosSimulacao;
import com.learntech.dashboards.core.simulador.ResultadoSimulacao;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpStatus;
import org.springframework.http.client.JdkClientHttpRequestFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestClient;
import org.springframework.web.client.RestClientException;
import org.springframework.web.util.UriBuilder;

import java.net.http.HttpClient;
import java.time.Duration;
import java.util.List;
import java.util.Map;
import java.util.function.Supplier;

@Component
public class DashboardsApiClient {

    /** Preset como a API devolve. */
    public record PresetDto(String codigo, String rotulo, boolean padrao, ParametrosSimulacao parametros) {
    }

    private final RestClient http;

    // configura URL base, chave e tempos limite; usa o HttpClient do JDK porque HttpURLConnection não aceita PATCH
    public DashboardsApiClient(RestClient.Builder builder,
                               @Value("${dashboards.api.base-url}") String baseUrl,
                               @Value("${dashboards.api.key:}") String chave) {
        HttpClient jdk = HttpClient.newBuilder()
                .connectTimeout(Duration.ofSeconds(5))
                .version(HttpClient.Version.HTTP_1_1)
                .build();
        JdkClientHttpRequestFactory fabrica = new JdkClientHttpRequestFactory(jdk);
        fabrica.setReadTimeout(Duration.ofSeconds(60));
        this.http = builder.baseUrl(baseUrl).requestFactory(fabrica)
                .defaultHeader("X-Api-Key", chave)
                .build();
    }

    // painel de métricas; fase nula = todas
    public PainelMetricas painel(Integer fase) {
        return chamar(() -> http.get()
                .uri(u -> {
                    UriBuilder b = u.path("/api/metricas");
                    if (fase != null) {
                        b.queryParam("fase", fase);
                    }
                    return b.build();
                })
                .retrieve().body(PainelMetricas.class));
    }

    // presets do simulador
    public List<PresetDto> presets() {
        return chamar(() -> http.get().uri("/api/simulador/presets")
                .retrieve().body(new ParameterizedTypeReference<List<PresetDto>>() { }));
    }

    // projeção do simulador
    public ResultadoSimulacao simular(ParametrosSimulacao p) {
        return chamar(() -> http.post().uri("/api/simulador").body(p)
                .retrieve().body(ResultadoSimulacao.class));
    }

    // quadro Kanban com filtros
    public Quadro quadro(Projeto projeto, FiltroTarefas.FiltroDia dia) {
        return chamar(() -> http.get()
                .uri(u -> {
                    UriBuilder b = u.path("/api/kanban");
                    if (projeto != null) {
                        b.queryParam("projeto", projeto);
                    }
                    if (dia != null) {
                        b.queryParam("dia", dia);
                    }
                    return b.build();
                })
                .retrieve().body(Quadro.class));
    }

    // move a tarefa; 409 vira WipExcedidoException com a tarefa ativa
    public Tarefa mover(String id, Etapa etapa) {
        try {
            return http.patch().uri("/api/kanban/tarefas/{id}", id).body(Map.of("etapa", etapa))
                    .retrieve().body(Tarefa.class);
        } catch (HttpClientErrorException e) {
            if (e.getStatusCode().isSameCodeAs(HttpStatus.CONFLICT)) {
                Map<?, ?> corpo = e.getResponseBodyAs(Map.class);
                throw new WipExcedidoException(corpo == null ? "?" : String.valueOf(corpo.get("tarefaAtiva")));
            }
            throw new ApiIndisponivelException("A API recusou o movimento (" + e.getStatusCode().value() + ")", e);
        } catch (RestClientException e) {
            throw new ApiIndisponivelException("API indisponível", e);
        }
    }

    // restaura o quadro inicial
    public void restaurar() {
        chamar(() -> http.post().uri("/api/kanban/reset").retrieve().toBodilessEntity());
    }

    // executa a chamada traduzindo falhas de rede ou HTTP para ApiIndisponivelException
    private static <T> T chamar(Supplier<T> chamada) {
        try {
            return chamada.get();
        } catch (RestClientException e) {
            throw new ApiIndisponivelException("API indisponível", e);
        }
    }
}
// fim de DashboardsApiClient.java
