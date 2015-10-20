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
package org.alfresco.po.rm.util;

import static org.alfresco.webdrone.WebDroneUtil.checkMandotaryParam;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;
import java.util.Random;

import org.alfresco.webdrone.ElementState;
import org.alfresco.webdrone.RenderElement;
import org.alfresco.webdrone.WebDrone;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.openqa.selenium.By;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebElement;

/**
 * Utility methods for the page objects
 *
 * @author Tuna Aksoy
 * @since 2.2
 */
public class RmPageObjectUtils
{
    private static Log logger = LogFactory.getLog(RmPageObjectUtils.class);
    private Properties prop = new Properties();
    /**
     * Helper method to check if a {@link WebElement} is displayed
     *
     * @param webDrone {@link WebDrone} The web drone instance
     * @param selector {@link By} The selector which is used to find the {@link WebElement}
     * @return <code>true</code> if the {@link WebElement} is visible <code>false</code> otherwise
     */
    public static boolean isDisplayed(WebDrone webDrone, By selector)
    {
        checkMandotaryParam("webDrone", webDrone);
        checkMandotaryParam("selector", selector);

        boolean isDisplayed = false;
        try
        {
            isDisplayed = webDrone.find(selector).isDisplayed();
        }
        catch (NoSuchElementException nse)
        {
        }
        return isDisplayed;
    }

    /**
     * Helper method to click on a {@link WebElement}
     *
     * @param webDrone {@link WebDrone} The web drone instance
     * @param selector {@link By} The css selector which is used to find the {@link WebElement}
     * @return
     */
    public static void select(WebDrone webDrone, By selector)
    {
        checkMandotaryParam("webDrone", webDrone);
        checkMandotaryParam("selector", selector);

        // use the render element helper to ensure the item is not only visible, but also
        // clickable
        RenderElement clickable = new RenderElement(selector, ElementState.CLICKABLE);
        clickable.render(webDrone, webDrone.getDefaultWaitTime());
        
        // we know the web element is there and ready now, so find and click
        WebElement webElement = webDrone.find(selector);
        webElement.click();
    }

    /**
     * This util method gets the random number for the given length of return
     * string.
     *
     * @param length int
     * @return String
     */
    public static String getRandomString(int length)
    {
        checkMandotaryParam("lenght", length);

        StringBuilder rv = new StringBuilder();
        Random rnd = new Random();
        char from[] = "abcdefghijklmnopqrstuvwxyz0123456789".toCharArray();

        for (int i = 0; i < length; i++)
            rv.append(from[rnd.nextInt((from.length - 1))]);
        return rv.toString();
    }

    /**
     * Load properties file by file name
     *
     * @param propertiesFile properties file name
     */
    public void loadProperties(String propertiesFile)
    {
        checkMandotaryParam("propertiesFile", propertiesFile);

        ClassLoader loader = Thread.currentThread().getContextClassLoader();
        InputStream in = loader.getResourceAsStream(propertiesFile);
        try
        {
            prop.load(in);
            in.close();
        }
        catch (IOException e)
        {
            logger.debug(e.getMessage());
        }
    }

    /**
     * Function returns property value by name
     *
     * @param name Name of property
     * @return String property value
     */
    public String getPropertyValue(String name)
    {
        checkMandotaryParam("name", name);

        return prop.getProperty(name);
    }
}