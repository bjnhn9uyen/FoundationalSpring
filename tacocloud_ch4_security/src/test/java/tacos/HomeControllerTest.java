package tacos;

import static org.hamcrest.Matchers.containsString;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.web.servlet.MockMvc;

import tacos.data.IngredientRepository;
import tacos.data.OrderRepository;
import tacos.data.TacoRepository;
import tacos.data.UserRepository;

@WebMvcTest
public class HomeControllerTest {

	@Autowired
	private MockMvc mockMvc; // inject MockMvc

	// most of these mocks are here to avoid auto-wiring issues,
	// they just need to exist so auto-wiring can take place
	@MockBean
	private IngredientRepository ingredientRepository;

	@MockBean
	private TacoRepository designRepository;

	@MockBean
	private OrderRepository orderRepository;

	@MockBean
	private UserRepository userRepository;

	@MockBean
	private PasswordEncoder passwordEncoder;

	@Test
	public void testHomePage() throws Exception {
		mockMvc.perform(get("/"))
								.andExpect(status().isOk()) // expect HTTP 200
								.andExpect(view().name("home")) // expect home view
								.andExpect(content().string(containsString("Welcome to..."))); // expect text
	}

}
