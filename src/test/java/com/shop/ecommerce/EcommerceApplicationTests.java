package com.shop.ecommerce;

import com.shop.ecommerce.config.SecurityConfig;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest(classes = {EcommerceApplication.class, SecurityConfig.class})
class EcommerceApplicationTests {

	@Test
	void contextLoads() {
	}

}
