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
package org.alfresco.po.rm.details;

import org.alfresco.po.rm.details.event.CompleteEventDialog;
import org.alfresco.po.rm.details.event.EventBlock;
import org.alfresco.po.rm.site.RMSiteNavigation;
import org.alfresco.po.share.details.DetailsPage;
import org.openqa.selenium.support.FindBy;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * Disposable Item Details page
 *
 * @author Tatiana Kalinovskaya
 */
@Component
public class DisposableItemDetailsPage extends DetailsPage<RMSiteNavigation>
{
    /** list of events */
    @FindBy(css = ".event")
    private List<EventBlock> events;

    /**complete event dialog */
    @Autowired
    private CompleteEventDialog completeEventDialog;

    /**
     * get event form Event Block
     * @param name - event name
     * @return Event Block, null if there is no such event
     */
    public EventBlock getEventByName(String name)
    {
        for (EventBlock event : events)
        {
            if (event.getEventName().equals(name))
                return event;
        }
        return null;
    }

    /**
     * get the quantity of events displayed on folder/record details page
     */
    public int getEventsQuantity()
    {
        return events.size();
    }

    /**
     * click on Complete Event
     * @param name - event name
     * @return Complete Event Dialog
     */
    public CompleteEventDialog clickOnCompleteEvent(String name)
    {
        this.getEventByName(name).clickOnCompleteEvent();
        return completeEventDialog.render();
    }

    /**
     * click on Undo Event
     * @param name - event name
     * @return details page
     */
    public <T extends DisposableItemDetailsPage> T clickOnUndoEvent(String name)
    {
        this.getEventByName(name).clickOnUndoEvent();
        return super.render();
    }
}
