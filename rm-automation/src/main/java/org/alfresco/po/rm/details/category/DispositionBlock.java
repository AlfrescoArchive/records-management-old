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
package org.alfresco.po.rm.details.category;

import java.text.MessageFormat;

import org.openqa.selenium.By;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

import ru.yandex.qatools.htmlelements.element.HtmlElement;

/**
 * Disposition block of the category details page
 *
 * @author Tatiana Kalinovskaya
 */
public class DispositionBlock extends HtmlElement
{
    /** disposition authority */
    @FindBy (xpath = ".//div[@class='properties']/div[1]/span[2]")
    private HtmlElement dispositionAuthority;

    /** disposition instructions */
    @FindBy (xpath = ".//div[@class='properties']/div[2]/span[2]")
    private HtmlElement dispositionInstructions;

    /** applied to */
    @FindBy (xpath = ".//div[@class='properties']/div[3]/span[2]")
    private HtmlElement appliedTo;

    /** unpublished updates availability */
    @FindBy (xpath = ".//div[@class='properties']/div[4]/span[2]")
    private HtmlElement unpublishedUpdates;

    /** disposition steps */
    @FindBy (css = ".actions")
    private WebElement dispositionSteps;

    private static final String DISPOSITION_STEP_SELECTOR_XPATH = ".//div[{0}]/div[@class=''name'']";
    
    public String getDispositionAuthority()
    {
        return dispositionAuthority.getText();
    }

    public String getDispositionInstructions()
    {
        return dispositionInstructions.getText();
    }

    public String getAppliedTo()
    {
        return appliedTo.getText();
    }

    public boolean isUnpublishedUpdateAvailable()
    {
        return (unpublishedUpdates.getText().equals("Yes")) ? true : false;
    }

    /**
     * get quantity of steps
     */
    public int getStepsQuantity()
    {
        return Integer.parseInt(dispositionSteps.getAttribute("childElementCount"));
    }

    /**
     * get disposition step name by number
     * @param stepNumber
     */
    public String getDispositionStepName(int stepNumber)
    {
        try
        {
            return dispositionSteps
                    .findElement(getDispositionStepSelector(stepNumber))
                    .getText();
        }
        catch (NoSuchElementException e)
        {
            // do nothing, just return null
        }
        return null;
    }

    /**
     * Helper method to get the disposition step selector
     */
    private By getDispositionStepSelector(int stepNumber)
    {
        String eventXPATH = MessageFormat.format(DISPOSITION_STEP_SELECTOR_XPATH, stepNumber);
        return By.xpath(eventXPATH);
    }
    
}
