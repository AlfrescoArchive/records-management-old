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
package org.alfresco.test;

import java.util.List;

import org.alfresco.po.common.util.Utils;
import org.openqa.selenium.WebDriver;
import org.testng.IReporter;
import org.testng.ISuite;
import org.testng.xml.XmlSuite;

/**
 * Resource teardown.
 * <p>
 * Uses the reporter callback to clear down any resources 
 * once everything has been completed.
 * 
 * @author Roy Wetherall
 */
public class ResourceTeardown implements IReporter
{
    /**
     * @see org.testng.IReporter#generateReport(java.util.List, java.util.List, java.lang.String)
     */
    @Override
    public void generateReport(List<XmlSuite> xmlSuites, List<ISuite> suites, String outputDirectory)
    {
        // quit web drone if still open
        WebDriver webDriver = Utils.getWebDriver();
        if (webDriver != null)
        {
            webDriver.quit();
        }
    }

}
