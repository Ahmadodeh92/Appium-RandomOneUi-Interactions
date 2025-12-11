package tests;

import org.openqa.selenium.WebElement;
import org.testng.annotations.Test;

import core.BaseTest;

public class DeviceExplorationTest extends BaseTest { 

    @Test
    public void testDeviceExploration() {
        System.out.println("=== Device Exploration: Test Running ===");

        // 1️⃣ Click search button using Accessibility ID (content-desc)
        actions.clickByAccessibilityId("Search settings");
        System.out.println("✅ Search button clicked");

        // 2️⃣ Wait for search input and type "About phone"
        WebElement searchInput = actions.waitForElementById(
                "com.android.settings:id/search_src_text", // Standard/AOSP ID
                "com.samsung.android.settings.search:id/search_src_text", // Samsung Search ID
                "com.android.settings.intelligence:id/search_src_text" // Other vendor ID
        );
        
        if (searchInput == null) {
            throw new RuntimeException("Search input field not found after waiting. Check locators.");
        }
        
        actions.typeText(searchInput, "About phone");
        System.out.println("✅ Typed 'About phone' successfully");

        // 3️⃣ Click on the result using UiSelector (className + index)
        // **Warning: This is highly fragile. Use a text locator if possible.**
        actions.clickByUiSelector("android.widget.LinearLayout", 2);
        System.out.println("✅ Clicked on 'About phone' item");

        // 4️⃣ Optional: swipe down to explore more settings
        actions.swipeDown();
        System.out.println("✅ Swiped down");
        
        actions.clickByUiAutomatorText("new UiSelector().text(\"Software information\")");
        
        

        
        
        
        
        
    }
}