package tacos.web;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import tacos.DiscountCodeProps;

@Controller
@RequestMapping("/discounts")
public class DiscountController {

	// DiscountCodeProps bean can be injected into any other bean that needs properties it’s holding
	private DiscountCodeProps discountProps;

	@Autowired
	public DiscountController(DiscountCodeProps discountProps) {
		this.discountProps = discountProps;
	}

	@GetMapping
	public String displayDiscountCodes(Model model) {

		Map<String, Integer> codes = discountProps.getCodes(); // get from the application.yml file
		model.addAttribute("codes", codes);

		return "discountList";
	}

}
