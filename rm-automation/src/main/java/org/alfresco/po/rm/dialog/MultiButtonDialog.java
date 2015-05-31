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
package org.alfresco.po.rm.dialog;

import static org.alfresco.po.common.util.Utils.waitForInvisibilityOf;

import org.alfresco.po.common.renderable.Renderable;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * General Alfresco confirmation dialog.
 *
 * @author Neil Mc Erlean
 */
@Component
public class MultiButtonDialog extends Renderable
{
    /** This selector selects the buttons in left-to-right order. */
    public static final By BUTTONS_SELECTOR = By.cssSelector(".alfresco-dialog-AlfDialog .alfresco-buttons-AlfButton > span");

    /** Clicks the specified button.
     * @param index 0-based, left-to-right.
     */
    public void clickButton(final int index)
    {
        final List<WebElement> buttonElements = webDriver.findElements(BUTTONS_SELECTOR);

        if (index < 0 || index > buttonElements.size() - 1)
        {
            throw new IllegalArgumentException(String.format("Index %d out of range: [%d .. %d]", index, 0, buttonElements.size() - 1));
        }

        final WebElement buttonToClick = buttonElements.get(index);
        buttonToClick.click();

        waitForInvisibilityOf(buttonToClick);
    }
}
