package tacos.web;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import javax.validation.Valid;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import lombok.extern.slf4j.Slf4j;
import tacos.Ingredient;
import tacos.Ingredient.Type;
import tacos.Taco;

// The @Slf4j is a Lombok-provided annotation that, at runtime, will automatically
// generate a SLF4J Logger in the class (SLF4J stands for “Simple Logging Facade for Java”).
// The @Controller annotation marks the class as a candidate for component scanning, so that Spring will
// discover it and automatically create an instance of it as a bean in the Spring application context.
// The @RequestMapping specifies that the controller will handle requests whose path begins with “/design”
@Slf4j
@Controller
@RequestMapping("/design")
public class DesignTacoController {

	// The method-level @GetMapping paired with the class-level @RequestMapping, specifies that
	// when a GET request is received for “/design”, this method will be called to handle the request
	@GetMapping
	public String showDesignForm(Model model) {
		// construct a list of Ingredient objects (hard-coded)
		List<Ingredient> ingredients = Arrays.asList(new Ingredient("FLTO", "Flour Tortilla", Type.WRAP),
								new Ingredient("COTO", "Corn Tortilla", Type.WRAP),
								new Ingredient("GRBF", "Ground Beef", Type.PROTEIN),
								new Ingredient("CARN", "Carnitas", Type.PROTEIN),
								new Ingredient("TMTO", "Diced Tomatoes", Type.VEGGIES),
								new Ingredient("LETC", "Lettuce", Type.VEGGIES),
								new Ingredient("CHED", "Cheddar", Type.CHEESE),
								new Ingredient("JACK", "Monterrey Jack", Type.CHEESE),
								new Ingredient("SLSA", "Salsa", Type.SAUCE),
								new Ingredient("SRCR", "Sour Cream", Type.SAUCE));

		// assign enum of Ingredients named “Type” to an array for iteration
		Type[] types = Ingredient.Type.values();
		for (Type type : types) {
			// corresponding to each Ingredient type, we have a filtered list of Ingredients by type,
			// and then add as an attribute to the Model object (these attributes for check boxes)
			model.addAttribute(type.toString().toLowerCase(), filterByType(ingredients, type));
		}
		// add Taco object to the Model and bind it to request attribute “taco”
		model.addAttribute("taco", new Taco());
		return "design"; // forward to the view (design.html)
	}

	// Use @PostMapping to handle POST requests for taco design submissions.
	// In the design.html file, we have: <form method="POST" th:object="${taco}">
	@PostMapping
	public String processDesign(@Valid Taco design, Errors errors) {

		// To validate a submitted taco, you need to add the @Valid annotation to the Taco argument
		// If there are any validation errors, the details of errors will be captured in an Errors object
		if (errors.hasErrors()) {
			return "design"; // forward
		}

		// For now, we use SLF4J Logger to log the taco design that’s submitted.
		// In the next chapter, we will save it into the database instead of using SLF4J Logger
		log.info("Processing design: " + design);
		return "redirect:/orders/current"; // redirect
	}

	private List<Ingredient> filterByType(List<Ingredient> ingredients, Type type) {
		// return a filtered list of Ingredients corresponding to the type passed into the method
		return ingredients.stream().filter(x -> x.getType().equals(type)).collect(Collectors.toList());
	}

}
