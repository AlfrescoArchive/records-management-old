/*
 * Copyright (C) 2005-2014 Alfresco Software Limited.
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
package org.alfresco.po.common.util;

import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStreamWriter;
import java.nio.charset.Charset;
import java.util.Collection;

import org.apache.commons.lang3.StringUtils;
import org.openqa.selenium.*;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.internal.WrapsElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;

import ru.yandex.qatools.htmlelements.element.TypifiedElement;

/**
 * Utility class containing helpful methods.
 * 
 * @author Tuna Aksoy
 * @since 2.2
 * @version 1.0
 */
@Component
public final class Utils implements ApplicationContextAware
{
    /** application context */
    private static ApplicationContext applicationContext;
    
    /**
     * @see org.springframework.context.ApplicationContextAware#setApplicationContext(org.springframework.context.ApplicationContext)
     */
    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException
    {
        Utils.applicationContext = applicationContext;
    }

    /**
     * Helper to get web driver
     * 
     * @return
     */
    public static WebDriver getWebDriver()
    {
        return (WebDriver)Utils.applicationContext.getBean("webDriver");
    }

    /**
     * Helper to get web driver wait object
     * 
     * @return
     */
    public static WebDriverWait webDriverWait()
    {
        return new WebDriverWait(getWebDriver(), 10);
    }
    
    /**
     * Helper method to see if element exists on page
     */
    public static boolean elementExists(By selector)
    {
        try
        {
            getWebDriver().findElement(selector);
            return true;
        }
        catch (NoSuchElementException exception)
        {
            return false;
        }
    }
    
    /**
     * Helper method to see if element exists within element
     */
    public static boolean elementExists(WebElement webElement, By selector)
    {
        try
        {
            webElement.findElement(selector);
            return true;
        }
        catch (NoSuchElementException exception)
        {
            return false;
        }
        catch (StaleElementReferenceException exception)
        {
            webElement.findElement(selector);
            return true;
        }
    }
    
    /**
     * Helper method to wait for the staleness of an element
     */
    public static void waitForStalenessOf(WebElement webElement)
    {
        webDriverWait().until(ExpectedConditions.stalenessOf(webElement));
    }
    
    /**
     * @see Utils#waitForStalenessOf(WebElement)
     */
    public static void waitForStalenessOf(TypifiedElement webElement)
    {
        waitForStalenessOf(webElement.getWrappedElement());
    }
    
    /**
     * Helper method to wait for the visibility of an element
     */
    public static void waitForVisibilityOf(WebElement webElement)
    {
        webDriverWait().until(ExpectedConditions.visibilityOf(webElement));
    }
    
    /**
     * @see Utils#waitForVisibilityOf(WebElement)
     */
    public static void waitForVisibilityOf(TypifiedElement webElement)
    {
        waitForVisibilityOf(webElement.getWrappedElement());
    }
    
    /**
     * Helper method to wait for the visibility of element located
     * by selector
     */
    public static void waitForVisibilityOf(By locator)
    {
        webDriverWait().until(ExpectedConditions.visibilityOfElementLocated(locator));
    }
    
    /**
     * Helper method to wait for the invisibility of an element located
     * by selector
     */
    public static void waitForInvisibilityOf(By locator)
    {
        webDriverWait().until(ExpectedConditions.invisibilityOfElementLocated(locator));
    }

    /**
     * Helper to mouse over element
     * 
     * @param webElement
     * @return
     */
    public static <T extends WebElement> T mouseOver(T webElement)
    {
        Actions actions = new Actions(getWebDriver());
        actions.moveToElement(webElement).perform();
        return webElement;
    }

    /**
     * Heler method to mouse over element
     * 
     * @param wrapsElement
     * @return
     */
    public static <T extends WrapsElement> T mouseOver(T wrapsElement)
    {
        mouseOver(wrapsElement.getWrappedElement());
        return wrapsElement;
    }

    /**
     * Clear control and enter text
     */
    public static <T extends WebElement> T clearAndType(T field, String text)
    {
        checkMandotaryParam("field", field);
        checkMandotaryParam("text", text);
        field.clear();
        field.sendKeys(text);
        return field;
    }

    /**
     * Clear control and enter text
     */
    public static <T extends WrapsElement> T clearAndType(T field, String text)
    {
        clearAndType(field.getWrappedElement(), text);
        return field;
    }

    /**
     * Check mandatory parameter values
     * 
     * @param paramName parameter name
     * @param object object value
     */
    public static <E> void checkMandotaryParam(final String paramName, final Object object)
    {
        if (StringUtils.isBlank(paramName)) 
        { 
            throw new IllegalArgumentException(String.format(
                "The parameter paramName is required and can not be'%s'", paramName)); 
        }
        if (object == null) 
        { 
            throw new IllegalArgumentException(String.format(
                "'%s' is a mandatory parameter and must have a value", paramName)); 
        }
        if (object instanceof String && StringUtils.isBlank((String) object)) 
        { 
            throw new IllegalArgumentException(
                String.format("'%s' is a mandatory parameter", paramName)); 
        }
        if (object instanceof Collection<?> && ((Collection<?>) object).isEmpty()) 
        { 
            throw new IllegalArgumentException(
                String.format("'%s' is a mandatory parameter and can not be empty", paramName)); 
        }
    }

    /**
     * Create temp file TODO .. support multiple mimetypes .. build files with
     * real size content
     * 
     * @param name file name
     * @return {@link File} file
     */
    public static File createTempFile(final String name)
    {
        try
        {
            // create file
            File file = File.createTempFile(name, ".txt");

            // create writer
            OutputStreamWriter writer = new OutputStreamWriter(new FileOutputStream(file), Charset.forName("UTF-8")
                    .newEncoder());
            try
            {
                // place content in file
                writer.write("this is a sample test upload file");
            }
            finally
            {
                // close writer
                writer.close();
            }

            return file;
        }
        catch (Exception exception)
        {
            throw new RuntimeException("Unable to create test file.");
        }
    }
}
