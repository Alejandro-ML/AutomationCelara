package locators;

/** Isolated the selectors in DOM for Login and messages. */
public final class LoginLocators {
    private LoginLocators() {}

    public static final String USERNAME_INPUT = "#username";
    public static final String PASSWORD_INPUT = "#password";
    public static final String SIGNIN_BUTTON  = "#signin-button";

    public static final String MESSAGE_BANNER = "#message, .message, #flash, .flash, #error, .error, .alert";

    public static final String PAGE_BODY = "body";
}
