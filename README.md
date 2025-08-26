# UI Tests – Java + Playwright + Cucumber

End-to-end UI tests for a sample web app using **Java 21**, **Maven**, **Playwright**, **Cucumber (JUnit 5)** and **PicoContainer** for DI.

## Tech stack
- Java: **21** (configurable via `maven.compiler.release`)
- Maven: **3.9+**
- Playwright: **1.46.0**
- Cucumber JVM: **7.15.0**
- JUnit Platform (Suite): **1.10.2**

## Project structure
```
ChallengeTest/
├─ pom.xml
└─ src
   └─ test
      ├─ java
      │  ├─ pages/
      │  │  ├─ LoginLocators.java
      │  │  └─ LoginPage.java
      │  ├─ runners/
      │  │  └─ RunCucumberTest.java
      │  └─ steps/
      │     ├─ Hooks.java
      │     ├─ LoginSteps.java
      │     └─ World.java
      └─ resources
         └─ features/
            └─ login.feature
```
> `World` is the per-scenario context that shares Playwright objects (`Playwright`, `Browser`, `BrowserContext`, `Page`) and common config (e.g., `baseUrl`).

## Prerequisites
- JDK **21** installed (`java -version`). If you prefer JDK 17, change `<maven.compiler.release>` to `17` in `pom.xml`.
- Maven **3.9+** (`mvn -v`).
- App under test running locally at `http://localhost:3100` for the login tests.

## Create the config.file with the following keys
baseUrl=http://localhost:3100
headless=false
Then allocate this file in the resource folder
```
Override at runtime with JVM properties, e.g.:
```bash
-DbaseUrl=http://localhost:3100 -Dheadless=false
```
If your `Hooks` supports it, you can also pass:
```bash
-Dbrowser=chromium|firefox|webkit  [-Dchannel=chrome|msedge]
```

## First run
Install Playwright browsers and run tests (headed):
```bash
mvn clean test -Dplaywright.cli.install=true -Dheadless=false -DbaseUrl=http://localhost:3100
If you need chrome not to trigger set headless=true
If you want to run the test in Chromium modify the hook file "setChannel("chromium")"
```
Subsequent runs (headless, using pom defaults):
```bash
mvn test

```

## How to run
- Run everything (headed/headless toggle):
```bash
mvn test -DbaseUrl=http://localhost:3100 -Dheadless=false
```
- Run a single **scenario** by name (regex):
```bash
mvn test -Dcucumber.filter.name="Login Success" -DbaseUrl=http://localhost:3100
```
- Run a single **feature**:
```bash
mvn test -Dcucumber.features=classpath:features/login.feature
```

## How it works
- The JUnit 5 runner `RunCucumberTest` boots the **Cucumber** engine on the JUnit Platform.
- `maven-surefire-plugin` includes `**/*RunCucumberTest.java` so Maven discovers the runner.
- Features are loaded from `classpath:features` and steps (glue) from the `steps` package (configured in the runner).
- `Hooks` creates and tears down Playwright per scenario (`@Before/@After`).

## Run from Playwright
- Run a *.feature indvidually.

## Adding new tests
1. Create a Page Object (`pages/*Page.java`) and, optionally,
2. Create a locator class for each feature and allocate in (`locators/*Locators.java`).
2. Add step definitions in `src/test/java/steps` (inject `World` via constructor).
3. Write a `.feature` under `src/test/resources/features`.
4. Run via `mvn test` or from the IDE by running `RunCucumberTest`.
