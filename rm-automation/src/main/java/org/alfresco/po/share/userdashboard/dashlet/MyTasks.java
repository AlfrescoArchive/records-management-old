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
package org.alfresco.po.share.userdashboard.dashlet;

import java.text.MessageFormat;

import org.alfresco.po.common.Dashlet;
import org.alfresco.po.common.util.Utils;
import org.alfresco.po.rm.site.tasks.EditTaskPage;
import org.alfresco.po.rm.site.tasks.SiteInvitationTaskPanel;
import org.alfresco.po.share.site.CollaborationSiteDashboard;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * The "My Tasks" dashlet on the User Dashboard.
 *
 * @author tpage
 * @since 3.0
 */
@Component
public class MyTasks extends Dashlet
{
    /** XPath to find (first) invitation to site */
    private static final String INVITE = "//a[contains(@href, \"task-edit\")][contains(., \"{0}\")]";
    
    /** edit task page */
    @Autowired
    EditTaskPage editTaskPage;
    
    /** collaboration site dashboard */
    @Autowired
    CollaborationSiteDashboard siteDashboard;
    
    /** my tasks container */
    @FindBy(css = ".my-tasks")
    WebElement myTasksContainer;

    /**
     * Accept an invitation to join a site.
     *
     * @param siteName The name of the site to join.
     * @return The site dashboard of the site that was joined.
     */
    public CollaborationSiteDashboard acceptInvitation(String siteName)
    {
        // get the invitation selector
        By invitationSelector = getInvitationSelector(siteName);
        
        // wait for invitation to becomes available
        Utils.waitForVisibilityOf(invitationSelector);
        
        // get the invitation link and click
        WebElement invitationLink = myTasksContainer.findElement(invitationSelector);
        invitationLink.click();
        
        // accept the invitation
        editTaskPage.render();
        SiteInvitationTaskPanel siteInvitePanel = (SiteInvitationTaskPanel) editTaskPage.getTaskPanel();
        siteInvitePanel.acceptInvitation();
        
        // return the site dashboard
        siteDashboard.render();
        return siteDashboard;
    }
    
    /**
     * Get the site invitation selector
     */
    private By getInvitationSelector(String siteName)
    {
        String invitationXPath = MessageFormat.format(INVITE, siteName);
        return By.xpath(invitationXPath);
    }
}
