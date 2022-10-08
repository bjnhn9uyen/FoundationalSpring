package tacos;

import lombok.Data;

// The @Data annotation is provided by Lombok and tells Lombok to generate (at runtime) all of getter
// and setter methods as well as any constructor with specific arguments (properties) as your requirement
@Data
public class Ingredient {

	private final String id;
	private final String name;
	private final Type type;

	public static enum Type {
		WRAP, PROTEIN, VEGGIES, CHEESE, SAUCE
	}

}