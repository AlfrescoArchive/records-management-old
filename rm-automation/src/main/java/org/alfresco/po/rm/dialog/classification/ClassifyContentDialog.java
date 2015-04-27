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

import org.alfresco.po.common.Dialog;
import org.alfresco.po.common.renderable.Renderable;
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

    /** Here we rely on the text node being "Create". */
    @FindBy(xpath="//div[contains(@class, 'footer')]//span[contains(@class, 'alfresco-buttons-AlfButton')]//span[contains(@class, 'dijitButtonText')][.='Create']")
    private WebElement createButton;

    /** Here we rely on the text node being "Cancel". */
    @FindBy(xpath="//div[contains(@class, 'footer')]//span[contains(@class, 'alfresco-buttons-AlfButton')]//span[contains(@class, 'dijitButtonText')][.='Cancel']")
    private WebElement cancelButton;

    public ClassifyContentDialog setLevel(String levelId)
    {
        // Open the dropdown menu.
        levelSelectButton.click();
        // Choose the appropriate option by the label.
        String selector = "//table[@id='LEVELS_CONTROL_menu']//td[contains(@class, 'dijitMenuItemLabel')][.='" + levelId + "']";
        WebElement level = levelsMenu.findElement(By.xpath(selector));
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

    public ClassifyContentDialog addReason(String id)
    {
        // Assume that the classification reason is on the first page of results for the search "i".
        // This hack currently works as all three pre-configured classification reasons have an "i" in them.
        clearAndType(reasonTextInput, "i");
        String selector = "#REASONS_CONTROL .alfresco-forms-controls-MultiSelect__result[data-aikau-value='" + id + "']";
        Utils.waitForVisibilityOf(By.cssSelector(selector));
        WebElement reason = dialog.findElement(By.cssSelector(selector));
        reason.click();
        return this;
    }

    public ClassifyContentDialog removeSelectedReason(String id) throws NoSuchElementException
    {
        String selector = "[data-aikau-value='"+id+"'] ~ a";
        WebElement removeButton = reasonsContainer.findElement(By.cssSelector(selector));
        removeButton.click();
        return this;
    }

    public Renderable submitDialog()
    {
        createButton.click();
        Utils.waitForInvisibilityOf(createButton);
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
}
