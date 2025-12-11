package helpers;

import java.time.Duration;
import java.util.Arrays;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.PointerInput;
import org.openqa.selenium.interactions.Sequence;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import io.appium.java_client.AppiumBy;
import io.appium.java_client.android.AndroidDriver;
import io.appium.java_client.android.nativekey.AndroidKey;
import io.appium.java_client.android.nativekey.KeyEvent;


/**
 * Helper class for interacting with Android devices in Appium. Provides
 * reusable methods for clicks, typing, swipes, scrolling, and key presses.
 */
public class DeviceActions {

	private final AndroidDriver driver;
	private final WebDriverWait wait;

	public DeviceActions(AndroidDriver driver, int waitSeconds) {
		this.driver = driver;
		this.wait = new WebDriverWait(driver, Duration.ofSeconds(waitSeconds));
	}

	/** ===================== CLICK ACTIONS ===================== */
	
	/** * Click element using the full UI Automator selector string. 
     * Uses WebDriverWait for stability (best practice).
     */
    public void clickByUiAutomatorText(String uiSelectorString) {
        try {
            // 1. Define the locator using the provided string
            By locator = AppiumBy.androidUIAutomator(uiSelectorString);

            // 2. WAIT: Use the existing WebDriverWait to ensure the element is clickable
            WebElement element = wait.until(ExpectedConditions.elementToBeClickable(locator));
            
            // 3. CLICK: Perform the click action
            element.click();
            
        } catch (Exception e) {
            System.out.println("❌ Failed to click element using UiAutomator: " + uiSelectorString);
            throw new RuntimeException("Test failed: UiAutomator element not clickable or found.", e);
        }
    }

	/** Click element by Accessibility ID (content-desc) */
	public void clickByAccessibilityId(String id) {
		WebElement element = wait.until(ExpectedConditions.elementToBeClickable(AppiumBy.accessibilityId(id)));
		element.click();
	}

	/** Click element by resource ID (try multiple IDs in order) */
	public void clickById(String... ids) {
		for (String id : ids) {
			try {
				WebElement element = wait.until(ExpectedConditions.elementToBeClickable(AppiumBy.id(id)));
				element.click();
				return;
			} catch (Exception ignored) {
			}
		}
		System.out.println("❌ None of the IDs were clickable: " + String.join(", ", ids));
        throw new RuntimeException("Test failed: None of the provided IDs were clickable: " + String.join(", ", ids));
	}

	/** Click element using UiSelector (className + index) */
	public void clickByUiSelector(String className, int index) {
		driver.findElement(
				AppiumBy.androidUIAutomator("new UiSelector().className(\"" + className + "\").index(" + index + ")"))
				.click();
	}

	/** ===================== TYPE ACTIONS ===================== */

	/** Type text into a WebElement input field */
	public void typeText(WebElement element, String text) {
		element.clear();
		element.sendKeys(text);
	}

	/** Press a key on Android keyboard */
	public void pressKey(AndroidKey key) {
		driver.pressKey(new KeyEvent(key));
	}

	/** ===================== SWIPE / SCROLL ACTIONS ===================== */

	/** Swipe down from top to bottom using W3C pointer input. */
	public void swipeDown() {
		int height = driver.manage().window().getSize().height;
		int width = driver.manage().window().getSize().width;
		int startX = width / 2;
        // Swipe down starts high (75%) and moves to low (25%) on the screen
		int startY = (int) (height * 0.75); 
		int endY = (int) (height * 0.25);  

		swipe(startX, startY, startX, endY, 400); 
	}

	/** Swipe up from bottom to top using W3C pointer input */
	public void swipeUp() {
		int height = driver.manage().window().getSize().height;
		int width = driver.manage().window().getSize().width;
		int startX = width / 2;
        // Swipe up starts low (25%) and moves to high (75%) on the screen
		int startY = (int) (height * 0.25);
		int endY = (int) (height * 0.75);

		swipe(startX, startY, startX, endY, 400); 
	}

	/** General swipe helper using W3C PointerInput */
	private void swipe(int startX, int startY, int endX, int endY, int durationMs) {
		PointerInput finger = new PointerInput(PointerInput.Kind.TOUCH, "finger");
		Sequence swipe = new Sequence(finger, 1);
		swipe.addAction(finger.createPointerMove(Duration.ZERO, PointerInput.Origin.viewport(), startX, startY));
		swipe.addAction(finger.createPointerDown(PointerInput.MouseButton.LEFT.asArg()));
		swipe.addAction(
				finger.createPointerMove(Duration.ofMillis(durationMs), PointerInput.Origin.viewport(), endX, endY));
		swipe.addAction(finger.createPointerUp(PointerInput.MouseButton.LEFT.asArg()));
		driver.perform(Arrays.asList(swipe));
	}

	/** Scroll vertically until an element with specific text is visible */
	public void scrollToText(String text) {
		driver.findElement(AppiumBy.androidUIAutomator("new UiScrollable(new UiSelector().scrollable(true))"
				+ ".scrollIntoView(new UiSelector().text(\"" + text + "\"));"));
	}

	/** ===================== WAIT ACTIONS ===================== */

	/** Wait until element is visible by resource ID */
	public WebElement waitForElementById(String... ids) {
		for (String id : ids) {
			try {
				return wait.until(ExpectedConditions.visibilityOfElementLocated(AppiumBy.id(id)));
			} catch (Exception ignored) {
			}
		}
		System.out.println("❌ None of the IDs are visible: " + String.join(", ", ids));
		return null; 
	}

	/** Wait until element is visible by Accessibility ID */
	public WebElement waitForElementByAccessibilityId(String id) {
		return wait.until(ExpectedConditions.visibilityOfElementLocated(AppiumBy.accessibilityId(id)));
	}
	
	/** ===================== VERIFICATION ACTIONS ===================== */

    /**
     * Waits for an element to be visible by locator and returns its text content.
     * Throws a RuntimeException if the element is not found within the timeout (5 seconds).
     */
    public String getElementText(By locator) {
        try {
            // Wait for the element to be visible using the existing WebDriverWait
            WebElement element = wait.until(ExpectedConditions.visibilityOfElementLocated(locator));
            return element.getText();
        } catch (Exception e) {
            System.out.println("❌ Element not found or visible to retrieve text: " + locator.toString());
            throw new RuntimeException("Test failed: Element not found or visible to retrieve text.", e);
        }
    }
    
    /** Returns a By locator object for a custom Android UI Automator string. */
    public By getUiAutomatorLocator(String uiSelectorString) {
        return AppiumBy.androidUIAutomator(uiSelectorString);
    }
}