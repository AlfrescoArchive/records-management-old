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
package org.alfresco.po.rm.disposition.edit.steps;

import static org.alfresco.po.common.util.Utils.clearAndType;

import org.alfresco.po.common.util.Utils;
import org.alfresco.po.share.page.SharePage;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

import ru.yandex.qatools.htmlelements.element.Button;
import ru.yandex.qatools.htmlelements.element.CheckBox;
import ru.yandex.qatools.htmlelements.element.HtmlElement;
import ru.yandex.qatools.htmlelements.element.Link;
import ru.yandex.qatools.htmlelements.element.Select;
import ru.yandex.qatools.htmlelements.element.TextInput;

/**
 * Disposition Step block of Edit Disposition Schedule page
 *
 * @author Tatiana Kalinovskaya
 */
public class DispositionStepBlock extends HtmlElement
{
    /** edit icon */
    @FindBy (css = ".edit")
    private Link editIcon;

    /** delete icon */
    @FindBy (css = ".delete")
    private Link deleteIcon;

    /** transfer location select-box */
    @FindBy (css = "select.action-location")
    private Select transferLocationSelectBox;

    /** period section */
    @FindBy (xpath = ".//div[input[@class='period-enabled']]")
    private PeriodSection periodSection;

    /** events check-box */
    @FindBy (css = ".events-enabled")
    private CheckBox eventsCheckbox;

    /** event section */
    @FindBy (xpath = ".//div[@class = 'section events']")
    private EventsSection eventsSection;

    /** maintain record metadata after destroy  check-box*/
    @FindBy (css = ".ghostOnDestroy")
    private CheckBox ghostOnDestroyCheckbox;

    /** description */
    @FindBy (css = "textarea.description")
    private TextInput descriptionInput;

    /** save button */
    @FindBy(css="span[class*='saveaction']")
    private Button saveButton;

    /** cancel button */
    @FindBy(css="span[class*='cancel']")
    private Button cancelButton;

    /** name of the step */
    @FindBy (css = ".title")
    private WebElement stepTitle;

    /**
     * get step title
     * @return title of the step
     */
    public String getStepTitle()
    {
        return stepTitle.getText();
    }
    /**
     * click on edit icon
     */
    public void clickOnEdit()
    {
        editIcon.click();
    }

    /**
     * click on Delete icon
     */
    public void clickOnDelete()
    {
        deleteIcon.click();
    }
    /**
     * Get Transfer Location
     */
    public String getTransferLocation()
    {
        WebElement selectedElement = transferLocationSelectBox.getFirstSelectedOption();
        return selectedElement.getText();
    }

    /**
     * Get Period
     */
    public PeriodSection getPeriodSection()
    {
        return periodSection;
    }

    /**
     * Is event checkbox selected
     */
    public Boolean isEventsCheckboxSelected()
    {
        return eventsCheckbox.isSelected();
    }

    /**
     * Get Events Section
     */
    public EventsSection getEventsSection()
    {
        return eventsSection;
    }

    /**
     * Is ghostOnDestroy Checkbox selected
     */
    public Boolean isGhostOnDestroyCheckboxSelected()
    {
        return ghostOnDestroyCheckbox.isSelected();
    }

    /**
     * Get description
     */
    public String getDescription()
    {
        return descriptionInput.getText();
    }

    /**
     * Set Location
     */
    public DispositionStepBlock setLocation(String transferLocation)
    {
        transferLocationSelectBox.selectByValue(transferLocation);
        return this;
    }

    /**
     * Check events checkbox
     */
    public DispositionStepBlock checkEvents(boolean check)
    {
        eventsCheckbox.set(check);
        SharePage.getLastRenderedPage().render();
        return this;
    }

    /** add event
     * @param eventName
     * @return DispositionStepBlock
     */
    public DispositionStepBlock addEvent(String eventName)
    {
        this.checkEvents(true);
        eventsSection.selectEvent(eventName);
        SharePage.getLastRenderedPage().render();
        return this;
    }

    /** remove event
     *
     * @param eventNumber
     * @return DispositionStepBlock
     */
    public DispositionStepBlock removeEvent(int eventNumber)
    {
        eventsSection.deleteEvent(eventNumber);
        SharePage.getLastRenderedPage().render();
        return this;
    }
    /**
     * Check ghost checkbox
     */
    public DispositionStepBlock checkGhostOnDestroy(boolean check)
    {
        ghostOnDestroyCheckbox.set(check);
        return this;
    }

    /**
     * Set Description
     */
    public DispositionStepBlock setDescription(String description)
    {
        clearAndType(descriptionInput, description);
        return this;
    }

    /**
     * Click on save
     */
    public EditDispositionSchedulePage clickOnSave()
    {
        saveButton.click();
        return SharePage.getLastRenderedPage().render();
    }

    /**
     * Click on cancel
     */
    public EditDispositionSchedulePage clickOnCancel()
    {
        cancelButton.click();
        Utils.waitForStalenessOf(cancelButton);
        return SharePage.getLastRenderedPage().render();
    }
}
