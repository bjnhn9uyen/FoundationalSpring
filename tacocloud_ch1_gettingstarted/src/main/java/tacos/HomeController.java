package tacos;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class HomeController {

	@GetMapping("/") // handle requests for the root path /
	public String home() {
		return "home"; // forward to the home-page view without populating any model data
		// this “home” value is interpreted as the logical name of a view (home.html)
	}

}
