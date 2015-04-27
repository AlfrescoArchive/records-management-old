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
package org.alfresco.po.rm.dialog.holds;

import java.util.List;

import org.alfresco.po.common.Dialog;
import org.alfresco.po.common.buttonset.OkCancelButtonSet;
import org.alfresco.po.common.buttonset.StandardButtons;
import org.alfresco.po.common.renderable.Renderable;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import ru.yandex.qatools.htmlelements.element.CheckBox;

/**
 * Add to / remove from hold base dialog
 *
 * @author Roy Wetherall
 */
public abstract class AddToRemoveFromHold extends Dialog
                                          implements StandardButtons
{
    /** data element */
    @FindBy(css="div[id$='listofholds'] tbody[class$='data']")
    private WebElement data;

    /** names selector */
    private By namesSelector = By.cssSelector("tr td.yui-dt-col-name div");

    /** checkbox selector */
    private By checkboxesSelector = By.cssSelector("tr td.yui-dt-col-check input");

    /** button set */
    @FindBy(css=VISIBLE_DIALOG)
    private OkCancelButtonSet buttonset;

    /**
     * @return  true if the hold is present in the list, false otherwise
     */
    public boolean isHoldVisible(String name)
    {
        return (findHoldCheckBox(name) != null);
    }

    public AddToRemoveFromHold selectHold(String name)
    {
        return selectHold(name, true);
    }

    /**
     * Select/deselect hold in the list
     *
     * @param name - name of hold to select/deselect
     * @param select - select if true, deselect if false
     */
    public AddToRemoveFromHold selectHold(String name, Boolean select)
    {
        CheckBox checkbox = findHoldCheckBox(name);
        if (checkbox != null)
        {
            checkbox.set(select);
            return this;
        }
        else
        {
            throw new RuntimeException("Hold " + name + " not found.");
        }
    }

    /**
     * Helper to find the checkbox for a given hold
     */
    private CheckBox findHoldCheckBox(String name)
    {
        try{Thread.sleep(1000);}catch(Exception e){}

        List<WebElement> nameElements = data.findElements(namesSelector);
        List<WebElement> checkboxes = data.findElements(checkboxesSelector);

        CheckBox checkbox = null;
        int index = 0;
        for (WebElement nameElement : nameElements)
        {
            if (nameElement.getText().equals(name))
            {
                checkbox = new CheckBox(checkboxes.get(index));
                break;
            }

            index++;
        }
        return checkbox;
    }

    /**
     * Click on ok
     */
    public Renderable clickOnOk()
    {
        return buttonset.click(OK);
    }

    /**
     * Click on cancel
     */
    public Renderable clickOnCancel()
    {
        return buttonset.click(CANCEL);
    }

}
