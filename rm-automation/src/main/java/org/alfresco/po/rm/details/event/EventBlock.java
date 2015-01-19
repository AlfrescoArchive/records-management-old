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
package org.alfresco.po.rm.details.event;

import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

import ru.yandex.qatools.htmlelements.element.Button;
import ru.yandex.qatools.htmlelements.element.HtmlElement;

/**
 * Events section of the disposable item details page
 *
 * @author Tatiana Kalinovskaya
 */
public class EventBlock extends HtmlElement
{
    /** event name */
    @FindBy(xpath = ".//div[@class='field name']/span")
    private WebElement eventName;

    /** complete/undo event button */
    @FindBy(xpath = ".//button")
    private Button completeEventButton;

    /** get the name of event */
    public String getEventName()
    {
        return eventName.getText();
    }

    /** is Complete button available for te event
     *
     * @return true if "Complete" button is available
     *         false if "Undo" button is available
     */
    public boolean isCompleteAvailable()
    {
        return completeEventButton.getWrappedElement().getText().equals("Complete Event");
    }


    public void clickOnCompleteEvent()
    {
        if (isCompleteAvailable())
            completeEventButton.click();
        else throw new RuntimeException("Complete event button doesn't exist");
    }

    public void clickOnUndoEvent()
    {
        if (!isCompleteAvailable())
            completeEventButton.click();
        else throw new RuntimeException("Undo event button doesn't exist");
    }
}
