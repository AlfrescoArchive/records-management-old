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

import static java.util.stream.Collectors.toList;
import static org.alfresco.po.common.util.Utils.clearAndType;
import static org.alfresco.po.common.util.Utils.retry;
import static org.alfresco.po.common.util.Utils.waitForInvisibilityOf;
import static org.alfresco.po.common.util.Utils.waitForVisibilityOf;

import java.util.List;

import org.alfresco.po.common.Dialog;
import org.alfresco.po.common.renderable.Renderable;
import org.alfresco.po.common.util.Utils;
import org.alfresco.po.share.page.SharePage;
import org.openqa.selenium.By;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.FluentWait;
import org.openqa.selenium.support.ui.Wait;
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

    @FindBy(css="#CLASSIFIED_BY .dijitInputContainer input")
    private TextInput classifiedByTextInput;

    @FindBy(css="#AGENCY .dijitInputContainer input")
    private TextInput agencyTextInput;

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
        final String selector = "tr[aria-label='" + levelId + " '] td[class$='dijitMenuItemLabel']";

        // get the classification level
        WebElement level = Utils.waitForFind(levelsMenu, By.cssSelector(selector));

        // select the right level
        level.click();
        return this;
    }

    public String getLevel()
    {
        return selectedLevel.getText();
    }

    public String getClassifiedBy()
    {
        return classifiedByTextInput.getText();
    }

    /**
     * Open the levels dropdown to find the potential options available to the current user.
     *
     * @return The list of levels in the order they were found on the page.
     */
    public List<String> getAvailableLevels()
    {
        // Open the dropdown menu.
        levelSelectButton.click();

        // Selector for all the options.
        final String selector = "td[class$='dijitMenuItemLabel']";

        // retry since wait seems unreliable
        List<WebElement> levels = retry(() -> levelsMenu.findElements(By.cssSelector(selector)), 5);

        return levels.stream()
                     .map(webElement -> webElement.getText())
                     .collect(toList());
    }

    /**
     * Set classification agency
     */
    public ClassifyContentDialog setAgency(String agency)
    {
        clearAndType(agencyTextInput, agency);
        return this;
    }

    /**
     * Set classified by
     */
    public ClassifyContentDialog setClassifiedBy(String classifiedBy)
    {
        clearAndType(classifiedByTextInput, classifiedBy);
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
        clearAndType(reasonTextInput, id);
        waitForVisibilityOf(reasonsResultsContainer);

        // try and find the reason
        String selectorValue = ".alfresco-forms-controls-MultiSelect__results__result[data-aikau-value='" + id + "']";
        By selector = By.cssSelector(selectorValue);

        // wait for the element to show
        Wait<WebDriver> wait = new FluentWait<WebDriver>(webDriver);
        wait.until(webDriver -> webDriver.findElement(selector));

        // click on the reason
        webDriver.findElement(selector).click();
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

    /**
     * Click on classify
     */
    public Renderable clickOnClassify()
    {
        createButton.click();
        waitForInvisibilityOf(createButton);
        return SharePage.getLastRenderedPage().render();
    }
    /**
     * Click on Classify from the Details Page
     */
    public Renderable clickOnClassifyFromDetailsPage()
    {
         createButton.click();
         Utils.waitFor(ExpectedConditions.invisibilityOfElementLocated(By.cssSelector(".footer .alfresco-buttons-AlfButton:nth-child(1) [role=button]")));
         return SharePage.getLastRenderedPage().render();
    }

    /**
     * Click on cancel
     */
    public Renderable clickOnCancel()
    {
        cancelButton.click();
        waitForInvisibilityOf(cancelButton);
        return SharePage.getLastRenderedPage().render();
    }

    /**
     * Close dialog
     */
    public Renderable closeDialog()
    {
        closeButton.click();
        waitForInvisibilityOf(closeButton);
        return SharePage.getLastRenderedPage().render();
    }

    /** @return <code>true</code> if the dialog is visible (actually check if the "create" button is visible). */
    public boolean isDisplayed()
    {
        return (createButton != null && createButton.isDisplayed());
    }

    /** @return <code>true</code> if the classify button is visible and enabled. */
    public boolean isClassifyButtonEnabled()
    {
        if (createButton == null || !createButton.isDisplayed())
        {
            return false;
        }
        String disabledAttribute = createButton.getAttribute("aria-disabled");
        return (disabledAttribute == null || disabledAttribute.toLowerCase().equals("false"));
    }

    /** @return The classification agency entered. */
    public String getAgency()
    {
        return agencyTextInput.getText();
    }

    /** @return The list of selected classification reasons. */
    public List<String> getReasons()
    {
        List<WebElement> reasons = reasonsContainer.findElements(By
                    .cssSelector(".alfresco-forms-controls-MultiSelect__choice__content"));

        return reasons.stream()
                      .map(reason -> reason.getAttribute("data-aikau-value"))
                      .collect(toList());
    }
}
