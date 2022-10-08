package tacos.web;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.bind.support.SessionStatus;

import lombok.extern.slf4j.Slf4j;
import tacos.Order;
import tacos.User;
import tacos.data.OrderRepository;

@Slf4j
@Controller
@RequestMapping("/orders")
@SessionAttributes("order")
public class OrderController {

	private OrderRepository orderRepo;
//	private UserRepository userRepository;

	@Autowired
	public OrderController(OrderRepository orderRepo) {
		this.orderRepo = orderRepo;
	}

	@GetMapping("/current")
	public String orderForm(@AuthenticationPrincipal User user, @ModelAttribute Order order) {
		// get current logged in user’s information and fill them into the inputs
		if (order.getDeliveryName() == null) {
			order.setDeliveryName(user.getFullname());
		}
		if (order.getDeliveryStreet() == null) {
			order.setDeliveryStreet(user.getStreet());
		}
		if (order.getDeliveryCity() == null) {
			order.setDeliveryCity(user.getCity());
		}
		if (order.getDeliveryState() == null) {
			order.setDeliveryState(user.getState());
		}
		if (order.getDeliveryZip() == null) {
			order.setDeliveryZip(user.getZip());
		}
		return "orderForm";
	}

	/**
	 * The processOrder() method will need to be modified to determine who the authenticated user is and to
	 * call setUser() on the Order object to connect the order with the user. There are several ways to
	 * determine who the user is:
	 * 
	 * ► Inject a Principal object into the controller method
	 * 
	 * ► Inject an Authentication object into the controller method
	 * 
	 * ► Use SecurityContextHolder to get at the security context
	 * 
	 * ► Use an @AuthenticationPrincipal annotated method
	 */

	/**
	 * Modifying processOrder method to accept a Principal as a parameter, then use the principal name to look
	 * up the user from a UserRepository
	 */
//	@PostMapping
//	public String processOrder(@Valid Order order, Errors errors, SessionStatus sessionStatus,
//							Principal principal) {
//		if (errors.hasErrors()) {
//			return "orderForm";
//		}
//		User user = userRepository.findByUsername(principal.getName());
//		order.setUser(user);
//		orderRepo.save(order);
//		sessionStatus.setComplete(); // reset the session
//		log.info("Order submitted: " + order);
//		return "redirect:/";
//	}

	/**
	 * Using Principal object works fine, but it litters code that’s otherwise unrelated to security with
	 * security code. You can trim down some of the security-specific code by modifying processOrder() to
	 * accept an Authentication object as a parameter instead of a Principal. With the Authentication in hand,
	 * you can get the principal object which, in this case, is a User. Note that getPrincipal() returns a
	 * java.util.Object, so you need to cast it to User
	 */
//	@PostMapping
//	public String processOrder(@Valid Order order, Errors errors, SessionStatus sessionStatus,
//							Authentication authentication) {
//		if (errors.hasErrors()) {
//			return "orderForm";
//		}
//		User user = (User) authentication.getPrincipal();
//		order.setUser(user);
//		orderRepo.save(order);
//		sessionStatus.setComplete(); // reset the session
//		log.info("Order submitted: " + order);
//		return "redirect:/";
//	}

	/**
	 * Perhaps the cleanest solution of all, what’s nice about @AuthenticationPrincipal is that it doesn’t
	 * require a cast, and it limits the security-specific code to the annotation itself
	 */
	@PostMapping
	public String processOrder(@Valid Order order, Errors errors, SessionStatus sessionStatus,
							@AuthenticationPrincipal User user) {
		if (errors.hasErrors()) {
			return "orderForm";
		}
		order.setUser(user);
		orderRepo.save(order);
		sessionStatus.setComplete(); // reset the session
		log.info("Order submitted: " + order);
		return "redirect:/";
	}

	/**
	 * There’s one other way of identifying who the authenticated user is, although it’s a bit messy in the
	 * sense that it’s very heavy with security-specific code. You can obtain an Authentication object from
	 * the security context and then request its principal. Although this snippet is thick with
	 * security-specific code, it has one advantage over the other approaches described: it can be used
	 * anywhere in the application, not only in a controller’s handler methods
	 */
//	@PostMapping
//	public String processOrder(@Valid Order order, Errors errors, SessionStatus sessionStatus) {
//		if (errors.hasErrors()) {
//			return "orderForm";
//		}
//		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
//		User user = (User) authentication.getPrincipal();
//		order.setUser(user);
//		orderRepo.save(order);
//		sessionStatus.setComplete(); // reset the session
//		log.info("Order submitted: " + order);
//		return "redirect:/";
//	}

}
