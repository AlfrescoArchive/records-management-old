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
package org.alfresco.po.share.userdashboard.dashlet;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.alfresco.po.common.Dashlet;
import org.alfresco.po.common.ConfirmationPrompt;
import org.alfresco.po.common.renderable.Renderable;
import org.alfresco.po.common.util.Utils;
import org.alfresco.po.rm.site.RMSiteDashboard;
import org.alfresco.po.share.site.CollaborationSiteDashboard;
import org.alfresco.po.share.site.create.CreateSiteDialog;
import org.alfresco.po.share.userdashboard.UserDashboardPage;
import org.openqa.selenium.support.FindBy;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import ru.yandex.qatools.htmlelements.element.Link;

/**
 * My sites dashlet
 * 
 * @author Roy Wetherall
 */
@Component
public class MySitesDashlet extends Dashlet
{
	/** create site link */
	@FindBy(css = "a[id$='createSite-button']")
	private Link createSiteLink;

	/** site links */
	@FindBy(css="a[href$='dashboard'][class='theme-color-1']")
	private List<Link> siteLinks;
	
	/** delete links */
	@FindBy(css="a.delete-site")
	private List<Link> deleteLinks;
	
	/** helper site map */
	private Map<String, Link> siteMap;
	
	/** create site dialog */
	@Autowired
	private CreateSiteDialog createSiteDialog;
	
	/** rm site dashboard */
	@Autowired
	private RMSiteDashboard rmSiteDashboard;
	
	/** collaboration site dashboard */
	@Autowired
	private CollaborationSiteDashboard collabSiteDashboard;
	
	/** user dashboard */
	@Autowired
	private UserDashboardPage userDashboardPage;
	
	/** standard prompt */
	@Autowired
	private ConfirmationPrompt prompt;
	
	/**
	 * @see org.alfresco.po.common.renderable.Renderable#render()
	 */
	@Override
	public <T extends Renderable> T render() 
	{
		// clear site map
		siteMap = null;
		
		// render
		return super.render();
	}
	
	/**
	 * Helper method to map the site links by site id
	 */
	private Map<String, Link> getSiteMap()
	{
		if (siteMap == null)
		{
			siteMap = new HashMap<String, Link>(siteLinks.size());
			
			for (Link link : siteLinks) 
			{
				String[] parts = link.getReference().split("/");
				String id = parts[parts.length-2];
				siteMap.put(id, link);
			}
		}
		return siteMap;
	}
	
	/**
	 * Click on the create site button
	 */
	public CreateSiteDialog clickOnCreateSite()
	{
		createSiteLink.click();
		return createSiteDialog.render();
	}
	
	/**
	 * Is there a site of given id in the list
	 */
	public boolean siteExists(String siteId)
	{
		return getSiteMap().containsKey(siteId);
	}
	
	/**
	 * Get all the site id's listed
	 */
	public Set<String> getSites()
	{
		return getSiteMap().keySet();
	}
	
	/**
	 * Click on a collaboration site link
	 */
	public CollaborationSiteDashboard clickOnCollaborationSite(String siteId)
	{
		if (siteExists(siteId))
		{
			Link link = getSiteMap().get(siteId);
			link.click();
			return collabSiteDashboard.render();
		}
		else
		{
			throw new RuntimeException("Site " + siteId + " is not in the My Sites dashlet list.");
		}
	}
	
	/**
     * Click on a rm site link
     */
    public RMSiteDashboard clickOnRMSite(String siteId)
    {
        if (siteExists(siteId))
        {
            Link link = getSiteMap().get(siteId);
            link.click();
            return rmSiteDashboard.render();
        }
        else
        {
            throw new RuntimeException("Site " + siteId + " is not in the My Sites dashlet list.");
        }
    }
	
	/**
	 * Click on delete site for given site id
	 */
	public UserDashboardPage clickOnDeleteSite(String siteId)
	{
		if (siteExists(siteId))
		{
			// get site entry
			Link siteLink = getSiteMap().get(siteId);
			int index = 0;
			for (Link link : siteLinks) 
			{
				if (link.getReference().equals(siteLink.getReference()))
				{
					break;
				}
				index ++;
			}
			
			// click delete
			Link deleteLink = deleteLinks.get(index);			
			Utils.mouseOver(siteLink);
			deleteLink.click();
			
			// click on delete and yes on confirmation dialogs
			prompt.render();
			prompt
				.clickOnConfirm(prompt)
				.clickOnConfirm();
			
			return userDashboardPage.render();
		}
		else
		{
			throw new RuntimeException("Site " + siteId + " is not in the My Sites dashlet list.");
		}
	}
}
