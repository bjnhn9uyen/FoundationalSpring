package tacos;

import java.util.HashMap;
import java.util.Map;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import lombok.Data;

// The @ConfigurationProperties annotation defines that the DiscountCodeProps bean (annotated with @Component)
// is a properties holder and it holds the ‘taco.discount.codes’ property, you can change it to any value
// by setting the ‘taco.discount.codes’ property in the application.yml file
@Component
@ConfigurationProperties(prefix = "taco.discount")
@Data
public class DiscountCodeProps {

	private Map<String, Integer> codes = new HashMap<>(); // initial value
	// if the pair values of the ‘taco.discount.codes’ property isn’t set in the application.yml file,
	// then the property uses an empty HashMap as default

}
