package steps;

import com.microsoft.playwright.*;
import io.cucumber.java.After;
import io.cucumber.java.Before;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Arrays;

public class Hooks {
    private final World world;
    private static final Logger log = LoggerFactory.getLogger(Hooks.class);

    // PicoContainer injects the same World instance into Steps and Hooks
    public Hooks(World world) {
        this.world = world;
    }

    @Before
    public void setUp() {
        log.info("Launching Playwright. headless={}, baseUrl={}", world.headless, world.baseUrl);
        world.playwright = Playwright.create();
        BrowserType.LaunchOptions options = new BrowserType.LaunchOptions()
                .setHeadless(world.headless) // true/false from properties
                .setChannel("chrome"); // Chrome installed
        if (world.headless) {
            options.setArgs(Arrays.asList(new String[]{"--headless=new"}));
        }
        world.browser = world.playwright.chromium()
                .launch(options);
        world.context = world.browser.newContext(new Browser.NewContextOptions().setViewportSize(1280, 800));
        world.page = world.context.newPage();
        // Navigates to baseUrl
        try {
            world.page.navigate(world.baseUrl);
            log.info("Navigated to {}", world.baseUrl);
        } catch (RuntimeException e) {
            log.error("Failed to navigate to baseUrl: {}", world.baseUrl, e);
            throw e;
        }
    }

    @After
    public void afterScenario() {
        try {
            if (world.context != null) world.context.close();
        } catch (Exception e) {
            log.warn("Error closing context", e);
        }
        try {
            if (world.browser != null) world.browser.close();
        } catch (Exception e) {
            log.warn("Error closing browser", e);
        }
        try {
            if (world.playwright != null) world.playwright.close();
        } catch (Exception e) {
            log.warn("Error closing Playwright", e);
        }
        log.info("Playwright closed.");
    }
}

