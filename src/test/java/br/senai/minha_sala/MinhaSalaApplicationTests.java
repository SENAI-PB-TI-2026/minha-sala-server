package br.senai.minha_sala;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest(properties = "spring.datasource.url=jdbc:h2:mem:context-tests")
class MinhaSalaApplicationTests {

	/** Verifica que o contexto Spring inicia com o banco em memória. */
	@Test
	void contextLoads() {
	}

}
