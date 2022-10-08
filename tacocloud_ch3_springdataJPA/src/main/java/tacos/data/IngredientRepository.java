package tacos.data;

import org.springframework.data.repository.CrudRepository;

import tacos.Ingredient;

public interface IngredientRepository extends CrudRepository<Ingredient, String> {
	/**
	 * In the JDBC versions of the repositories, you explicitly declared the methods you wanted the repository
	 * to provide. But with Spring Data JPA, you can extend the CrudRepository interface instead. Notice that
	 * it’s parameterized, with the first parameter being the entity type the repository is to persist, and
	 * the second parameter being the type of the entity id property.
	 * 
	 * There’s no need to write an implementation! When the application starts, Spring Data JPA automatically
	 * generates an implementation on the fly. Just inject them into the controller like you did with the
	 * JDBC-based implementations.
	 */
}
