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
package org.alfresco.po.share.upload;

import org.alfresco.po.share.details.document.DocumentDetails;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import static org.alfresco.po.common.util.Utils.waitForVisibilityOf;
import static org.alfresco.po.common.util.Utils.getWebDriver;

/**
 *
 * @author Oana Nechiforescu
 */
@Component
public class CreateTextFilePage
{
    @Autowired
    private DocumentDetails documentDetails;

    public DocumentDetails createTextFile(String fileName, String content)
    {
        waitForVisibilityOf(By.cssSelector("button[id*='default-form-submit-button']"));
        setName(fileName);
        setContent(content);
        return saveTextFile();
    }

    public void setName(String name)
    {
        waitForVisibilityOf(By.cssSelector("input[id*='default_prop_cm_name']")).sendKeys(name);
    }

    public void setContent(String content)
    {
        waitForVisibilityOf(By.cssSelector("textarea[id*='default_prop_cm_content']")).sendKeys(content);
    }

    public DocumentDetails saveTextFile() {
        getWebDriver().findElement(By.cssSelector("button[id*='default-form-submit-button']")).click();
        waitForVisibilityOf(By.cssSelector(".textLayer"));
        return documentDetails.render();
    }
}
