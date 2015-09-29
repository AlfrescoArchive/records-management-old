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

import java.util.List;
import org.alfresco.po.common.ConfirmationPrompt;
import org.alfresco.po.rm.details.category.CategoryDetailsPage;
import org.alfresco.po.share.page.SharePage;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import ru.yandex.qatools.htmlelements.element.Button;
import ru.yandex.qatools.htmlelements.element.Link;

/**
 * Edit disposition schedule (steps) page
 *
 * @author Tatiana Kalinovskaya
 */
@Component
public class EditDispositionSchedulePage extends SharePage
{
    /** Add Step button */
    @FindBy(css = "button[id*='createaction-button']")
    private Button addStepButton;

    /** list of actions */
    @FindBy (css = "div.flow-buttons ul.first-of-type li")
    private List<Link> actions;

    /** done button */
    @FindBy(css="button[id*='done-button']")
    private Button doneButton;

    /** expanded disposition steps*/
    @FindBy (css = "li[class$='expanded']")
    private List<DispositionStepBlock> expandedDispositionSteps;

    /** all disposition steps*/
    @FindBy (css = ".action-list .action")
    private List<DispositionStepBlock> dispositionSteps;

    @FindBy (css = ".edit")
    private WebElement editIcon;

    @FindBy (css = ".delete")
    private WebElement deleteIcon;

    /** category details page */
    @Autowired
    private CategoryDetailsPage categoryDetailsPage;

    /** confirmation prompt */
    @Autowired
    private ConfirmationPrompt prompt;

    /**
     * is disposition action enabled in drop-down
     * @param actionIndex
     * @return true if the action is enabled
     */
    public boolean isActionEnabled(int actionIndex)
    {
        return actions.get(actionIndex).isEnabled();
    }

    /**
     * is disposition action drop-down expanded
     * @return true if the dropdown is expanded
     */
    public boolean isDropdownExpanded()
    {
        return actions.get(1).isDisplayed();
    }

    /**
     * get last expanded disposition step
     */
    public DispositionStepBlock getLastExpandedDispositionStepBlock()
    {
        return expandedDispositionSteps.get(expandedDispositionSteps.size() - 1);
    }

    /**
     * get disposition step by number
     */
    public DispositionStepBlock getDispositionStepBlock(int number)
    {
        return dispositionSteps.get(number-1);
    }

    /**
     * get quantity of disposition steps
     */
    public int getDispositionStepsQuantity()
    {
        return dispositionSteps.size();
    }

    public String getPageURL(String... context)
    {
        throw new UnsupportedOperationException("This page does not have a direct access URL set.");
    }

    /**
     * Click on Add Step button
     */
    public EditDispositionSchedulePage clickOnAddStep()
    {
        addStepButton.click();
        return this.render();
    }


    /**
     * helper method to add disposition action step
     * @param actionIndex - number of the step to add
     */
    public EditDispositionSchedulePage addStepAndRender(int actionIndex)
    {
        // click on add step button
        clickOnAddStep();

        // get disposition action link
        Link dispositionAction = actions.get(actionIndex);
        if (dispositionAction == null || !dispositionAction.isEnabled())
            throw new RuntimeException("The action " + "could not be added");
        dispositionAction.click();
        // render the EditDispositionSchedulePage
        return this.render();
    }


    /**
     * Add disposition action step
     * @param actionIndex - number of the step to add
     */
    public DispositionStepBlock addStep(int actionIndex)
    {
        addStepAndRender(actionIndex);
        return getLastExpandedDispositionStepBlock();
    }

    /**
     * Click on edit icon for the step with specified number
     * @param number
     * @return DispositionStepBlock
     */
    public DispositionStepBlock clickOnEdit(int number)
    {
        getDispositionStepBlock(number)
                .clickOnEdit();
        this.render();
        return getDispositionStepBlock(number);
    }


    /**
     * Click on delete icon for the step with specified number
     * @param number
     * @return ConfirmationPrompt
     */
    public ConfirmationPrompt clickOnDelete(int number)
    {
        getDispositionStepBlock(number).clickOnDelete();
        return prompt.render();
    }

    /**
     * Click on Done
     */
    public CategoryDetailsPage clickOnDone()
    {
        doneButton.click();
        return categoryDetailsPage.render();
    }


}
