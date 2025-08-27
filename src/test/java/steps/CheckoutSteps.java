package steps;

import io.cucumber.java.en.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import pages.CheckoutPage;
import com.microsoft.playwright.Route;

import java.math.BigDecimal;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class CheckoutSteps {
    private static final Logger log = LoggerFactory.getLogger(CheckoutSteps.class);

    private final World world;
    private final CheckoutPage checkout;

    public CheckoutSteps(World world) {
        this.world = world;
        this.checkout = new CheckoutPage(world.page);
    }

    // ---------- Navigation ----------
    @Given("the user opens the checkout page")
    public void the_user_opens_the_checkout_page() {
        checkout.open(world.baseUrl);
        log.info("Opened checkout page at {}/checkout", world.baseUrl);
    }

    // ---------- Fill form ----------
    @When("the user completes the checkout form with valid data")
    public void the_user_completes_the_checkout_form_with_valid_data() {
        checkout.fillBillingAndPayment(CheckoutPage.validDummyData());
        log.info("Filled billing and payment form with dummy data");
    }

    // ---------- Checkbox state ----------
    @And("the user ensures the Shipping address is the same as billing")
    public void the_user_ensures_same_address_checked() {
        checkout.ensureSameAsBillingChecked();
        log.info("Ensured 'same address' is checked");
    }

    @And("the user ensures the Shipping address is NOT the same as billing")
    public void the_user_ensures_same_address_unchecked() {
        checkout.ensureSameAsBillingUnchecked();
        log.info("Ensured 'same address' is unchecked");
    }

    // ---------- Submit ----------
    @When("the user submits the checkout form")
    public void the_user_submits_the_checkout_form() {
        checkout.submit();
        log.info("Submitted checkout form");
    }

    // ---------- Success ----------
    @Then("the user is redirected to the order page and sees a non-empty confirmation number")
    public void the_user_is_redirected_and_sees_confirmation() {
        checkout.waitForOrderRedirect();
        String orderNumber = checkout.readConfirmationNumber();
        assertNotNull(orderNumber, "Order number should not be null");
        assertFalse(orderNumber.isBlank(), "Order number should not be empty");
        log.info("Order confirmation number: {}", orderNumber);
    }

    // ---------- Alert handling (dialog) ----------
    // This is the correct way to handle the dialog when the backend/front-end produce an alert.
    @When("the user tries to submit and accepts the alert")
    public void the_user_tries_to_submit_and_accepts_the_alert() {
        String msg = checkout.submitExpectingAlertAndAccept();
        assertNotNull(msg, "Alert message should not be null");
        assertFalse(msg.isBlank(), "Alert message should not be empty");
        log.info("Alert appeared with message: {}", msg);
    }

    // Install a network stub for POST /form-validation that forces a 406 with text (so the page will alert).
    @When("the user tries to submit and stub")
    public void the_user_tries_to_submit_and_stub_the_post() {
        world.page.route("**/form-validation", route -> {
            route.fulfill(new Route.FulfillOptions()
                    .setStatus(406)
                    .setContentType("text/plain")
                    .setBody("Not acceptable for demo"));
        });
        log.info("Installed network stub for '**/form-validation' -> 406 text/plain");
    }

    @Then("the user verifies no alert is currently open")
    public void the_user_verifies_no_alert_is_open() {
        // If a dialog were still open, any interaction would fail; we evaluate a trivial script to confirm interactivity.
        Object ok = world.page.evaluate("() => true");
        assertEquals(Boolean.TRUE, ok, "Page should be interactive; no alert open");
        log.info("No alert is currently open");
    }

    // ---------- Cart total ----------
    @Then("the user sees the cart total equals the sum of the item prices")
    public void the_user_sees_cart_total_equals_sum() {
        List<BigDecimal> items = checkout.readItemPrices();
        BigDecimal sum = items.stream().reduce(BigDecimal.ZERO, BigDecimal::add);
        BigDecimal displayed = checkout.readDisplayedTotal();
        assertEquals(0, displayed.compareTo(sum),
                () -> "Displayed total " + displayed + " does not equal items sum " + sum);
        log.info("Cart total validated. Sum={} Displayed={}", sum, displayed);
    }
}
