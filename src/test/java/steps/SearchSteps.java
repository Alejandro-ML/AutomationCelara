package steps;

import io.cucumber.java.en.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import pages.SearchPage;

import static org.junit.jupiter.api.Assertions.*;

public class SearchSteps {
    private static final Logger log = LoggerFactory.getLogger(SearchSteps.class);

    private final World world;
    private final SearchPage search;

    public SearchSteps(World world) {
        this.world = world;
        this.search = new SearchPage(world.page);
    }

    // -------- Navigation --------
    /** Opens the Search page and waits for the form to be present. */
    @Given("the user opens the search page")
    public void the_user_opens_the_search_page() {
        log.info("Navigating to search page at {}/search ...", world.baseUrl);
        search.open(world.baseUrl);
        log.info("Search page opened successfully.");
    }

    // -------- Actions --------
    /** Types the provided term into the search box and submits the form. */
    @When("the user searches for the word {string}")
    public void the_user_searches_for_the_word(String word) {
        log.info("Submitting search for term='{}'", word);
        search.searchFor(word);
        log.debug("Search submit triggered for '{}'", word);
    }

    /** Submits the search with an empty term to trigger validation. */
    @When("the user submits an empty search")
    public void the_user_submits_an_empty_search() {
        log.info("Submitting empty search (validation path).");
        search.submitEmpty();
        log.debug("Empty search submit triggered.");
    }

    // -------- Assertions --------
    /** Verifies that the asynchronous result contains the success message with the searched word. */
    @Then("the user sees the success message containing {string}")
    public void the_user_sees_the_success_message_containing(String word) {
        String expectedFragment = "Found one result for " + word;
        log.info("Waiting for success message to contain: '{}'", expectedFragment);
        String txt = search.waitForResultToContain(expectedFragment, 5_000);
        log.debug("Result text resolved to: '{}'", txt);
        assertTrue(txt.contains(expectedFragment),
                () -> "Expected result to contain '" + expectedFragment + "', got: '" + txt + "'");
        log.info("Success message assertion passed.");
    }

    /** Verifies that the asynchronous result shows the empty-search validation message. */
    @Then("the user sees the empty-search error message")
    public void the_user_sees_the_empty_search_error_message() {
        String expected = "Please provide a search word.";
        log.info("Waiting for empty-search error message: '{}'", expected);
        String txt = search.waitForResultToContain("Please", 5_000);
        log.debug("Result text resolved to: '{}'", txt);
        assertTrue(txt.contains(expected),
                () -> "Expected error message '" + expected + "', got: '" + txt + "'");
        log.info("Empty-search error message assertion passed.");
    }
}
