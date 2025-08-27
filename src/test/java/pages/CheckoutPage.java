package pages;

import com.microsoft.playwright.Dialog;
import com.microsoft.playwright.Page;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;
import java.util.function.Consumer;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static locators.CheckoutLocators.*;

public class CheckoutPage {
    private final Page page;

    public CheckoutPage(Page page) {
        this.page = page;
    }

    public void open(String baseUrl) {
        page.navigate(baseUrl + "/checkout");
    }

    // --------- Fillers ---------
    public void fillBillingAndPayment(CheckoutTestData d) {
        page.fill(ID_FULL_NAME, d.fullName());
        page.fill(ID_EMAIL, d.email());
        page.fill(ID_ADDRESS, d.address());
        page.fill(ID_CITY, d.city());
        page.fill(ID_STATE, d.state());
        page.fill(ID_ZIP, d.zip());

        page.fill(ID_NAME_ON_CARD, d.nameOnCard());
        page.fill(ID_CC_NUMBER, d.cardNumber());
        page.selectOption(ID_EXP_MONTH, d.expMonth()); // visible text (value = label)
        page.fill(ID_EXP_YEAR, d.expYear());
        page.fill(ID_CVV, d.cvv());
    }

    // --------- Checkbox helpers ---------
    public boolean isSameAsBillingChecked() {
        return page.isChecked(NAME_SAMEADR);
    }

    public void ensureSameAsBillingChecked() {
        if (!isSameAsBillingChecked()) page.check(NAME_SAMEADR);
    }

    public void ensureSameAsBillingUnchecked() {
        if (isSameAsBillingChecked()) page.uncheck(NAME_SAMEADR);
    }

    // --------- Submit ---------
    public void submit() {
        page.click(BTN_CONTINUE);
    }

    /**
     * onDialog + CountDownLatch (without waitForEvent).
     * This funcion handles the alert events in Playwright dialog.
     */
    public String submitExpectingAlertAndAccept() {
        final String[] msg = new String[1];
        final CountDownLatch latch = new CountDownLatch(1);

        Consumer<Dialog> handler = dialog -> {
            msg[0] = dialog.message();
            dialog.accept();
            latch.countDown();
        };
        page.onDialog(handler);   // temporal record of the handler
        submit();                 // triggers the alert

        try {
            latch.await(5, TimeUnit.SECONDS);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
        // If your version supports offDialog(handler), you can unregister here.

        return msg[0] == null ? "" : msg[0];
    }

    /**
     * Waits redirect to /order without WaitForURLOptions.
     */
    public void waitForOrderRedirect() {
        page.waitForURL("**/order");
    }

    /**
     * Reads a confirmation number /order (fallback if no testid).
     */
    public String readConfirmationNumber() {
        String[] candidates = {
                "#order-number", "[data-test='order-number']", "[data-testid='order-number']"
        };
        for (String sel : candidates) {
            if (page.isVisible(sel)) {
                String txt = page.textContent(sel);
                if (txt != null && !txt.trim().isEmpty()) return txt.trim();
            }
        }
        String body = page.textContent("body");
        if (body == null) return "";
        Matcher m = Pattern.compile("\\b\\d{4,}\\b").matcher(body);
        return m.find() ? m.group() : "";
    }

    // --------- Cart helpers ---------
    public List<BigDecimal> readItemPrices() {
        int count = page.locator(CART_ITEM_PRICES).count();
        List<BigDecimal> prices = new ArrayList<>();
        for (int i = 0; i < count - 1; i++) { // el último es "Total"
            String raw = page.locator(CART_ITEM_PRICES).nth(i).textContent();
            prices.add(parseMoney(raw));
        }
        return prices;
    }

    public BigDecimal readDisplayedTotal() {
        String raw = page.textContent(CART_TOTAL_PRICE);
        return parseMoney(raw);
    }

    private static BigDecimal parseMoney(String raw) {
        if (raw == null) return BigDecimal.ZERO;
        String cleaned = raw.replace("$", "").trim();
        return new BigDecimal(cleaned);
    }

    // --------- Test data factory (dummy data) ---------
    public static CheckoutTestData validDummyData() {
        return new CheckoutTestData(
                "Jane Tester",
                "jane.tester@example.com",
                "123 Test Ave",
                "Metropolis",
                "CA",
                "90210",
                "JANE TESTER",
                "4111111111111111", // PAN de prueba
                "March",
                "2030",
                "123"
        );
    }

    // --------- POJO ---------
    public record CheckoutTestData(
            String fullName, String email, String address, String city, String state, String zip,
            String nameOnCard, String cardNumber, String expMonth, String expYear, String cvv
    ) {
    }
}
