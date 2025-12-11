package core;

import java.net.MalformedURLException;
import java.net.URL;
import java.time.Duration;

import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeTest;

import helpers.DeviceActions;
import io.appium.java_client.android.AndroidDriver;
import io.appium.java_client.android.options.UiAutomator2Options;

/**
 * BaseTest handles driver setup and teardown for all Android tests.
 */
public class BaseTest {

    protected AndroidDriver driver;
    protected DeviceActions actions; 
    protected UiAutomator2Options options;

    @BeforeTest
    public void setUp() {
        System.out.println("=== BaseTest: Setup ===");

        options = new UiAutomator2Options();
        options.setPlatformName("Android");
        
        // Ensure this is your simple, verified ADB device ID
        options.setDeviceName("RZ8W90NJNKM"); 
        
        // This capability remains correct
        options.setAutomationName("UiAutomator2"); 
        
        options.setAppPackage("com.android.settings"); 
        options.setAppActivity(".Settings");
        
        options.setNewCommandTimeout(Duration.ofSeconds(60)); 

        try {
            URL appiumServerURL = new URL("http://127.0.0.1:4723");
            driver = new AndroidDriver(appiumServerURL, options);
            System.out.println("✅ Connected to Android device!");
        } catch (MalformedURLException e) {
            throw new RuntimeException("Appium Server URL is malformed: " + e.getMessage(), e);
        } catch (Exception e) {
             System.out.println("❌ Failed to connect to device. Ensure Appium Server is running!");
             e.printStackTrace();
             throw new RuntimeException("Driver instantiation failed: " + e.getMessage(), e);
        }

        // Initialize the helper actions after the driver is created
        actions = new DeviceActions(driver, 5); 
    }

    @AfterTest
    public void tearDown() {
        System.out.println("=== BaseTest: Teardown ===");
        if (driver != null) {
            driver.quit();
            System.out.println("✅ Driver session ended!");
        }
    }
}