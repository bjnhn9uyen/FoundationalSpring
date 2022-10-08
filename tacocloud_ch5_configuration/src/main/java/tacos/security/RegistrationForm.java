package tacos.security;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;

import org.springframework.security.crypto.password.PasswordEncoder;

import lombok.Data;
import tacos.User;

@Data
public class RegistrationForm {

	@Pattern(regexp = "^"

							+ "(?=.{8,20}$)"// at least 8-20 characters long
							+ "(?![_.])" // no _ or . at the beginning
							+ "(?!.*[_.]{2})" // no __ or _. or ._ or .. inside
							+ "[a-zA-Z0-9._]+" // allow characters, underscores, and dots
							+ "(?<![_.])" // no _ or . at the end
							+ "$", message = "Username must be 8 to 20 chars long; alphanumeric chars; "
													+ "underscore and dot are allowed; "
													+ "underscore and dot can not be at the end "
													+ "or start of a username or next to each other, "
													+ "or used multiple times in a row")
	private String username;

	@Pattern(regexp = "^"

							+ "(?=.*[0-9])" // contain at least one digit
							+ "(?=.*[a-z])" // contain at least one lower case letter
							+ "(?=.*[A-Z])" // contain at least one upper case letter
							+ "(?=.*[@#$%^&+=])" // contain at least one special character
							+ "(?=\\S+$)" // no whitespace allowed in the entire string
							+ ".{8,}" // at least 8 chars long
							+ "$", message = "Password must be at least 8 chars long, "
													+ "contains at least one digit, one lower alpha char, "
													+ "one upper alpha char, one special char, "
													+ "and not contains whitespace")
	private String password;

	@NotBlank(message = "The password confirmation does not match")
	private String pwConfirm;

	private String fullname;
	private String street;
	private String city;
	private String state;
	private String zip;

	@Pattern(regexp = "^[1-9][0-9]{9,14}$", message = "Phone must be 10 to 15 digits and not start with 0")
	private String phone;

	public User toUser(PasswordEncoder passwordEncoder) {
		return new User(username, passwordEncoder.encode(password), fullname, street, city, state, zip,
								phone);
	}

	// override Lombok’s setter method to compare ‘password’ and ‘pwConfirm’ received from registration form
	public void setPwConfirm(String pwConfirm) {
		if (!pwConfirm.equals(password)) {
			this.pwConfirm = ""; // set to null String for triggering @NotBlank error message
		} else {
			this.pwConfirm = pwConfirm;
		}
	}
}
