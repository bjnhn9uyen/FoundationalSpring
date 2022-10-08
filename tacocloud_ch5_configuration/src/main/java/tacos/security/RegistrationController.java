package tacos.security;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import tacos.data.UserRepository;

@Controller
@RequestMapping("/register")
public class RegistrationController {

	private PasswordEncoder passwordEncoder;
	private UserRepository userRepo;

	@Autowired
	public RegistrationController(UserRepository userRepo, PasswordEncoder passwordEncoder) {
		this.userRepo = userRepo;
		this.passwordEncoder = passwordEncoder;
	}

	@ModelAttribute(name = "reg")
	public RegistrationForm registrationModel() {
		return new RegistrationForm();
	}

	@GetMapping
	public String registerForm() {
		return "registration";
	}

	@PostMapping
	public String processRegistration(@Valid @ModelAttribute("reg") RegistrationForm regForm, Errors errors) {
		if (errors.hasErrors()) {
			return "registration";
		}
		userRepo.save(regForm.toUser(passwordEncoder));
		return "redirect:/login";
	}

}
