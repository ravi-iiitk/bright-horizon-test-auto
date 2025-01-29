

# **BrightHorizon Test Automation Framework**

## **Overview**
The **BrightHorizon Test Automation Framework** is a **hybrid test framework** designed for **web automation** using **Selenium WebDriver**, **TestNG**, and **Cucumber**. It supports **local execution, Selenium Grid, Docker, and BrowserStack for cloud-based cross-browser testing**.

### **Key Features**
- **Multiple Execution Platforms**:
  - `local-driver`: Runs tests on a local browser.
  - `local-driver-manager`: Uses WebDriverManager to handle drivers.
  - `grid`: Executes tests on a **Selenium Grid**.
  - `docker`: Runs tests inside **Docker containers**.
  - `cloud`: Executes tests on **BrowserStack**.
- **TestNG & Parallel Execution**: Supports **TestNG test suites** for single or parallel execution.
- **Cloud Testing with BrowserStack**: Configured for running tests on **BrowserStack**.
- **Configurable Test Execution**: Uses `config.properties` for execution settings.
- **Advanced Logging & Reporting**:
  - **Extent Reports** for HTML-based reports.
  - **Allure Reports** for advanced test execution insights.
- **Selenium Utility Methods**: Predefined methods in `ElementUtils`, `WaitUtils`, and `ValidationUtils` improve automation efficiency.

---

## **Project Structure**
### **1. `src/main/java/com/brighthorizon/test/automation/framework`**
Contains **framework utilities** and configurations:
- **`config/`**: Handles framework configuration (`ConfigReader`).
- **`constants/`**: Stores framework constants (e.g., `FrameworkConstants`).
- **`reporting/`**: Handles reporting (`AllureReportGenerator`, `ExtentReports`).
- **`utils/`**: Core utilities:
  - `selenium/` (e.g., `ElementUtils`, `WaitUtils`, `ValidationUtils`).
  - `exception/` (Handles custom exceptions).
  - `misc/` (Other helper functions).

### **2. `src/test/java/com/brighthorizon/test/automation/framework`**
Contains **test execution setup**:
- **`base/`**: Manages test setup (`WebBaseTest`).
- **`core/`**: Manages WebDriver instances (`DriverManager`).
- **`runner/`**: Contains **TestNG runners** (`TestRunner`, `CucumberTestRunner`).
- **`unittests/`**: Framework-level tests.

### **3. `src/test/java/com/brighthorizon/test/web`**
Contains **test implementation**:
- **`pageobjects/`**: Page Object Model (`BrightHorizonsHomePage`).
- **`stepdefs/`**: Cucumber Step Definitions (`SearchFunctionalitySteps`).
- **`hooks/`**: Cucumber Hooks (`CucumberHooks`).
- **`resources/feature-files/`**: Contains feature files (`search-functionality.feature`).

### **4. `resources/config/`**
Contains **configuration files**:
- **`config.properties`**: Main configuration file.
- **Environment-Specific Configurations**:
  - `qa.properties`
  - `uat.properties`
  - `prod.properties`
- **Driver Settings**:
  - `chrome-driver-path`
  - `firefox-driver-path`
  - `edge-driver-path`

### **5. `resources/drivers/`**
Stores browser drivers:
- **Chrome, Firefox, Edge drivers** for local execution.

---

## **Configuration (`config.properties`)**
The **`config.properties`** file defines key execution settings:

```properties
# Global Configuration
browser=chrome
element-visibility-timeout=10
element-clickable-timeout=20
report.type=extent
platform=web
application.name=BrightHorizon
execution-platform=local-driver
cloud-provider=browserstack
grid-url=http://localhost:4444/wd/hub
chrome-driver-path=src/main/resources/drivers/chromedriver/chromedriver.exe
firefox-driver-path=src/main/resources/drivers/geckodriver/geckodriver.exe
edge-driver-path=src/main/resources/drivers/edgedriver/msedgedriver.exe
```

### **Key Parameters**
- **`browser`**: Browser type (`chrome`, `firefox`, `edge`).
- **`execution-platform`**:
  - `local-driver`: Runs on a local browser.
  - `grid`: Runs on Selenium Grid.
  - `docker`: Runs in a Docker container.
  - `cloud`: Runs on BrowserStack.
- **`cloud-provider=browserstack`**: Enables cloud execution via BrowserStack.
- **`report.type=extent`**: Defines the reporting format (`allure`, `extent`, `testng`).

---

## **Driver Management**
The `DriverManager` class dynamically initializes WebDriver based on the **execution platform**.

### **Execution Platforms**
| Platform            | Description |
|---------------------|-------------|
| `local-driver` | Uses locally installed browsers. |
| `local-driver-manager` | Uses WebDriverManager for driver setup. |
| `grid` | Runs tests on a **Selenium Grid**. |
| `docker` | Executes tests inside **Docker containers**. |
| `cloud` | Executes tests on **BrowserStack**. |

### **Cloud Execution with BrowserStack**
When `cloud-provider=browserstack`, the `DriverManager` will:
- Use **BrowserStack credentials**.
- Fetch the **OS, browser, and device** from `BrowserStackRunner`.
- Execute tests using **RemoteWebDriver** on BrowserStack.

---

## **How to Run Tests**
### **Using TestNG Configuration Files**
The framework includes **4 TestNG XML files** for different execution modes:

| TestNG File | Description |
|-------------|-------------|
| `testng-single-browser.xml` | Runs tests on a **single browser**. |
| `testng-cross-browsers.xml` | Executes tests on **multiple browsers in parallel**. |
| `testng-browserstack.xml` | Runs tests on **BrowserStack**. |
| `testng-browserstack-mob-chrome.xml` | Runs tests on **mobile browsers via BrowserStack**. |

### **Run Tests via Maven**
Execute **specific TestNG files**:
```sh
mvn test -DsuiteXmlFile=testng-single-browser.xml
mvn test -DsuiteXmlFile=testng-cross-browsers.xml
mvn test -DsuiteXmlFile=testng-browserstack.xml
```

### **Run Tests on Selenium Grid**
Ensure **Selenium Grid is running** (`http://localhost:4444/wd/hub`):
```sh
mvn test -Dexecution-platform=grid
```

### **Run Tests in Docker**
```sh
mvn test -Dexecution-platform=docker
```

### **Run Tests on BrowserStack**
```sh
mvn test -Dexecution-platform=cloud -Dcloud-provider=browserstack
```

---

## **Reporting**
- **Extent Reports**: Found under `target/reports/extent-report.html`.
- **Allure Reports**: Generate with:
  ```sh
  allure serve target/allure-results
  ```

---

## **Conclusion**
This framework enables **scalable web automation testing** with **TestNG, Selenium Grid, Docker, and BrowserStack**, supporting **parallel execution**, **cloud testing**, and **detailed reporting**. 🚀

