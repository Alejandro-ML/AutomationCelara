package steps;

import io.cucumber.java.en.*;
import pages.LoginPage;
import static org.junit.jupiter.api.Assertions.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class LoginSteps {
    private final World world;
    private LoginPage login;
    private static final Logger log = LoggerFactory.getLogger(LoginSteps.class);

    public LoginSteps(World world) {
        this.world = world;
        this.login = new LoginPage(world.page);
    }

    @Given("I open the login page")
    public void i_open_the_login_page() {
        login.open(world.baseUrl);
        log.info("Opens the login page");
    }

    @When("I login with user {string} and password {string}")
    public void i_login_with_user_and_password(String user, String pass) {
        login.login(user, pass);
        log.info("Logs in with user {} and password {}", user, pass);
    }

    @Then("I should see a welcome message containing {string}")
    public void i_should_see_welcome_with_username(String username) {
        assertTrue(login.isWelcomeFor(username),
                "Expected welcome message containing username, got: " + login.getMessageText());
    }

    @Then("I should see an error message")
    public void i_should_see_an_error_message() {
        assertTrue(login.isErrorMessageShown(),
                "Expected error message, got: " + login.getMessageText());
    }
}

