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
package org.alfresco.po.share.userdashboard;

import org.alfresco.po.common.annotations.RenderableChild;
import org.alfresco.po.share.page.SharePage;
import org.alfresco.po.share.userdashboard.dashlet.MySitesDashlet;
import org.alfresco.po.share.userdashboard.dashlet.MyTasks;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;

/**
 * User dashboard page object representing the user the dashboard page.
 * It holds the html elements that can be found on the page.
 *
 * @author Tuna Aksoy
 * @since 2.2
 * @version 1.0
 */
@Component
@Lazy
public class UserDashboardPage extends SharePage
{
    /** my sites dashlet */
    @Autowired
    @RenderableChild
    private MySitesDashlet mySitesDashlet;
    @Autowired
    @RenderableChild
    private MyTasks myTasks;

    /**
     * Get the URL of the page
     */
    public String getPageURL(String ... context)
    {
        // By default the user is admin.
        String user = "admin";
        if (context.length > 0)
        {
            user = context[0];
        }
        return "/page/user/" + user + "/dashboard";
    }

    /**
     * Get 'My Site' dashlet
     */
    public MySitesDashlet getMySitesDashlet()
    {
        return mySitesDashlet;
    }

    /** Get the "My Tasks" dashlet. */
    public MyTasks getMyTasks()
    {
        return myTasks;
    }
}
