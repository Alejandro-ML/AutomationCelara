# QA UI Automation – Playwright + Cucumber (Java)

End-to-end UI tests for the demo app (Login, Checkout, Grid, Search) running on **Playwright for Java** + **Cucumber** (JUnit Platform).
Link to instructions: https://github.com/automationapptest/home-test
---

## ✨ Overview
- **Stack:** Java, Maven, Playwright (Java), Cucumber, JUnit Platform, SLF4J.
- **Pattern:** Page Object Model with dedicated *locators* packages, reusable `CommonSteps`, and Hooks that log + take screenshots on failure.
- **Headless:** **Disabled by default** (`headless=false`) so the browser opens visibly.

---

## 🧰 Requirements
- JDK 17+
- Maven 3.8+
- Google Chrome installed (project launches with `setChannel("chrome")`). You can switch to stock Chromium if preferred.
- Internet on first run (Playwright will download browsers if needed).

---

## 📁 Project Structure
```
src/
└─ test/
   ├─ java/
   │  ├─ locators/
   │  │  ├─ CheckoutLocators.java
   │  │  ├─ GridLocators.java
   │  │  ├─ LoginLocators.java
   │  │  └─ SearchLocators.java
   │  ├─ pages/
   │  │  ├─ CheckoutPage.java
   │  │  ├─ GridPage.java
   │  │  ├─ LoginPage.java
   │  │  └─ SearchPage.java
   │  ├─ runners/
   │  │  └─ RunCucumberTest.java
   │  └─ steps/
   │     ├─ CheckoutSteps.java
   │     ├─ CommonSteps.java
   │     ├─ GridSteps.java
   │     ├─ Hooks.java
   │     ├─ LoginSteps.java
   │     └─ SearchSteps.java
   └─ resources/
      ├─ features/
      │  ├─ checkout.feature
      │  ├─ grid.feature
      │  ├─ login.feature
      │  └─ search.feature
      └─ config.properties
```

---

## ⚙️ Configuration (MUST DO)
Create `src/test/resources/config.properties` with the following keys:

```properties
baseUrl=http://localhost:3100
headless=false
```

> `headless=false` means the browser **opens visibly**. Set `true` if you prefer headless CI runs.

---

## ▶️ How to Run with Maven

Run the entire suite:
```bash
mvn test
```

Run by **feature file**:
```bash
# Checkout only
mvn test -Dcucumber.features=src/test/resources/features/checkout.feature

# Grid only
mvn test -Dcucumber.features=src/test/resources/features/grid.feature

# Login only
mvn test -Dcucumber.features=src/test/resources/features/login.feature

# Search only
mvn test -Dcucumber.features=src/test/resources/features/search.feature
```

Run by **tag**:
```bash
mvn test -Dcucumber.filter.tags=@order_success
mvn test -Dcucumber.filter.tags="@cart_total or @form_alert"
```

Run a **single scenario** (by line in the feature file):
```bash
mvn test -Dcucumber.features=src/test/resources/features/checkout.feature:12
```

Switch **headful/headless**:
```properties
# In src/test/resources/config.properties
headless=false   # headful (default here)
# headless=true  # headless (CI-friendly)
```

---

## 💡 Running Individual Features from the IDE (Plugins)

You can run tests feature-by-feature and even scenario-by-scenario directly from the IDE thanks to Cucumber integrations.

- **IntelliJ IDEA Plugins:**
  - **Cucumber for Java** (required to run features and get step navigation).
  - **Playwright** plugin is **optional**. Playwright Java runs through JUnit; no special plugin is required, but the plugin may provide editor niceties.  
- **How:** Open any `*.feature` file and **Right‑click → Run** on a *Feature*, *Scenario Outline*, or a single *Scenario*. The IDE will invoke the JUnit Platform Cucumber engine and run only what you selected.

> You can also run via Maven using `-Dcucumber.features=<path>` to target a single feature file without opening the IDE runner.

---

## 🧪 Features Covered
- **Login UI** — Success / Failure.
- **Checkout** — Order Success (redirect + confirmation number), Alert flow, Cart total equals sum.
- **Grid** — Assertion by position (title/price), and a full validation that every card has title/price/image/button.
- **Search** — Success message (`Found one result for <word>`) and empty-search validation (`Please provide a search word.`).

---

## 🪵 Logging & Evidence
- Hooks log scenario start/end, browser version, and `document.readyState` after each step.
- Steps log every action and assertion.
- On failure, a full-page **screenshot** is attached to the scenario report.

---