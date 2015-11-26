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
import org.alfresco.po.common.buttonset.OkCancelButtonSet;
import org.alfresco.po.common.buttonset.StandardButtons;
import org.alfresco.po.common.renderable.Renderable;
import org.alfresco.po.common.util.Utils;
import org.openqa.selenium.By;
import org.openqa.selenium.StaleElementReferenceException;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

import static org.alfresco.po.common.util.Utils.waitForVisibilityOf;

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
    private WebElement tree;

    /** selectors */
    private static By selectedSelector = By.cssSelector(VISIBLE_DIALOG + " div.treeview div.selected");
    private static By selectedItemSelector = By.xpath("//div[@class='treeview']//div[@class='ygtvitem selected']");
    private static By spanSelector = By.cssSelector("div.ygtvitem span");

    /**
     * Select an item in the tree
     */
    public CopyMoveLinkFileDialog select(String name)
    {
        // wait for the dialog to be visible
        waitForVisibilityOf(selectedSelector);
        WebElement destination = waitForVisibilityOf(By.xpath("//div[@class='treeview']//td//span[contains(text(), '" + name + "')]"));
        destination.click();
        waitForVisibilityOf(selectedItemSelector);
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
