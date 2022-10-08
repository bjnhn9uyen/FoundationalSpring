package tacos;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.io.File;
import java.util.List;
import java.util.concurrent.TimeUnit;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;

@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
public class DesignTacoControllerBrowserTest {

	private static ChromeDriver browser;

	@LocalServerPort
	private int port;

	@Autowired
	TestRestTemplate rest;

	@BeforeAll
	public static void openBrowser() {
		// Chrome driver works with chrome 105.x
		System.setProperty("webdriver.chrome.driver",
								new File("").getAbsolutePath()
														+ "/src/test/chrome-browser-test/chromedriver.exe");
		browser = new ChromeDriver();
		browser.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);
	}

	@AfterAll
	public static void closeBrowser() {
		browser.quit();
	}

	@Test
	// @Disabled("Need to get around authentication in this test")
	public void testDesignATacoPage() throws Exception {
		browser.get("http://localhost:" + port + "/design");

		// authentication
		browser.findElementByName("username").sendKeys("habuma00");
		browser.findElementByName("password").sendKeys("aA@00000");
		browser.findElementByCssSelector("form#loginForm").submit();

		List<WebElement> ingredientGroups = browser.findElementsByClassName("ingredient-group");
		assertEquals(5, ingredientGroups.size());

		WebElement wrapGroup = ingredientGroups.get(0);
		List<WebElement> wraps = wrapGroup.findElements(By.tagName("div"));
		assertEquals(2, wraps.size());
		assertIngredient(wrapGroup, 0, "COTO", "Corn Tortilla");
		assertIngredient(wrapGroup, 1, "FLTO", "Flour Tortilla");

		WebElement proteinGroup = ingredientGroups.get(1);
		List<WebElement> proteins = proteinGroup.findElements(By.tagName("div"));
		assertEquals(2, proteins.size());
		assertIngredient(proteinGroup, 0, "CARN", "Carnitas");
		assertIngredient(proteinGroup, 1, "GRBF", "Ground Beef");
	}

	private void assertIngredient(WebElement ingredientGroup, int ingredientIdx, String id, String name) {
		List<WebElement> proteins = ingredientGroup.findElements(By.tagName("div"));
		WebElement ingredient = proteins.get(ingredientIdx);
		assertEquals(id, ingredient.findElement(By.tagName("input")).getAttribute("value"));
		assertEquals(name, ingredient.findElement(By.tagName("span")).getText());
	}

}
