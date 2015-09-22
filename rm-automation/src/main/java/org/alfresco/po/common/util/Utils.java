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
package org.alfresco.po.common.util;

import static java.util.Arrays.asList;

import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStreamWriter;
import java.nio.charset.Charset;
import java.util.Collection;
import java.util.concurrent.TimeUnit;
import java.util.function.Supplier;

import org.apache.commons.lang3.StringUtils;
import org.openqa.selenium.By;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.StaleElementReferenceException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.interactions.MoveTargetOutOfBoundsException;
import org.openqa.selenium.internal.WrapsElement;
import org.openqa.selenium.support.ui.ExpectedCondition;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.FluentWait;
import org.openqa.selenium.support.ui.Wait;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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
    private static final Logger LOGGER = LoggerFactory.getLogger(Utils.class);

    /** application context */
    private static ApplicationContext applicationContext;

    /** default wait 10 seconds */
    private static final int DEFAULT_WAIT = 20;
    
    /** default retry 5 times */
    public static final int DEFAULT_RETRY = 5;

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
    public static WebDriverWait webDriverWait(long waitSeconds)
    {
        return new WebDriverWait(getWebDriver(), waitSeconds);
    }

    /**
     * Helper to get web driver wait object
     *
     * @return
     */
    public static WebDriverWait webDriverWait()
    {
        return webDriverWait(DEFAULT_WAIT);
    }

    /**
     * Helper method to see if element exists on page
     */
    public static boolean elementExists(By selector)
    {
        Wait<WebDriver> wait = new FluentWait<WebDriver>(getWebDriver())
                                        .withTimeout(1, TimeUnit.SECONDS);
        try
        {
            wait.until((webDriver) -> webDriver.findElement(selector));
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

    public static <T> void waitFor(ExpectedCondition<T> condition)
    {
        webDriverWait().until(condition);
    }
    
    /**
     * Waits for a web element to be found.
     * <p>
     * Throws TimeoutException if element is not found.
     * 
     * @param  selector             selector
     * @return {@link WebElement}   web element
     */
    public static WebElement waitForFind(By selector)
    {
        return new FluentWait<WebDriver>(getWebDriver())
                    .ignoring(NoSuchElementException.class)
                    .until((WebDriver webDriver) -> webDriver.findElement(selector));
    }
    
    /**
     * Waits for a web element to be found in the scope of another element.
     * <p>
     * Throws TimeoutException if element is not found.
     * 
     * @param webElement    web element to scope find
     * @param selector      web selector
     * @return {@link WebElement}   found web element
     */
    public static WebElement waitForFind(WebElement webElement, By selector)
    {
        return new FluentWait<WebElement>(webElement)
                    .ignoring(NoSuchElementException.class)
                    .until((WebElement w) -> w.findElement(selector));
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
    public static WebElement waitForVisibilityOf(By locator)
    {
        return webDriverWait().until(ExpectedConditions.visibilityOfElementLocated(locator));
    }

    /**
     * Helper method to wait for the invisibility of an element
     */
    public static void waitForInvisibilityOf(WebElement webElement)
    {
        webDriverWait().until(ExpectedConditions.not(ExpectedConditions.visibilityOf(webElement)));
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
    @SuppressWarnings("unchecked")
    public static <T extends WebElement> T mouseOver(T webElement)
    {
        return retry(() ->
        {
                Actions actions = new Actions(getWebDriver());
                actions.moveToElement(webElement).perform();
                return webElement;            
        }, 5, MoveTargetOutOfBoundsException.class);        
    }

    /**
     * Helper method to mouse over element
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
     * Clear control
     */
    public static <T extends WebElement> T clear(T field)
    {
        checkMandatoryParam("field", field);
        field.clear();
        return field;
    }

    /**
     * Clear control and enter text
     */
    public static <T extends WebElement> T clearAndType(T field, String text)
    {
        clear(field);
        checkMandatoryParam("text", text);
        field.sendKeys(text);
        
        // brief pause to allow the UI to process all the characters entered
        try{Thread.sleep(250);}catch(Exception e){};
        
        return field;
    }

    /**
     * Clear control and enter text
     */
    public static <T extends WrapsElement> T clear(T field)
    {
        clear(field.getWrappedElement());
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
     * Checks that the provided parameter satisfies all mandatory preconditions. These include:
     * <ul>
     *     <li>{@code paramName} should be non-null and not blank.</li>
     *     <li>{@code object} should be non-null.</li>
     *     <li>String {@code object}s should not be blank.</li>
     *     <li>Collection {@code object}s should not be empty.</li>
     * </ul>
     *
     * @param paramName parameter name
     * @param object object value
     * @throws IllegalArgumentException if any violations have occurred.
     */
    public static void checkMandatoryParam(final String paramName, final Object object)
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
            final File file = File.createTempFile(name, ".txt");

            // create writer
            try (FileOutputStream   fos    = new FileOutputStream(file);
                 OutputStreamWriter writer = new OutputStreamWriter(fos, Charset.forName("UTF-8").newEncoder()))
            {
                // place content in file
                writer.write("this is a sample test upload file");
            }

            return file;
        }
        catch (Exception exception)
        {
            throw new RuntimeException("Unable to create test file.", exception);
        }
    }

    /**
     * Helper method to retry the provided {@link Retry code block}, ignoring any {@code Exception}s until either
     * the code block completes successfully or the maximum number of retries has been reached.
     *
     * @param <T>       the return type from the code block.
     * @param retry     a code block to execute.
     * @param count     maximum number of retries.
     * @return          result of the code block.
     */
    @SuppressWarnings("unchecked")
    public static final <T> T retry(Retry<T> retry, int count)
    {
        return retry(retry, count, Exception.class);
    }

    /**
     * Helper method to retry the provided {@link Retry code block}, ignoring the specified exception types until either
     * the code block completes successfully or the maximum number of retries has been reached.
     *
     * @param <T>       the return type from the code block.
     * @param retry     a code block to execute.
     * @param count     maximum number of retries.
     * @param retryExceptions a sequence of exception types any one of which will start a retry.
     * @return          result of the code block.
     */
    @SuppressWarnings("unchecked")
    public static final <T> T retry(Retry<T> retry, int count, Class<? extends Exception>... retryExceptions)
    {
        int attempt = 0;

        while (true)
        {
            try
            {
                // try and execute
                return retry.execute();
            }
            catch (Exception exception)
            {
                LOGGER.debug("Exception ignored on attempt {}: {}", attempt, exception);

                // Is the caught exception a type that requires a retry?
                if (asList(retryExceptions)
                        .stream()
                        .anyMatch(e -> e.isAssignableFrom(exception.getClass())))
                {
                    attempt++;
                    // if we have used up all our retries throw the exception
                    if (attempt >= count)
                    {
                        throw exception;
                    }

                    // otherwise do nothing and try again
                }
                else
                {
                    throw exception;
                }
            }
        }
    }

    /**
     * Helper method to retry the provided {@link Retry code block} until the {@code predicate} block returns
     * {@code true} or the maximum number of retries has been reached.
     * Note that any exceptions thrown within the {@link Retry} will not be caught internally and will be thrown
     * immediately.
     *
     * @param retry     the code block to retry.
     * @param predicate a predicate code block which determines when the {@link Retry} is successfully completed.
     * @param count     the maximum number of retries.
     * @param <T>       the return type of the {@link Retry} block.
     * @return          the value returned by the {@link Retry} block.
     */
    public static final <T> T retryUntil(Retry<T> retry, Supplier<Boolean> predicate, int count)
    {
        for (int attempt = 0; attempt < count; attempt++)
        {
            T result = retry.execute();
            if (predicate.get()) return result;
        }
        throw new RuntimeException("Tried " + count + " times without successful completion.");
    }
}
