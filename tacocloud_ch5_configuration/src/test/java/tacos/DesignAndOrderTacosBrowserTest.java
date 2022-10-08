package tacos;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.htmlunit.HtmlUnitDriver;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;;

@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
public class DesignAndOrderTacosBrowserTest {

	private static HtmlUnitDriver browser;

	@LocalServerPort
	private int port;

	@Autowired
	TestRestTemplate rest;

	@BeforeAll
	public static void setup() {
		browser = new HtmlUnitDriver();
		browser.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);
	}

	@AfterAll
	public static void closeBrowser() {
		browser.quit();
	}

	@Test
	public void testDesignATacoPage_HappyPath() throws Exception {
		doInvalidRegistration();
		browser.get(homePageUrl());
		clickDesignATaco();
		assertLandedOnLoginPage();
		doRegistration("testuser", "aA@00000", "aA@00000");
		assertLandedOnLoginPage();
		doLogin("testuser", "aA@00000");
		assertDesignPageElements();
		buildAndSubmitATaco("Basic Taco", "FLTO", "GRBF", "CHED", "TMTO", "SLSA");
		clickBuildAnotherTaco();
		buildAndSubmitATaco("Another Taco", "COTO", "CARN", "JACK", "LETC", "SRCR");
		fillInAndSubmitOrderForm();
		assertEquals(homePageUrl(), browser.getCurrentUrl());
		doLogout();
	}

	@Test
	public void testDesignATacoPage_EmptyOrderInfo() throws Exception {
		browser.get(homePageUrl());
		clickDesignATaco();
		assertLandedOnLoginPage();
		doRegistration("testuser2", "aA@00000", "aA@00000");
		doLogin("testuser2", "aA@00000");
		assertDesignPageElements();
		buildAndSubmitATaco("Basic Taco", "FLTO", "GRBF", "CHED", "TMTO", "SLSA");
		submitEmptyOrderForm();
		fillInAndSubmitOrderForm();
		assertEquals(homePageUrl(), browser.getCurrentUrl());
		doLogout();
	}

	@Test
	public void testDesignATacoPage_InvalidOrderInfo() throws Exception {
		browser.get(homePageUrl());
		clickDesignATaco();
		assertLandedOnLoginPage();
		doRegistration("testuser3", "aA@00000", "aA@00000");
		doLogin("testuser3", "aA@00000");
		assertDesignPageElements();
		buildAndSubmitATaco("Basic Taco", "FLTO", "GRBF", "CHED", "TMTO", "SLSA");
		submitInvalidOrderForm();
		fillInAndSubmitOrderForm();
		assertEquals(homePageUrl(), browser.getCurrentUrl());
		doLogout();
	}

	/*
	 * Browser test action methods
	 */
	private void buildAndSubmitATaco(String name, String... ingredients) {
		assertDesignPageElements();

		for (String ingredient : ingredients) {
			browser.findElementByCssSelector("input[value='" + ingredient + "']").click();
		}
		browser.findElementByCssSelector("input#name").sendKeys(name);
		browser.findElementByCssSelector("form#tacoForm").submit();
	}

	private void assertLandedOnLoginPage() {
		assertEquals(loginPageUrl(), browser.getCurrentUrl());
	}

	private void doInvalidRegistration() {
		browser.get(registrationPageUrl());
		fillField("input#username", "_");
		fillField("input#password", " ");
		fillField("input#pwConfirm", "0");
		fillField("input#phone", "0");
		browser.findElementByCssSelector("form#registerForm").submit();

		assertEquals(registrationPageUrl(), browser.getCurrentUrl());

		List<String> validationErrors = getValidationErrorTexts();
		assertEquals(4, validationErrors.size());
		assertTrue(validationErrors.contains("Username must be 8 to 20 chars long; alphanumeric chars; "
								+ "underscore and dot are allowed; "
								+ "underscore and dot can not be at the end "
								+ "or start of a username or next to each other, "
								+ "or used multiple times in a row"));
		assertTrue(validationErrors.contains("Password must be at least 8 chars long, "
								+ "contains at least one digit, one lower alpha char, "
								+ "one upper alpha char, one special char, "
								+ "and not contains whitespace"));
		assertTrue(validationErrors.contains("The password confirmation does not match"));
		assertTrue(validationErrors.contains("Phone must be 10 to 15 digits and not start with 0"));
	}

	private void doRegistration(String username, String password, String pwConfirm) {
		browser.findElementByLinkText("here").click();
		assertEquals(registrationPageUrl(), browser.getCurrentUrl());
		browser.findElementByName("username").sendKeys(username);
		browser.findElementByName("password").sendKeys(password);
		browser.findElementByName("pwConfirm").sendKeys(password);
		browser.findElementByName("fullname").sendKeys("Test McTest");
		browser.findElementByName("street").sendKeys("1234 Test Street");
		browser.findElementByName("city").sendKeys("Testville");
		browser.findElementByName("state").sendKeys("TX");
		browser.findElementByName("zip").sendKeys("12345");
		browser.findElementByName("phone").sendKeys("1231231234");
		browser.findElementByCssSelector("form#registerForm").submit();
	}

	private void doLogin(String username, String password) {
		browser.findElementByName("username").sendKeys(username);
		browser.findElementByName("password").sendKeys(password);
		browser.findElementByCssSelector("form#loginForm").submit();
	}

	private void doLogout() {
		WebElement logoutForm = browser.findElementByCssSelector("form#logoutForm");
		if (logoutForm != null) {
			logoutForm.submit();
		}
	}

	private void assertDesignPageElements() {
		assertEquals(designPageUrl(), browser.getCurrentUrl());
		List<WebElement> ingredientGroups = browser.findElementsByClassName("ingredient-group");
		assertEquals(5, ingredientGroups.size());

		WebElement wrapGroup = browser.findElementByCssSelector("div.ingredient-group#wraps");
		List<WebElement> wraps = wrapGroup.findElements(By.tagName("div"));
		assertEquals(2, wraps.size());
		assertIngredient(wrapGroup, 0, "COTO", "Corn Tortilla");
		assertIngredient(wrapGroup, 1, "FLTO", "Flour Tortilla");

		WebElement proteinGroup = browser.findElementByCssSelector("div.ingredient-group#proteins");
		List<WebElement> proteins = proteinGroup.findElements(By.tagName("div"));
		assertEquals(2, proteins.size());
		assertIngredient(proteinGroup, 0, "CARN", "Carnitas");
		assertIngredient(proteinGroup, 1, "GRBF", "Ground Beef");

		WebElement cheeseGroup = browser.findElementByCssSelector("div.ingredient-group#cheeses");
		List<WebElement> cheeses = proteinGroup.findElements(By.tagName("div"));
		assertEquals(2, cheeses.size());
		assertIngredient(cheeseGroup, 0, "CHED", "Cheddar");
		assertIngredient(cheeseGroup, 1, "JACK", "Monterrey Jack");

		WebElement veggieGroup = browser.findElementByCssSelector("div.ingredient-group#veggies");
		List<WebElement> veggies = proteinGroup.findElements(By.tagName("div"));
		assertEquals(2, veggies.size());
		assertIngredient(veggieGroup, 0, "LETC", "Lettuce");
		assertIngredient(veggieGroup, 1, "TMTO", "Diced Tomatoes");

		WebElement sauceGroup = browser.findElementByCssSelector("div.ingredient-group#sauces");
		List<WebElement> sauces = proteinGroup.findElements(By.tagName("div"));
		assertEquals(2, sauces.size());
		assertIngredient(sauceGroup, 0, "SLSA", "Salsa");
		assertIngredient(sauceGroup, 1, "SRCR", "Sour Cream");
	}

	private void fillInAndSubmitOrderForm() {
		assertTrue(browser.getCurrentUrl().startsWith(orderDetailsPageUrl()));
		fillField("input#deliveryName", "Ima Hungry");
		fillField("input#deliveryStreet", "1234 Culinary Blvd.");
		fillField("input#deliveryCity", "Foodsville");
		fillField("input#deliveryState", "CO");
		fillField("input#deliveryZip", "81019");
		fillField("input#ccNumber", "4111111111111111");
		fillField("input#ccExpiration", "10/19");
		fillField("input#ccCVV", "123");
		browser.findElementByCssSelector("form#orderForm").submit();
	}

	private void submitEmptyOrderForm() {
		assertEquals(currentOrderDetailsPageUrl(), browser.getCurrentUrl());
		// clear fields automatically populated from user profile
		fillField("input#deliveryName", "");
		fillField("input#deliveryStreet", "");
		fillField("input#deliveryCity", "");
		fillField("input#deliveryState", "");
		fillField("input#deliveryZip", "");
		browser.findElementByCssSelector("form#orderForm").submit();

		assertEquals(orderDetailsPageUrl(), browser.getCurrentUrl());

		List<String> validationErrors = getValidationErrorTexts();
		assertEquals(9, validationErrors.size());
		assertTrue(validationErrors.contains("Please correct the problems below and resubmit."));
		assertTrue(validationErrors.contains("Delivery name is required"));
		assertTrue(validationErrors.contains("Street is required"));
		assertTrue(validationErrors.contains("City is required"));
		assertTrue(validationErrors.contains("State is required"));
		assertTrue(validationErrors.contains("ZIP code is required"));
		assertTrue(validationErrors.contains("Not a valid credit card number"));
		assertTrue(validationErrors.contains("Must be formatted MM/YY"));
		assertTrue(validationErrors.contains("Invalid CVV"));
	}

	private List<String> getValidationErrorTexts() {
		List<WebElement> validationErrorElements = browser.findElementsByClassName("validationError");
		List<String> validationErrors = validationErrorElements.stream()
								.map(el -> el.getText())
								.collect(Collectors.toList());
		return validationErrors;
	}

	private void submitInvalidOrderForm() {
		assertTrue(browser.getCurrentUrl().startsWith(orderDetailsPageUrl()));
		fillField("input#deliveryName", "I");
		fillField("input#deliveryStreet", "1");
		fillField("input#deliveryCity", "F");
		fillField("input#deliveryState", "C");
		fillField("input#deliveryZip", "8");
		fillField("input#ccNumber", "1234432112344322");
		fillField("input#ccExpiration", "14/91");
		fillField("input#ccCVV", "1234");
		browser.findElementByCssSelector("form#orderForm").submit();

		assertEquals(orderDetailsPageUrl(), browser.getCurrentUrl());

		List<String> validationErrors = getValidationErrorTexts();
		assertEquals(4, validationErrors.size());
		assertTrue(validationErrors.contains("Please correct the problems below and resubmit."));
		assertTrue(validationErrors.contains("Not a valid credit card number"));
		assertTrue(validationErrors.contains("Must be formatted MM/YY"));
		assertTrue(validationErrors.contains("Invalid CVV"));
	}

	private void fillField(String fieldName, String value) {
		WebElement field = browser.findElementByCssSelector(fieldName);
		field.clear();
		field.sendKeys(value);
	}

	private void assertIngredient(WebElement ingredientGroup, int ingredientIdx, String id, String name) {
		List<WebElement> proteins = ingredientGroup.findElements(By.tagName("div"));
		WebElement ingredient = proteins.get(ingredientIdx);
		assertEquals(id, ingredient.findElement(By.tagName("input")).getAttribute("value"));
		assertEquals(name, ingredient.findElement(By.tagName("span")).getText());
	}

	private void clickDesignATaco() {
		assertEquals(homePageUrl(), browser.getCurrentUrl());
		browser.findElementByCssSelector("a[id='design']").click();
	}

	private void clickBuildAnotherTaco() {
		assertTrue(browser.getCurrentUrl().startsWith(orderDetailsPageUrl()));
		browser.findElementByCssSelector("a[id='another']").click();
	}

	/*
	 * URL helper methods
	 */
	private String loginPageUrl() {
		return homePageUrl() + "login";
	}

	private String registrationPageUrl() {
		return homePageUrl() + "register";
	}

	private String designPageUrl() {
		return homePageUrl() + "design";
	}

	private String homePageUrl() {
		return "http://localhost:" + port + "/";
	}

	private String orderDetailsPageUrl() {
		return homePageUrl() + "orders";
	}

	private String currentOrderDetailsPageUrl() {
		return homePageUrl() + "orders/current";
	}

}
