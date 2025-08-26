package pages;

import com.microsoft.playwright.Locator;
import com.microsoft.playwright.Page;
import com.microsoft.playwright.options.LoadState;
import locators.LoginLocators;

public class LoginPage {
    private final Page page;

    public LoginPage(Page page) { this.page = page; }

    public void open(String baseUrl) {
        page.navigate(baseUrl + "/login");
    }

    public void fillUsername(String user) {
        page.fill(LoginLocators.USERNAME_INPUT, user);
    }

    public void fillPassword(String pass) {
        page.fill(LoginLocators.PASSWORD_INPUT, pass);
    }

    public void submit() {
        page.click(LoginLocators.SIGNIN_BUTTON);
        // Smart wait until POST /authenticate
        page.waitForLoadState(LoadState.NETWORKIDLE, new Page.WaitForLoadStateOptions().setTimeout(5000));
    }

    public void login(String user, String pass) {
        fillUsername(user);
        fillPassword(pass);
        submit();
    }

    /** Returns text from banner if it exists; else, the whole body (fallback). */
    public String getMessageText() {
        Locator banner = page.locator(LoginLocators.MESSAGE_BANNER);
        if (banner.count() > 0) {
            return banner.first().innerText().trim();
        }
        return page.textContent(LoginLocators.PAGE_BODY).trim();
    }

    public boolean containsIgnoreCase(String haystack, String needle) {
        return haystack != null && needle != null &&
                haystack.toLowerCase().contains(needle.toLowerCase());
    }

    public boolean isErrorMessageShown() {
        String t = getMessageText();

        return containsIgnoreCase(t, "invalid")
                || containsIgnoreCase(t, "incorrect")
                || containsIgnoreCase(t, "required")
                || containsIgnoreCase(t, "error")
                || page.url().contains("/login");
    }

    public boolean isWelcomeFor(String username) {
        String t = getMessageText();

        return containsIgnoreCase(t, "welcome") && containsIgnoreCase(t, username);
    }
}

