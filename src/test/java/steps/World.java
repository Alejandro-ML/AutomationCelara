package steps;

import com.microsoft.playwright.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class World {
    public Playwright playwright;
    public Browser browser;
    public BrowserContext context;
    public Page page;
    public String baseUrl;
    public boolean headless;
    private static final Logger log = LoggerFactory.getLogger(Hooks.class);
    public World() {
        loadConfig();
    }

    private void loadConfig() {
        Properties props = new Properties();
        try (InputStream input = getClass().getClassLoader().getResourceAsStream("config.properties")) {
            if (input == null) {
                log.error("Could not find config.properties, using defaults.");
                baseUrl = "http://localhost:3100";
                headless = true;
                return;
            }
            props.load(input);

            baseUrl = props.getProperty("baseUrl", "http://localhost:3100");
            headless = Boolean.parseBoolean(props.getProperty("headless", "true"));

        } catch (IOException e) {
            throw new RuntimeException("Error loading config.properties", e);
        }
    }

}
