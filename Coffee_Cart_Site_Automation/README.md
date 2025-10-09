# ☕ Coffee Cart Site Automation

This repository contains an **automated test suite** for the [Coffee Cart](https://coffee-cart.app/) demo web application.  
It uses **Java**, **Selenium WebDriver**, **TestNG**, and the **Page Object Model** design pattern to automate UI testing scenarios like adding items to the cart, updating quantities, verifying totals, and checking out.

---

## 📁 Project Structure

```
Coffee_Cart_Site_Automation/
├── src/
│   ├── main/
│   │   ├── java/
│   │   │   └── pages/            # Page Object classes (CartPage, MenuPage, Checkout, BasePage)
│   │   └── resources/           # Configuration files (e.g., config.properties)
│   └── test/
│       ├── java/
│       │   └── tests/           # TestNG test classes (CartTests, CheckoutTests)
│       └── resources/           # Test data files (ExcelReader, etc.)
├── pom.xml                       # Maven build file (if using Maven)
└── README.md
```

---

## 🛠️ Tech Stack

- **Language:** Java 17+  
- **Automation Tool:** Selenium WebDriver  
- **Test Runner:** TestNG  
- **Design Pattern:** Page Object Model (POM)  
- **Logging:** Log4j / custom LoggerUtils  
- **Build Tool:** Maven  
- **Browser:** Chrome (default)

---

## ⚙️ Prerequisites

- Install **Java JDK** (version 8 or above)  
- Install **Maven** and add it to your PATH  
- Install **Git**  
- Download **ChromeDriver** compatible with your Chrome browser version  
- An IDE such as IntelliJ IDEA / Eclipse / VS Code

---

## 🧭 Configuration

All configuration settings are stored in:
```
src/main/resources/config.properties
```

Typical properties include:
```
base_url=https://coffee-cart.app/
browser=chrome
implicit_wait=10
explicit_wait=15
```

---

## 🚀 How to Run the Tests

### 🧰 1. Clone the Repository
```bash
git clone https://github.com/amaankhan67/Capstone_Project_QAE.git
cd Capstone_Project_QAE/Coffee_Cart_Site_Automation
```

### 🧪 2. Install Dependencies
```bash
mvn clean install
```

### ▶️ 3. Run Tests
```bash
mvn test
```

Or run a specific test class from your IDE (e.g., `CartTests` or `CheckoutTests`).

### 📊 4. View Test Reports
After test execution, reports are generated at:
```
target/surefire-reports
```

You can open the HTML report in a browser.

---

## 🧪 Test Scenarios Automated

- Verify cart is empty on initial load  
- Add single item and verify presence in cart  
- Validate total price calculation for single/multiple items  
- Increase/decrease item quantity and check price updates  
- Remove item when quantity reaches zero  
- Verify multiple items presence in cart  
- Validate total cart price for multiple items  
- Navigate to Checkout page after clicking Checkout button

---

## 🧭 Utilities Used

- `WaitUtils` for explicit waits and synchronization  
- `LoggerUtils` for structured logging  
- `ExcelReader` for data-driven testing  
- `DriverManager` for WebDriver lifecycle handling

---

## 👨‍💻 Author

**Amaan Khan**  
📎 [GitHub Profile](https://github.com/amaankhan67)
