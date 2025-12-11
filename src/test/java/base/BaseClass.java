package base;

import java.time.Duration;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.annotations.AfterClass; // <-- Teardown for the whole class
import org.testng.annotations.BeforeClass; // <-- Setup for the whole class

import io.github.bonigarcia.wdm.WebDriverManager;

public class BaseClass {

    // These variables are accessible to all test classes that extend BaseClass
    protected WebDriver driver; 
    protected WebDriverWait wait; 
    protected final String BASE_URL = "https://smartbuy-me.com/";
    private final int TIMEOUT_SECONDS = 15; 

    // Runs ONCE before any @Test method in the class is executed.
    @BeforeClass
    public void setup() {
        // --- 1. ChromeOptions Setup (Headless Configuration) ---
        ChromeOptions options = new ChromeOptions(); 
        boolean isHeadless = Boolean.parseBoolean(System.getProperty("headless", "false"));

        if (isHeadless) {
            options.addArguments("--headless=new");
            options.addArguments("--disable-gpu");
            options.addArguments("--window-size=1920,1080"); 
            System.out.println("TEST RUNNING: Chrome in HEADLESS mode.");
        } else {
            System.out.println("TEST RUNNING: Chrome VISIBLY.");
        }

        // Initialize WebDriverManager and ChromeDriver
        WebDriverManager.chromedriver().setup();
        driver = new ChromeDriver(options); 
        
        // --- 2. Initialize Driver Settings and Wait ---
        driver.manage().window().maximize();
        wait = new WebDriverWait(driver, Duration.ofSeconds(TIMEOUT_SECONDS));

        // Navigate to the base URL (only happens once per test class)
        driver.get(BASE_URL);
    }

    // Runs ONCE after all @Test methods in the class have executed.
    @AfterClass
    public void teardown() {
        if (driver != null) {
            driver.quit();
            System.out.println("Browser closed.");
        }
    }
}