package steps;

import io.cucumber.java.en.*;
import pages.LoginPage;
import static org.junit.jupiter.api.Assertions.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class LoginSteps {
    private final World world;
    private final LoginPage login;
    private static final Logger log = LoggerFactory.getLogger(LoginSteps.class);

    public LoginSteps(World world) {
        this.world = world;
        this.login = new LoginPage(world.page);
    }

    // Then (error)
    @Then("the user sees an error message")
    public void the_user_sees_an_error_message() {
        String msg = login.getMessageText();
        assertTrue(login.isErrorMessageShown(),
                () -> "Expected an error message, got: " + msg);
        log.info("Error message shown as expected");
    }
}
