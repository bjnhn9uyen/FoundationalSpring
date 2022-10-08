package tacos.web;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.SessionAttributes;

import tacos.Ingredient;
import tacos.Ingredient.Type;
import tacos.Order;
import tacos.Taco;
import tacos.data.IngredientRepository;
import tacos.data.TacoRepository;

// The class-level @SessionAttributes annotation specifies that any request attribute named “order” should be
// kept in session and available across multiple requests so we can create and add multiple tacos to the order
@Controller
@RequestMapping("/design")
@SessionAttributes("order")
public class DesignTacoController {

	private IngredientRepository ingredientRepo;
	private TacoRepository designRepo;

	// inject IngredientRepository and TacoRepository into DesignTacoController via the @Autowired annotation
	@Autowired
	public DesignTacoController(IngredientRepository ingredientRepo, TacoRepository designRepo) {
		this.ingredientRepo = ingredientRepo;
		this.designRepo = designRepo;
	}

	// instead of calling addAttribute method on the Model object in previous chapter,
	// we use @ModelAttribute annotated method to define that Taco object returned from the method
	// will be binding to the request attribute “taco” and be part of the Model in Spring MVC,
	// then you can have Spring MVC supply this object to the controller's methods
	// by using the @ModelAttribute annotation with the method's parameters
	@ModelAttribute(name = "taco")
	public Taco taco() {
		return new Taco();
	}

	@GetMapping
	public String showDesignForm(Model model) {
		List<Ingredient> ingredients = new ArrayList<>();
		ingredientRepo.findAll().forEach(i -> ingredients.add(i));

		Type[] types = Ingredient.Type.values();
		for (Type type : types) {
			model.addAttribute(type.toString().toLowerCase(), filterByType(ingredients, type));
		}
		return "design";
	}

	@PostMapping
	public String processDesign(@Valid Taco design, Errors errors, Order order) {
		if (errors.hasErrors()) {
			return "design";
		}

		// save the Taco design then add it to the Order that’s kept in the session
		Taco saved = designRepo.save(design);
		order.addDesign(saved); // Order object remains in the session until user submits the order form

		return "redirect:/orders/current";
	}

	private List<Ingredient> filterByType(List<Ingredient> ingredients, Type type) {
		return ingredients.stream().filter(x -> x.getType().equals(type)).collect(Collectors.toList());
	}

}
