package tacos;

import java.util.List;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import lombok.Data;

// The @Data annotation is provided by Lombok and tells Lombok to generate (at runtime) all of getter
// and setter methods as well as any constructor with specific arguments (properties) as your requirement
@Data
public class Taco {

	@NotNull
	@Size(min = 5, message = "Name must be at least 5 characters long")
	private String name;
	@Size(min = 1, message = "You must choose at least 1 ingredient")
	private List<String> ingredients;

}
