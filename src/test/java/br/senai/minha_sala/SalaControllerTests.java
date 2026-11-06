package br.senai.minha_sala;

import br.senai.minha_sala.repository.SalaRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT, properties = {
        "spring.datasource.url=jdbc:h2:mem:sala-tests;DB_CLOSE_DELAY=-1",
        "spring.jpa.hibernate.ddl-auto=create-drop"
})
class SalaControllerTests {
    @LocalServerPort
    private int port;

    @Autowired
    private SalaRepository repository;

    private final HttpClient client = HttpClient.newHttpClient();

    @BeforeEach
    void limparBanco() {
        repository.deleteAll();
    }

    @Test
    void deveExecutarCrudCompleto() throws Exception {
        assertThat(request("GET", "/salas", null).body()).isEqualTo("[]");
        var criada = request("POST", "/salas", "{\"id\":999,\"nome\":\"Sala A\",\"capacidade\":30}");
        assertThat(criada.statusCode()).isEqualTo(201);
        String location = criada.headers().firstValue("Location").orElseThrow();
        String caminho = URI.create(location).getPath();
        assertThat(caminho).isNotEqualTo("/salas/999");
        assertThat(request("GET", caminho, null).body()).contains("Sala A");
        assertThat(request("GET", "/salas", null).body()).contains("Sala A");

        var atualizada = request("PUT", caminho, "{\"id\":999,\"nome\":\"Sala B\",\"capacidade\":40}");
        assertThat(atualizada.statusCode()).isEqualTo(200);
        assertThat(atualizada.body()).contains("Sala B", "\"capacidade\":40");
        assertThat(request("GET", caminho, null).body()).isEqualTo(atualizada.body());
        assertThat(repository.count()).isEqualTo(1);

        assertThat(request("DELETE", caminho, null).statusCode()).isEqualTo(204);
        assertThat(request("GET", caminho, null).statusCode()).isEqualTo(404);
        assertThat(repository.count()).isZero();
    }

    @Test
    void deveRejeitarDadosInvalidosSemAlterarBanco() throws Exception {
        for (String dados : new String[]{"{}", "{\"nome\":\"   \",\"capacidade\":10}",
                "{\"nome\":\"Sala\",\"capacidade\":0}", "{\"nome\":\"Sala\",\"capacidade\":-1}"}) {
            assertThat(request("POST", "/salas", dados).statusCode()).isEqualTo(400);
        }
        var criada = request("POST", "/salas", "{\"nome\":\"Original\",\"capacidade\":20}");
        String caminho = URI.create(criada.headers().firstValue("Location").orElseThrow()).getPath();
        assertThat(request("PUT", caminho, "{\"nome\":\"\",\"capacidade\":-1}").statusCode()).isEqualTo(400);
        assertThat(request("GET", caminho, null).body()).contains("Original", "\"capacidade\":20");
        assertThat(repository.count()).isEqualTo(1);
    }

    @Test
    void deveRetornar404ParaSalaInexistente() throws Exception {
        assertThat(request("GET", "/salas/999999", null).statusCode()).isEqualTo(404);
        assertThat(request("PUT", "/salas/999999", "{\"nome\":\"Sala\",\"capacidade\":10}").statusCode()).isEqualTo(404);
        assertThat(request("DELETE", "/salas/999999", null).statusCode()).isEqualTo(404);
        assertThat(repository.count()).isZero();
    }

    private HttpResponse<String> request(String method, String path, String body) throws Exception {
        return client.send(HttpRequest.newBuilder(URI.create("http://localhost:" + port + path))
                .header("Content-Type", "application/json")
                .method(method, body == null ? HttpRequest.BodyPublishers.noBody() : HttpRequest.BodyPublishers.ofString(body))
                .build(), HttpResponse.BodyHandlers.ofString());
    }
}
