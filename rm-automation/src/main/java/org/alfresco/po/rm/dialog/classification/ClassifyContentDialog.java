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

import org.alfresco.po.common.util.Utils;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.springframework.stereotype.Component;

/**
 * The classify content dialog.
 *
 * @author tpage
 * @since 2.4.a
 */
@Component
public class ClassifyContentDialog extends ClassificationDialog
{
    @FindBy(css="#LEVELS_CONTROL")
    private WebElement levelSelectButton;

    @FindBy(css="#LEVELS_CONTROL .dijitSelectLabel")
    private WebElement selectedLevel;

    @FindBy(css="#LEVELS_CONTROL_menu")
    private WebElement levelsMenu;

    /**
     * @see org.alfresco.po.rm.dialog.classification.ClassificationDialog#getLevelSelectButton()
     */
    @Override
    protected WebElement getLevelSelectButton()
    {
        return levelSelectButton;
    }

    /**
     * @see org.alfresco.po.rm.dialog.classification.ClassificationDialog#getSelectedLevel()
     */
    @Override
    protected WebElement getSelectedLevel()
    {
        return selectedLevel;
    }

    /**
     * @see org.alfresco.po.rm.dialog.classification.ClassificationDialog#getLevelsMenu()
     */
    @Override
    protected WebElement getLevelsMenu()
    {
        return levelsMenu;
    }

    /**
     * @see org.alfresco.po.rm.dialog.classification.ClassificationDialog#setLevel(java.lang.String)
     */
    @Override
    public ClassifyContentDialog setLevel(String levelId)
    {
        // Open the dropdown menu.
        getLevelSelectButton().click();

        // Choose the appropriate option by the label.
        final String selector = "tr[aria-label='" + levelId + " '] td[class$='dijitMenuItemLabel']";

        // get the classification level
        WebElement level = Utils.waitForFind(getLevelsMenu(), By.cssSelector(selector));

        // select the right level
        level.click();
        return this;
    }
}
