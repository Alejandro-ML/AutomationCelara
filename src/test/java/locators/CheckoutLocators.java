package locators;

/** Selectors based strictly on the provided HTML. */
public final class CheckoutLocators {
    private CheckoutLocators() {}

    // ---- Inputs (ids / names) ----
    public static final String ID_FULL_NAME    = "#fname";     // name=firstname
    public static final String ID_EMAIL        = "#email";     // name=email
    public static final String ID_ADDRESS      = "#adr";       // name=address
    public static final String ID_CITY         = "#city";      // name=city
    public static final String ID_STATE        = "#state";     // name=state
    public static final String ID_ZIP          = "#zip";       // name=zip

    public static final String ID_NAME_ON_CARD = "#cname";     // name=cardname
    public static final String ID_CC_NUMBER    = "#ccnum";     // name=cardnumber
    public static final String ID_EXP_MONTH    = "#expmonth";  // select name=expmonth
    public static final String ID_EXP_YEAR     = "#expyear";   // name=expyear
    public static final String ID_CVV          = "#cvv";       // name=cvv

    public static final String NAME_SAMEADR    = "input[name='sameadr']"; // checkbox
    public static final String BTN_CONTINUE    = "button.btn:has-text('Continue to checkout')";

    // ---- Cart (right panel) ----
    public static final String CART_CONTAINER  = "div.col-25 .container";
    public static final String CART_ITEM_PRICES = CART_CONTAINER + " p .price"; // includes Total (last)
    public static final String CART_TOTAL_PRICE = CART_CONTAINER + " p:has-text('Total') .price b";

    // ---- Generic ----
    public static final String FORM            = "form[action='/form-validation']";
}

