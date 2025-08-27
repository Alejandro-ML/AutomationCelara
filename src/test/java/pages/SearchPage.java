package pages;

import com.microsoft.playwright.Page;

import static locators.SearchLocators.*;

/**
 * Page Object for the /search page.
 * Works with older Playwright Java versions (no WaitForURLOptions, no EventType).
 */
public class SearchPage {
    private final Page page;

    public SearchPage(Page page) {
        this.page = page;
    }

    /** Navigate to /search and wait for the form to be present. */
    public void open(String baseUrl) {
        page.navigate(baseUrl + SEARCH_URL);
        page.waitForSelector(FORM);
    }

    /** Type a search term and submit the form. */
    public void searchFor(String term) {
        page.fill(INPUT_SEARCH, term);
        // Click submit (you could also press Enter)
        page.click(BUTTON_SUBMIT);
    }

    /** Submit with an empty field. */
    public void submitEmpty() {
        page.fill(INPUT_SEARCH, "");
        page.click(BUTTON_SUBMIT);
    }

    /** Read the visible result text. */
    public String resultText() {
        String text = page.textContent(RESULT_TEXT);
        return text == null ? "" : text.trim();
    }

    /**
     * Wait until the result text contains the expected substring (polling).
     * Useful because the page updates the result asynchronously via jQuery AJAX.
     */
    public String waitForResultToContain(String expected, long timeoutMs) {
        long start = System.currentTimeMillis();
        String txt = "";
        while ((System.currentTimeMillis() - start) <= timeoutMs) {
            txt = resultText();
            if (!txt.isEmpty() && txt.contains(expected)) break;
            page.waitForTimeout(100); // small polling delay
        }
        return txt;
    }
}
