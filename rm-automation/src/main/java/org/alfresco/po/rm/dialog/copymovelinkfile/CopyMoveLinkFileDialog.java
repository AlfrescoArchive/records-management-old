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
package org.alfresco.po.rm.dialog.copymovelinkfile;

import java.util.List;

import org.alfresco.po.common.Dialog;
import org.alfresco.po.common.annotations.WaitFor;
import org.alfresco.po.common.buttonset.OkCancelButtonSet;
import org.alfresco.po.common.buttonset.StandardButtons;
import org.alfresco.po.common.renderable.Renderable;
import org.alfresco.po.common.util.Utils;
import org.openqa.selenium.By;
import org.openqa.selenium.StaleElementReferenceException;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

/**
 * Base dialog for copy, move, link
 *
 * @author Roy Wetherall
 */
public abstract class CopyMoveLinkFileDialog extends Dialog
                                         implements StandardButtons
{
    /** buttonset */
    @FindBy(css = VISIBLE_DIALOG)
    protected OkCancelButtonSet buttonset;

    /** tree view */
    @FindBy(css = VISIBLE_DIALOG + " div.treeview")
    @WaitFor
    private WebElement tree;

    /** selectors */
    private static By selectedSelector = By.cssSelector(VISIBLE_DIALOG + " div.treeview div.selected");
    private static By spanSelector = By.cssSelector("div.ygtvitem span");

    /**
     * Select an item in the tree
     */
    public CopyMoveLinkFileDialog select(String name)
    {
        // wait for the selected item to be visible
        Utils.waitForVisibilityOf(selectedSelector);

        // re-try in order to deal with unreliable rendering
        for (int retryCount = 0; retryCount < 3; retryCount++)
        {
            try
            {
                // get all the items in the tree
                List<WebElement> spans = tree.findElements(spanSelector);

                for (WebElement span : spans)
                {
                    // find the item that matches the text provided
                    if (span.getText().equals(name))
                    {
                        // select the item in the tree
                        Utils.mouseOver(span);
                        span.click();
                    }
                }

                // break on success
                break;
            }
            catch (StaleElementReferenceException exception)
            {
                if (retryCount == 2)
                {
                    throw exception;
                }
            }
        }

        return this;
    }

    /**
     * Click on cancel button
     */
    public Renderable clickOnCancel()
    {
        return buttonset.click(CANCEL);
    }
}
