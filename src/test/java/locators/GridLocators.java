package locators;

public final class GridLocators {
    private GridLocators() {}

    public static final String GRID_URL          = "/grid";
    public static final String GRID_CONTAINER    = "#menu.grid-container";
    public static final String GRID_ITEMS        = GRID_CONTAINER + " .item";

    // In each .item
    public static final String ITEM_LABEL_NUM    = "label[data-test-id='card-number']";
    public static final String ITEM_IMG          = "img";
    public static final String ITEM_NAME         = "h4[data-test-id='item-name']";
    public static final String ITEM_PRICE        = "#item-price";            // id repetido por item
    public static final String ITEM_ADD_BUTTON   = "[data-test-id='add-to-order']";
}
