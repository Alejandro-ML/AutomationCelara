package locators;

/** CSS selectors for the Search page. Keep them 1:1 with the provided HTML. */
public final class SearchLocators {
    private SearchLocators() {}

    // URLs
    public static final String SEARCH_URL = "/search";

    // Form
    public static final String FORM            = "form.example[action='/search-engine']";
    public static final String INPUT_SEARCH    = "form.example input[name='searchWord']";
    public static final String BUTTON_SUBMIT   = "form.example button[type='submit']";

    // Result
    public static final String RESULT_TEXT     = "#result";
}

