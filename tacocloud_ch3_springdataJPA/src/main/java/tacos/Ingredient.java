package tacos;

import javax.persistence.Entity;
import javax.persistence.Id;

import lombok.AccessLevel;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;

// The Lombok @Data annotation implicitly create required arguments constructors at runtime,
// but when a @NoArgsConstructor annotation is used, required arguments constructors get removed.
// The explicit @RequiredArgsConstructor annotation ensures that you’ll still have a required arguments
// constructor in addition to the private no-arguments constructor
@Data
@NoArgsConstructor(access = AccessLevel.PRIVATE, force = true)
@RequiredArgsConstructor
@Entity
public class Ingredient {

	@Id
	private final String id;

	private final String name;

	private final Type type;

	public static enum Type {
		WRAP, PROTEIN, VEGGIES, CHEESE, SAUCE
	}

}