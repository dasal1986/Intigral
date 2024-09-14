Selenium WebDriver Java Gradle Framework
Welcome to the Selenium WebDriver Java Gradle Framework repository. This project is designed to facilitate automated testing using Selenium WebDriver for frontend testing and REST Assured for API testing.

Features
Frontend Testing: Automated tests for web applications using Selenium WebDriver.
API Testing: Automated tests for RESTful APIs using REST Assured.
Allure Reports: Detailed test reports including pass/fail status, execution time, and screenshots.

Tests Included
This framework includes the following tests:
**testSubscriptionPackages**: Validates the functionality related to subscription packages.
**testAddDevice**: Checks the device creation using API.

Getting Started
To get started with this project, follow the instructions below:

Prerequisites
Ensure you have the following installed on your machine:

Java JDK **17.0.10**
Gradle
Cloning the Repository
Clone the repository using the following command:
git clone https://github.com/dasal1986/Intigral.git

Running Tests
To execute the tests and generate the Allure report, use the following Gradle command:
**gradle clean test -Dtags="SampleTest" --continue allureReport**

This command will:
Clean the project
Run the tests with the tag SampleTest
Continue execution despite any test failures
Generate an Allure report with test results, including pass/fail status, execution time, and screenshots.

Reports
After execution, the **Allure report** will be available with:
Test Status: Pass or Fail
Execution Time: Time taken for each test
Screenshots: Last screenshot taken during test execution

Contributing
Feel free to open issues or submit pull requests for improvements. Contributions are welcome!
