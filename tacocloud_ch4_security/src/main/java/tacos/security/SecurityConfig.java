package tacos.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {

	@Autowired
	private UserDetailsService userDetailsService;

	@Bean
	public PasswordEncoder encoder() {
		// StandardPasswordEncoder is deprecated, because it uses digest based password encoding
		// and that is not considered secure, BCryptPasswordEncoder is a better choice in terms of security
		return new BCryptPasswordEncoder();
	}

	// this method will be invoked every time you login
	@Override
	protected void configure(AuthenticationManagerBuilder auth) throws Exception {
		auth.userDetailsService(userDetailsService).passwordEncoder(encoder());
	}

	// intercepting requests to ensure that the User has proper authority
	@Override
	protected void configure(HttpSecurity http) throws Exception {
		// security rules declared first take precedence over those declared lower down
		http.authorizeRequests()
								.antMatchers("/design", "/orders", "/orders/current")
								.access("hasRole('ROLE_USER')")
								.antMatchers("/", "/**")
								.access("permitAll")

								// allow users with ROLE_USER authority to create new tacos on Tuesdays
//								.access("hasRole('ROLE_USER') && T(java.util.Calendar).getInstance().get("
//														+ "T(java.util.Calendar).DAY_OF_WEEK) == "
//														+ "T(java.util.Calendar).TUESDAY")

								// to replace the built-in login page, you first need to tell Spring Security
								// what path your custom login page will be at, then you need to provide a
								// controller that handles requests at this path
								.and()
								.formLogin()
								.loginPage("/login")

								// by default, Spring Security listens for login requests at “/login”
								// and expects that the username and password field be named ‘username’
								// and ‘password’, but we can customize the path and field names
//								.loginProcessingUrl("/authenticate")
//								.usernameParameter("user")
//								.passwordParameter("pwd")

								// customize a default successful login page using defaultSuccessUrl method,
								// and passing value ‘true’ as a second parameter to the method to force
								// the User to the specific page after login if they were navigating elsewhere
								// prior to logging in (if this line enabled, the JUnit test will fail)
//								.defaultSuccessUrl("/", true)

								// When User clicks the log-out button, their session will be cleared.
								// By default, they’ll be redirected to the login page, but we can
								// customize that using logoutSuccessUrl method to send them to the home page
								.and()
								.logout()
								.logoutSuccessUrl("/")

								// make H2-Console non-secured (for debug purposes)
								.and()
								.csrf()
								.ignoringAntMatchers("/h2-console/**")

								// allow pages to be loaded in frames from the same origin (for H2-Console)
								.and()
								.headers()
								.frameOptions()
								.sameOrigin();
	}

}
