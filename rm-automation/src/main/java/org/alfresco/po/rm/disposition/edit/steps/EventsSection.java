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

import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.List;

import org.alfresco.po.share.page.SharePage;
import org.openqa.selenium.By;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

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

    private static final String DISPOSITION_EVENT_SELECTOR_XPATH = ".//a[text()=''{0}'']";

    /**
     * Click on Add Event
     */
    public EventsSection clickOnAddEvent()
    {
        addEventButton.click();
        return this;
    }

    /**
     * Select Event
     */
    public EventsSection selectEvent(String eventName)
    {
        // click on add event button
        clickOnAddEvent();

        // get disposition action link
        Link event = getDispositionEventLink(eventName);
        if (event == null || !event.isEnabled())
            throw new RuntimeException("The event " + eventName + " could not be added");
        event.click();
        return this;
    }

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
     * Helper method to get the disposition event link
     */
    private Link getDispositionEventLink(String eventName)
    {
        Link result = null;
        try
        {
            WebElement link = events.findElement(getDispositionEventSelector(eventName));
            result = new Link(link);
        }
        catch (NoSuchElementException e)
        {
            // do nothing, just return null
        }
        return result;
    }
    /**
     * Helper method to get the event selector
     */
    private By getDispositionEventSelector(String eventName)
    {
        String eventXPATH = MessageFormat.format(DISPOSITION_EVENT_SELECTOR_XPATH, eventName);
        return By.xpath(eventXPATH);
    }

    /**
     * delete event
     * @param eventNumber
     * @return event section
     * note that the returned event section is not refreshed
     */
    public EventsSection deleteEvent(int eventNumber)
    {
        deleteEventIcons.get(eventNumber).click();
        SharePage.getLastRenderedPage().render();
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
