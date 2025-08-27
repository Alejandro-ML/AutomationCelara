package pages;

import com.microsoft.playwright.Locator;
import com.microsoft.playwright.Page;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import static locators.GridLocators.*;

public class GridPage {
    private final Page page;

    public GridPage(Page page) {
        this.page = page;
    }

    public void open(String baseUrl) {
        page.navigate(baseUrl + GRID_URL);
        page.waitForSelector(GRID_ITEMS); // asegura render
    }

    // -------- helpers ----------
    private Locator itemAtPosition(int position1Based) {
        if (position1Based < 1) throw new IllegalArgumentException("position must be >= 1");
        return page.locator(GRID_ITEMS).nth(position1Based - 1);
    }

    public String titleAtPosition(int position1Based) {
        String txt = itemAtPosition(position1Based).locator(ITEM_NAME).textContent();
        return txt == null ? "" : txt.trim();
    }

    public String priceTextAtPosition(int position1Based) {
        String txt = itemAtPosition(position1Based).locator(ITEM_PRICE).textContent();
        return txt == null ? "" : txt.trim();
    }

    public BigDecimal priceAtPosition(int position1Based) {
        return parseMoney(priceTextAtPosition(position1Based));
    }

    public int itemCount() {
        return page.locator(GRID_ITEMS).count();
    }

    public List<String> validateAllItemsHaveTitlePriceImageButton() {
        List<String> problems = new ArrayList<>();
        Locator items = page.locator(GRID_ITEMS);
        int count = items.count();
        for (int i = 0; i < count; i++) {
            int pos = i + 1;
            Locator item = items.nth(i);

            // title
            String title = safeTrim(item.locator(ITEM_NAME).textContent());
            if (title.isEmpty()) problems.add("Item " + pos + " has empty title");

            // price
            String priceTxt = safeTrim(item.locator(ITEM_PRICE).textContent());
            if (priceTxt.isEmpty()) {
                problems.add("Item " + pos + " has empty price");
            } else {
                try { parseMoney(priceTxt); } catch (Exception e) {
                    problems.add("Item " + pos + " has invalid price: '" + priceTxt + "'");
                }
            }

            // image
            String src = item.locator(ITEM_IMG).getAttribute("src");
            if (src == null || src.trim().isEmpty()) problems.add("Item " + pos + " has empty img src");

            // button
            boolean buttonVisible = item.locator(ITEM_ADD_BUTTON).isVisible();
            boolean buttonEnabled = item.locator(ITEM_ADD_BUTTON).isEnabled();
            if (!buttonVisible) problems.add("Item " + pos + " add button is not visible");
            if (!buttonEnabled) problems.add("Item " + pos + " add button is not enabled");
        }
        return problems;
    }

    // -------- utils ----------
    private static String safeTrim(String s) { return s == null ? "" : s.trim(); }

    private static BigDecimal parseMoney(String raw) {
        String cleaned = safeTrim(raw).replace("$", "");
        if (cleaned.isEmpty()) return BigDecimal.ZERO;
        return new BigDecimal(cleaned);
    }
}

