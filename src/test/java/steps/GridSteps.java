package steps;

import io.cucumber.java.en.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import pages.GridPage;

import java.math.BigDecimal;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class GridSteps {
    private static final Logger log = LoggerFactory.getLogger(GridSteps.class);

    private final World world;
    private final GridPage grid;

    public GridSteps(World world) {
        this.world = world;
        this.grid = new GridPage(world.page);
    }

    // ---- Navigation ----
    /** Opens the grid page and verifies it renders. */
    @Given("the user opens the grid page")
    public void the_user_opens_the_grid_page() {
        log.info("Navigating to grid page at {}/grid ...", world.baseUrl);
        grid.open(world.baseUrl);
        int count = grid.itemCount();
        log.info("Grid page opened. Rendered items count: {}", count);
    }

    // ---- Position assertions ----
    /** Asserts the item title at the given 1-based position. */
    @Then("the user sees that item at position {int} has title {string}")
    public void the_user_sees_item_at_position_has_title(int position, String expectedTitle) {
        log.info("Asserting title at position {} should be '{}'", position, expectedTitle);
        String actual = grid.titleAtPosition(position);
        log.debug("Resolved title at position {}: '{}'", position, actual);
        assertEquals(expectedTitle, actual, "Unexpected title at position " + position);
        log.info("Title assertion passed at position {}.", position);
    }

    /** Asserts the item price text (e.g., '$10') at the given 1-based position. */
    @Then("the user sees that item at position {int} has price {string}")
    public void the_user_sees_item_at_position_has_price(int position, String expectedPriceText) {
        log.info("Asserting price text at position {} should be {}", position, expectedPriceText);
        String actualTxt = grid.priceTextAtPosition(position);
        log.debug("Resolved price text at position {}: {}", position, actualTxt);
        assertEquals(expectedPriceText, actualTxt, "Unexpected price text at position " + position);
        log.info("Price text assertion passed at position {}.", position);
    }

    /** Numeric price assertion variant for strict numeric comparison. */
    @Then("the user sees that item at position {int} has numeric price {double}")
    public void the_user_sees_item_at_position_has_numeric_price(int position, double expected) {
        log.info("Asserting numeric price at position {} should be {}", position, expected);
        BigDecimal actual = grid.priceAtPosition(position);
        log.debug("Resolved numeric price at position {}: {}", position, actual);
        assertEquals(0, actual.compareTo(BigDecimal.valueOf(expected)),
                "Unexpected numeric price at position " + position + ": " + actual);
        log.info("Numeric price assertion passed at position {}.", position);
    }

    // ---- All items non-empty ----
    /** Validates every grid card has non-empty title, price, image src, and an enabled visible button. */
    @Then("each grid item has non-empty title, price, image and button")
    public void each_grid_item_has_non_empty_title_price_image_and_button() {
        log.info("Validating that all grid items have title, price, image and button...");
        List<String> problems = grid.validateAllItemsHaveTitlePriceImageButton();

        if (!problems.isEmpty()) {
            problems.forEach(p -> log.warn("Validation issue: {}", p));
        }

        assertTrue(problems.isEmpty(),
                "Grid validations failed:\n" + String.join("\n", problems));
        log.info("All grid items validated successfully.");
    }
}
