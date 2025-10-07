package listeners;

import java.io.File;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import org.apache.commons.io.FileUtils;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.testng.ITestContext;
import org.testng.ITestListener;
import org.testng.ITestResult;

import managers.DriverManager;
import utils.ConfigReader;
import utils.LoggerUtils;

public class TestListener implements ITestListener{
	
	WebDriver driver = DriverManager.getDriver();
	LoggerUtils logger = new LoggerUtils(TestListener.class);

	@Override
	public void onTestFailure(ITestResult result) {
		String testName = result.getName();
		takeScreenshot(testName);
		logFailureDetails(result);
	}
	
	private void takeScreenshot(String testName) {
		String timestamp = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMdd_HHmmss"));
        String screenshotName = testName + "_" + timestamp + ".png";
        
        File screenshot = ((TakesScreenshot) driver).getScreenshotAs(OutputType.FILE);
        try {
            FileUtils.copyFile(screenshot, 
                new File(ConfigReader.getProperty("screenshotpath") + screenshotName));
        } catch (Exception e) {
        	logger.error("Unable to Process Screenshot");
        }
	}
	
	@Override
    public void onFinish(ITestContext context) {
        logger.info("TEST SUITE FINISHED");
        logger.info("Total Passed: " + context.getPassedTests().size());
        logger.error("Total Failed: " + context.getFailedTests().size());
        logger.warn("Total Skipped: " + context.getSkippedTests().size());
    }
	
	private void logFailureDetails(ITestResult result) {
        Throwable throwable = result.getThrowable();
        if (throwable != null) {
            logger.error("Failure Exception Type: " + throwable.getClass().getSimpleName());
            logger.error("Failure Message: " + throwable.getMessage());
        }
    }
}
