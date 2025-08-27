package steps;

import io.cucumber.java.en.*;
import pages.LoginPage;
import static org.junit.jupiter.api.Assertions.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class CommonSteps {
    private static final Logger log = LoggerFactory.getLogger(CommonSteps.class);

    private final World world;
    private final LoginPage login;

    public CommonSteps(World world) {
        this.world = world;
        this.login = new LoginPage(world.page);
    }

    // -------- Navigation --------
    /** Opens the Login page and verifies the page is reachable. */
    @Given("the user opens the login page")
    public void the_user_opens_the_login_page() {
        log.info("Navigating to login page at {} ...", world.baseUrl);
        login.open(world.baseUrl);
        log.info("Login page opened successfully.");
    }

    // -------- Action --------
    /** Fills credentials and submits the login form (never log plain passwords). */
    @When("the user logs in with user {string} and password {string}")
    public void the_user_logs_in_with_user_and_password(String user, String pass) {
        log.info("Attempting login with user '{}'", user);
        login.login(user, pass);
        log.debug("Login form submitted for user '{}'", user);
    }

    // -------- Assertion (success) --------
    /** Asserts that the welcome message includes the provided username. */
    @Then("the user sees a welcome message containing {string}")
    public void the_user_sees_a_welcome_message_containing(String username) {
        log.info("Verifying welcome message contains username '{}'", username);
        String msg = login.getMessageText();
        log.debug("Resolved message text: '{}'", msg);
        assertTrue(login.isWelcomeFor(username),
                () -> "Expected welcome message to contain '" + username + "', got: " + msg);
        log.info("Welcome message assertion passed for user '{}'", username);
    }

    // -------- Compound reusable step --------
    /** Opens login, performs login, and verifies success (useful Background precondition). */
    @Given("the user is logged in as {string} with password {string}")
    public void the_user_is_logged_in_as_with_password(String user, String pass) {
        log.info("Ensuring user '{}' is logged in (compound step)...", user);
        login.open(world.baseUrl);
        log.debug("Login page opened; proceeding with credentials for '{}'", user);
        login.login(user, pass);
        String msg = login.getMessageText();
        log.debug("Post-login message text: '{}'", msg);
        assertTrue(login.isWelcomeFor(user),
                () -> "Login did not succeed for '" + user + "'. Message: " + msg);
        log.info("User '{}' is logged in", user);
    }
}
