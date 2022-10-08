package tacos.web;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

import tacos.Ingredient;
import tacos.data.IngredientRepository;

@Component
public class IngredientByIdConverter implements Converter<String, Ingredient> {

	private IngredientRepository ingredientRepo;

	// inject IngredientRepository via the @Autowired annotation
	@Autowired
	public IngredientByIdConverter(IngredientRepository ingredientRepo) {
		this.ingredientRepo = ingredientRepo;
	}

	// because the IngredientByIdConverter class is registered as Spring bean via @Component annotation,
	// when Spring needs to make a convert, it will find for a matching conversion strategy with
	// the target (Ingredient) in the Spring application context, and the IngredientByIdConverter is called
	// to convert every String id (from checked check-boxes values in the design form) to a Ingredient object
	@Override
	public Ingredient convert(String id) {
		return ingredientRepo.findById(id);
	}

}
