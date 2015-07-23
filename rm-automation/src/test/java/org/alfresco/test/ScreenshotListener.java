/*
 * Copyright (C) 2005-2015 Alfresco Software Limited.
 *
 * This file is part of Alfresco
 *
 * Alfresco is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * Alfresco is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with Alfresco. If not, see <http://www.gnu.org/licenses/>.
 */
package org.alfresco.test;

import java.io.IOException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.ITestContext;
import org.testng.ITestListener;
import org.testng.ITestResult;

/**
 * Screenshot listener class for taking screenshot on failures
 *
 * @author Tuna Aksoy
 * @since 3.0.a
 */
public class ScreenshotListener implements ITestListener
{
    /** Logger */
    private static final Logger LOGGER = LoggerFactory.getLogger(ScreenshotListener.class);

    /**
     * @see org.testng.ITestListener#onTestStart(org.testng.ITestResult)
     */
    @Override
    public void onTestStart(ITestResult result)
    {
    }

    /**
     * @see org.testng.ITestListener#onTestSuccess(org.testng.ITestResult)
     */
    @Override
    public void onTestSuccess(ITestResult result)
    {
    }

    /**
     * @see org.testng.ITestListener#onTestFailure(org.testng.ITestResult)
     */
    @Override
    public void onTestFailure(ITestResult result)
    {
        Object instance = result.getInstance();
        if (instance instanceof BaseTest)
        {
            BaseTest test = ((BaseTest) result.getInstance());
            try
            {
                test.takeScreenshot(result.getInstanceName() + "." + result.getName());
            }
            catch (IOException error)
            {
                LOGGER.error("An error is occurred while taking screenshot.");
            }
        }
        else
        {
            LOGGER.info("The test result is not instance of 'BaseTest' but '" + instance + "'.");
        }
    }

    /**
     * @see org.testng.ITestListener#onTestSkipped(org.testng.ITestResult)
     */
    @Override
    public void onTestSkipped(ITestResult result)
    {
        onTestFailure(result);
    }

    /**
     * @see org.testng.ITestListener#onTestFailedButWithinSuccessPercentage(org.testng.ITestResult)
     */
    @Override
    public void onTestFailedButWithinSuccessPercentage(ITestResult result)
    {
        onTestFailure(result);
    }

    /**
     * @see org.testng.ITestListener#onStart(org.testng.ITestContext)
     */
    @Override
    public void onStart(ITestContext context)
    {
    }

    /**
     * @see org.testng.ITestListener#onFinish(org.testng.ITestContext)
     */
    @Override
    public void onFinish(ITestContext context)
    {
    }
}
