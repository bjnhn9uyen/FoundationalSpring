package tacos.web;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;
import org.springframework.validation.annotation.Validated;

import lombok.Data;

// The @ConfigurationProperties annotation specifies that the OrderProps bean (annotated with @Component)
// is a properties holder and it holds the “taco.orders.pageSize” property, you can change it to any value
// by setting the ‘taco.orders.pageSize’ property in the application.yml file
@Component
@ConfigurationProperties(prefix = "taco.orders")
@Validated
@Data
public class OrderProps {

	// @Min and @Max go along with the class-level @Validated annotation,
	// if the ‘taco.orders.pageSize’ property in the application.yml file violates these constraints,
	// the error message will be thrown out
	@Min(value = 5, message = "must be between 5 and 25")
	@Max(value = 25, message = "must be between 5 and 25")
	private int pageSize = 20; // initial value
	// this is default value if the ‘taco.orders.pageSize’ property isn’t set in the application.yml file

}
