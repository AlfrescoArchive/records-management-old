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

package org.alfresco.po.rm.details.record;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class ClassifiedPropertiesPanel extends PropertiesPanel
{
    @FindBy(css="div.document-metadata-header h2")
    private WebElement classifiedTitle;

    private static By CLASSIFIED_PROPERTIES_SELECTOR = By.cssSelector("div.document-details-panel .set-panel .viewmode-value");

    public static Integer ORIGINAL_CLASSIFICATION = 0;
    public static Integer CURRENT_CLASSIFICATION = 1;
    public static Integer CLASSIFICATION_AUTHORITY = 2;
    public static Integer CLASSIFICATION_REASON = 3;

    public String getClassifiedProperty(Integer property)
    {
        return getClassifiedProperties().get(property).getText();
    }

    public List<WebElement> getClassifiedProperties()
    {
        return webDriver.findElements(CLASSIFIED_PROPERTIES_SELECTOR);
    }
}
