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
import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

import ru.yandex.qatools.htmlelements.element.Button;
import ru.yandex.qatools.htmlelements.element.HtmlElement;
import ru.yandex.qatools.htmlelements.element.Link;

/**
 * Disposition block of the category details page
 *
 * @author Tatiana Kalinovskaya
 */
public class DispositionBlock extends HtmlElement
{

    /** Create Disposition Schedule button */
    @FindBy(css = "button[id*='createschedule-button']")
    private Button createDispositionScheduleButton;

    /** Edit disposition general information button */
    @FindBy(css = "button[id*='editproperties-button']")
    private  Button editDispositionGeneralButton;

    /** Edit disposition steps button */
    @FindBy(css = "button[id*='editschedule-button']")
    private  Button editDispositionStepsButton;

    /** disposition title */
    @FindBy (css =".disposition .title")
    private WebElement dispositionTitle;

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

    /** view step description link*/
    @FindBy  (css = ".more")
    private List<Link> viewDescription;

    /**step description */
    @FindBy (css = ".description")
    private List <WebElement> stepDescription;

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

    public String getStepDescription(int number)
    {
        return stepDescription.get(number-1).getText();
    }

    /** unpublished updates available
     * @return true if "Yes", els false
     */
    public boolean isUnpublishedUpdateAvailable()
    {
        return (unpublishedUpdates.getText().equals("Yes")) ? true : false;
    }

    public boolean isCreateDispositionScheduleEnabled()
    {
        return createDispositionScheduleButton.isEnabled();
    }

    public boolean isDispositionScheduleCreated()
    {
        return !dispositionTitle.getText().equals("No disposition schedule found");
    }

    public void clickOnViewDescription(int number)
    {
        viewDescription.get(number - 1).click();
    }

    public void clickOnCreateDispositionSchedule()
    {
        createDispositionScheduleButton.click();
    }

    public void clickOnEditDispositionGeneral()
    {
        editDispositionGeneralButton.click();
    }

    public void clickOnEditDispositionSteps()
    {
        editDispositionStepsButton.click();
    }
}
