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
package org.alfresco.po.rm.disposition.edit.steps;

import java.util.ArrayList;
import java.util.List;

import org.alfresco.po.common.util.Retry;
import org.alfresco.po.common.util.Utils;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.ui.ExpectedConditions;

import ru.yandex.qatools.htmlelements.element.Button;
import ru.yandex.qatools.htmlelements.element.HtmlElement;
import ru.yandex.qatools.htmlelements.element.Link;
import ru.yandex.qatools.htmlelements.element.Select;

/**
 * Event section of disposition step block
 *
 * @author Tatiana Kalinovskaya
 */
public class EventsSection extends HtmlElement
{
    /** add event button */
    @FindBy(xpath = ".//button")
    private Button addEventButton;

    /** list of events to add*/
    @FindBy (xpath = ".//div[@class='events-header']//div[ul]")
    private WebElement events;

    /** eligibleOnFirstCompleteEvent selectbox */
    @FindBy (name="eligibleOnFirstCompleteEvent")
    private Select eligibleOnFirstCompleteEvent;

    /** delete event icon */
    @FindBy (xpath = ".//span[@class='delete']")
    private List <Link> deleteEventIcons;

    /** list of added events */
    @FindBy (css = ".events-list .action-event-name")
    private List <WebElement> addedEventsNames;

    /** list of avilable events */
    @FindBy (css = "div.events-header ul")
    private WebElement availableEvents;

    /**
     * Click on Add Event
     */
    public EventsSection clickOnAddEvent()
    {
        // click on the add event button
        addEventButton.click();

        // wait for the available events list to be visible
        Utils.waitForVisibilityOf(availableEvents);

        return this;
    }

    /**
     * Select Event
     */
    public EventsSection selectEvent(final String eventName)
    {
        // click on add event button
        clickOnAddEvent();

        Utils.retry(new Retry<Void>()
        {
            public Void execute()
            {
                // find the correct event link
                boolean eventClicked = false;
                StringBuffer eventsChecked = new StringBuffer(255);
                List<WebElement> eventLinks = availableEvents.findElements(By.cssSelector("a.yuimenuitemlabel"));
                for (WebElement eventLink : eventLinks)
                {
                    // get the text in the event link
                    String eventLinkText = eventLink.getText().trim();
                    eventsChecked.append(eventLinkText).append(", ");

                    if (eventName.equals(eventLinkText))
                    {
                        // wait for the event link to be clickable
                        Utils.webDriverWait().until(ExpectedConditions.elementToBeClickable(eventLink));

                        // click event link
                        eventLink.click();
                        eventClicked = true;
                        break;
                    }
                }

                // if no event clicked throw exception
                if (eventClicked == false)
                {
                    throw new RuntimeException("The event " + eventName + " could not be added from a list of " + eventLinks.size() +
                                               " events [" + eventsChecked.toString() + "]");
                }
                
                return null;
            }
         }, 5);

        return this;
    }

    /** isEligibleOnFirstCompleteEvent
     *
     * @return true if 'Which ever event is earlier'
     *         false if 'When all events have occurred'
     */
    public boolean isEligibleOnFirstCompleteEvent ()
    {
        return eligibleOnFirstCompleteEvent.getFirstSelectedOption().equals("true") ? true : false;
    }
    /**
     * set eligible On First Complete Event
     * @param check - set "When all events have occurred" if false
     *              - set "Whichever event is earlier" if true
     */
    public EventsSection setEligibleOnFirstCompleteEvent(boolean check)
    {
        eligibleOnFirstCompleteEvent.selectByValue(String.valueOf(check));
        return this;
    }

    /**
     * delete event
     * @param eventNumber
     * @return event section
     * note that the returned event section is not refreshed
     */
    public EventsSection deleteEvent(int eventNumber)
    {
        if (deleteEventIcons.size()<eventNumber)
            throw new RuntimeException("The event could not be removed");
        deleteEventIcons.get(eventNumber-1).click();
        return this;

    }

    public List <String> getAddedEventsNames()
    {
        List <String> result = new ArrayList<>();
        for (WebElement event: addedEventsNames)
            result.add(event.getText());
        return result;
    }

}
