package org.jtalks.tests.jcommune.webdriver;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.*;
import org.testng.xml.XmlSuite;

/** @author stanislav bashkirtsev */
public class SeleniumSessionListener implements ITestListener, IInvokedMethodListener {

    @Override
    public void onTestFailure(ITestResult result) {
        logger.info("TEST FAIL [{}] >>>", result.getMethod().getMethodName());
    }

    @Override
    public void onTestStart(ITestResult result) {
        logger.info("TEST START [{}] >>>", result.getMethod().getMethodName());
    }

    @Override
    public void onTestSuccess(ITestResult result) {

    }

    @Override
    public void onTestSkipped(ITestResult result) {

    }

    @Override
    public void onTestFailedButWithinSuccessPercentage(ITestResult result) {

    }


    @Override
    public void onStart(ITestContext context) {

    }

    @Override
    public void onFinish(ITestContext context) {
        if (seleniumConfig != null) {
            seleniumConfig.destroy();
        }
    }

    @Override
    public void beforeInvocation(IInvokedMethod method, ITestResult testResult) {
        ITestClass testClass = method.getTestMethod().getTestClass();
        Class testClassToBeInvoked = testClass.getRealClass();
        if (testClassToBeInvoked != currentTestClass) {
            currentTestClass = testClassToBeInvoked;
            logger.info("STARTING TEST CLASS [{}] >>> >>>", currentTestClass.getSimpleName());
            if (seleniumConfig != null) {
                seleniumConfig.destroy();
            }

            seleniumConfig = new JCommuneSeleniumConfig();
            XmlSuite suite = testClass.getXmlTest().getSuite();
            try {
                seleniumConfig.init(suite.getParameter("webDriverUrl"), suite.getParameter("appUrl"));
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
            seleniumConfig.printSeleniumSessionId(testClass.getName());
        }
    }

    @Override
    public void afterInvocation(IInvokedMethod method, ITestResult testResult) {

    }

    private JCommuneSeleniumConfig seleniumConfig;
    /**
     * TestNG runs tests in groups split by class name. So first it runs all the tests in class 1, then in class 2. We
     * change the value of this field when first method of class 2 is invoked - this is a sign that we need to start a
     * new selenium session.
     */
    private Class currentTestClass;
    private final Logger logger = LoggerFactory.getLogger(SeleniumSessionListener.class);
}