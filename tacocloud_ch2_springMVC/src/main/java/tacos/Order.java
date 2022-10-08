package tacos;

import javax.validation.constraints.Digits;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;

import org.hibernate.validator.constraints.CreditCardNumber;

import lombok.Data;

// The @Data annotation is provided by Lombok and tells Lombok to generate (at runtime) all of getter
// and setter methods as well as any constructor with specific arguments (properties) as your requirement
@Data
public class Order {

	@NotBlank(message = "Name is required")
	private String deliveryName;

	@NotBlank(message = "Street is required")
	private String deliveryStreet;

	@NotBlank(message = "City is required")
	private String deliveryCity;

	@NotBlank(message = "State is required")
	private String deliveryState;

	@NotBlank(message = "ZIP code is required")
	private String deliveryZip;

	// The @CreditCardNumber ensures that the ccNumber property value is a valid credit card number,
	// but it doesn’t guarantee that the credit card number is actually assigned to a real account
	@CreditCardNumber(message = "Not a valid credit card number")
	private String ccNumber;

	@Pattern(regexp = "^(0[1-9]|1[0-2])([\\/])([1-9][0-9])$", message = "Must be formatted MM/YY")
	private String ccExpiration;

	// The @Digits ensures that the ccCVV value contains exactly three numeric digits
	@Digits(integer = 3, fraction = 0, message = "Invalid CVV")
	private String ccCVV;

}
