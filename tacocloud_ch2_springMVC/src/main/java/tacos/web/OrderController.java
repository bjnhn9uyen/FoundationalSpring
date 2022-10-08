package tacos.web;

import javax.validation.Valid;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import lombok.extern.slf4j.Slf4j;
import tacos.Order;

// The @Slf4j is a Lombok-provided annotation that, at runtime, will automatically
// generate a SLF4J Logger in the class (SLF4J stands for “Simple Logging Facade for Java”).
// The @Controller annotation marks the class as a candidate for component scanning, so that Spring will
// discover it and automatically create an instance of it as a bean in the Spring application context
// The @RequestMapping specifies that the controller will handle requests whose path begins with “/orders”
@Slf4j
@Controller
@RequestMapping("/orders")
public class OrderController {

	// The method-level @GetMapping paired with the class-level @RequestMapping, specifies that
	// when a GET request is received for “/orders/current”, this method will be called to handle the request
	@GetMapping("/current")
	public String orderForm(Model model) {
		model.addAttribute("order", new Order());
		return "orderForm"; // forward
	}

	// Use @PostMapping to handle POST requests for order submissions.
	// In the orderForm.html file, we have: <form method="POST" th:action="@{/orders}" th:object="${order}">
	@PostMapping
	public String processOrder(@Valid Order order, Errors errors) {

		// To validate a submitted order, you need to add the @Valid annotation to the Order argument
		// If there are any validation errors, the details of errors will be captured in an Errors object
		if (errors.hasErrors()) {
			return "orderForm"; // forward
		}

		// For now, we use SLF4J Logger to log the order that’s submitted.
		// In the next chapter, we will save the order into the database instead of using SLF4J Logger
		log.info("Order submitted: " + order);
		return "redirect:/"; // redirect
	}

}
