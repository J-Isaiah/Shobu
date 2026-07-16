package com.shobu;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest(properties = {
		"app.players.isaiah-id=00000000-0000-0000-0000-000000000001",
		"app.players.julia-id=00000000-0000-0000-0000-000000000002"
})
class BackendApplicationTests {

	@Test
	void contextLoads() {
	}
}