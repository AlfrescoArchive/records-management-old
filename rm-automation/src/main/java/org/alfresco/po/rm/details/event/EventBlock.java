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

    public String getEventName()
    {
        return eventName.getText();
    }

    public void clickOnCompleteEvent()
    {
        if (completeEventButton.getName().equals("Complete Event Button"))
            completeEventButton.click();
        else throw new RuntimeException("Complete event button doesn't exist");
    }

    public void clickOnUndoEvent()
    {
        if (completeEventButton.getName().equals("Undo"))
            completeEventButton.click();
        else throw new RuntimeException("Undo event button doesn't exist");
    }
}
