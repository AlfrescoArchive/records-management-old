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
package org.alfresco.po.rm.dialog.classification;

import static org.alfresco.po.common.util.Utils.clearAndType;

import java.util.ArrayList;
import java.util.List;

import org.alfresco.po.common.Dialog;
import org.alfresco.po.common.renderable.Renderable;
import org.alfresco.po.common.util.Retry;
import org.alfresco.po.common.util.Utils;
import org.alfresco.po.share.page.SharePage;
import org.openqa.selenium.By;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.springframework.stereotype.Component;

import ru.yandex.qatools.htmlelements.element.TextInput;

/**
 * The classify content dialog.
 *
 * @author tpage
 */
@Component
public class ClassifyContentDialog extends Dialog
{
    @FindBy(css=".dijitDialogCloseIcon")
    private WebElement closeButton;

    @FindBy(css="#LEVELS_CONTROL")
    private WebElement levelSelectButton;

    @FindBy(css="#LEVELS_CONTROL .dijitSelectLabel")
    private WebElement selectedLevel;

    @FindBy(css="#LEVELS_CONTROL_menu")
    private WebElement levelsMenu;

    @FindBy(css="#AUTHORITY .dijitInputContainer input")
    private TextInput authorityTextInput;

    @FindBy(css="#REASONS_CONTROL .alfresco-forms-controls-MultiSelect__container")
    private WebElement reasonsContainer;

    @FindBy(css="#REASONS_CONTROL input")
    private TextInput reasonTextInput;

    @FindBy(css="#REASONS_CONTROL_RESULTS")
    private WebElement reasonsResultsContainer;

    /** Here we rely on the create button being the first in the footer. */
    @FindBy(css=".footer .alfresco-buttons-AlfButton:nth-child(1) [role=button]")
    private WebElement createButton;

    /** Here we rely on the cancel button being the second in the footer. */
    @FindBy(css=".footer .alfresco-buttons-AlfButton:nth-child(2) [role=button]")
    private WebElement cancelButton;

    /**
     * Set the classification level.
     *
     * @param levelId The label on the classification level dropdown button.
     * @return The dialog to allow chaining of actions.
     */
    public ClassifyContentDialog setLevel(String levelId)
    {
        // Open the dropdown menu.
        levelSelectButton.click();
        
        // Choose the appropriate option by the label.
        String selector = "tr[aria-label='" + levelId + " '] td[class$='dijitMenuItemLabel']";
        
        // retry since wait seems unreliable
        WebElement level = Utils.retry(new Retry<WebElement>()
        {
			@Override
			public WebElement execute() 
			{
				return levelsMenu.findElement(By.cssSelector(selector));
			}
		}, 5);
         
        // select the right level
        level.click();
        return this;
    }

    public String getLevel()
    {
        return selectedLevel.getText();
    }

    public ClassifyContentDialog setAuthority(String authority)
    {
        clearAndType(authorityTextInput, authority);
        return this;
    }

    /**
     * Search for a classification reason then, when the dropdown options are visible, click on the desired reason.
     *
     * @param id The classification reason id (e.g. "1.4(c)") - note this is currently not displayed on the page.
     * @return The dialog to allow chaining of actions.
     */
    public ClassifyContentDialog addReason(String id)
    {
        // Clearing the search box activates the drop-down. Assume that the classification reason is on the first page of results.
        reasonTextInput.getWrappedElement().clear();
        String selector = "#REASONS_CONTROL_RESULTS .alfresco-forms-controls-MultiSelect__results__result[data-aikau-value='" + id + "']";
        Utils.waitForVisibilityOf(By.cssSelector(selector));
        WebElement reason = reasonsResultsContainer.findElement(By.cssSelector(selector));
        reason.click();
        return this;
    }

    /**
     * Remove a reason that has previously been selected.
     *
     * @param id The classification reason id (e.g. "1.4(c)") - note this is currently not displayed on the page.
     * @return The dialog to allow chaining of actions.
     * @throws NoSuchElementException If the reason with the given id has not been applied to the content.
     */
    public ClassifyContentDialog removeSelectedReason(String id) throws NoSuchElementException
    {
        String selector = "[data-aikau-value='" + id + "'] ~ a";
        WebElement removeButton = reasonsContainer.findElement(By.cssSelector(selector));
        removeButton.click();
        return this;
    }

    public Renderable submitDialog()
    {
        createButton.click();
        return SharePage.getLastRenderedPage().render();
    }

    public Renderable cancelDialog()
    {
        cancelButton.click();
        Utils.waitForInvisibilityOf(cancelButton);
        return SharePage.getLastRenderedPage().render();
    }

    public Renderable closeDialog()
    {
        closeButton.click();
        Utils.waitForInvisibilityOf(closeButton);
        return SharePage.getLastRenderedPage().render();
    }

    /** @return <code>true</code> if the dialog is visible (actually check if the "create" button is visible). */
    public boolean isDisplayed()
    {
        return (createButton != null && createButton.isDisplayed());
    }

    /** @return <code>true</code> if the create button is visible and enabled. */
    public boolean isCreateButtonEnabled()
    {
        if (createButton == null || !createButton.isDisplayed())
        {
            return false;
        }
        String disabledAttribute = createButton.getAttribute("aria-disabled");
        return (disabledAttribute == null || disabledAttribute.toLowerCase().equals("false"));
    }

    /** @return The classification authority entered. */
    public String getAuthority()
    {
        return authorityTextInput.getText();
    }

    /** @return The list of selected classification reasons. */
    public List<String> getReasons()
    {
        List<WebElement> reasons = reasonsContainer.findElements(By
                    .cssSelector(".alfresco-forms-controls-MultiSelect__choice__content"));
        List<String> reasonIds = new ArrayList<String>();
        for (WebElement reason : reasons)
        {
            String reasonId = reason.getAttribute("data-aikau-value");
            reasonIds.add(reasonId);
        }
        return reasonIds;
    }
}
