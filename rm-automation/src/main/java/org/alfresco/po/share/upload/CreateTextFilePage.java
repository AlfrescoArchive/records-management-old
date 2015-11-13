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

import org.alfresco.po.common.util.Utils;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.springframework.stereotype.Component;

/**
 *
 * @author Oana Nechiforescu
 */
@Component
public class CreateTextFilePage 
{
    @FindBy(css = "input[id*='default_prop_cm_name']")
    private WebElement nameInput;

    @FindBy(css = "input[id*='default_prop_cm_title']")
    private WebElement titleInput;

    @FindBy(css = "textarea[id*='default_prop_cm_content']")
    private WebElement contentTextarea;

    @FindBy(css = "button[id*='default-form-submit-button']")
    private WebElement createButton;
    @FindBy(css = "button[id*='default-form-cancel-button']")
    private WebElement cancelButton;
    
    public void createTextFile(String fileName, String content)
    {
        Utils.waitForVisibilityOf(By.cssSelector("button[id*='default-form-submit-button']"));
        Utils.waitForVisibilityOf(By.cssSelector("input[id*='default_prop_cm_name']")).sendKeys(fileName);
        Utils.waitForVisibilityOf(By.cssSelector("textarea[id*='default_prop_cm_content']")).sendKeys(content);
        Utils.getWebDriver().findElement(By.cssSelector("button[id*='default-form-submit-button']")).click();
        Utils.waitForVisibilityOf(By.cssSelector(".textLayer"));
    }        
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
}
