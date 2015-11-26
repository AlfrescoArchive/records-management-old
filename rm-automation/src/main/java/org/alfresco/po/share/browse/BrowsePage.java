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
package org.alfresco.po.share.browse;

import java.util.List;
import java.util.concurrent.TimeUnit;

import org.alfresco.po.common.Toolbar;
import org.alfresco.po.common.annotations.RenderableChild;
import org.alfresco.po.common.renderable.Renderable;
import org.alfresco.po.common.site.SiteNavigation;
import org.alfresco.po.common.site.SitePage;
import org.alfresco.po.common.util.Utils;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.ui.FluentWait;
import org.springframework.beans.factory.annotation.Autowired;

import com.google.common.base.Predicate;

/**
 * Browse page
 * 
 * @author Roy Wetherall
 */
public abstract class BrowsePage<N extends SiteNavigation,
                                 L extends BrowseList<?>,
                                 S extends Toolbar> extends SitePage<N>
{
    /** list view */
    @RenderableChild
    @Autowired
    private L list;

    /** toolbar */
    @RenderableChild
    @Autowired
    private S toolbar;

    @FindBy(css = "div[id$='default-navBar'] a:not(.filter-path)")
    private List<WebElement> breadCrumbNodes;
    
    /**
     * @return L list view
     */
    public L getList()
    {
        return list;
    }
    
    /**
     * @return S toolbar
     */
    public S getToolbar()
    {
        return toolbar;
    }
    
    /**
     * @see org.alfresco.po.share.page.SharePage#render()
     */
    @Override
    public <T extends Renderable> T render()
    {
        T result = super.render();
        
        // ensure that the "no items" banner has been removed
        Utils.waitForInvisibilityOf(By.cssSelector("div[id$='no-items-template']"));
            
        // wait until the wait message is not longer showing
        Predicate<WebDriver> predicate = w -> 
        {
            return !Utils.elementExists(By.cssSelector(".wait"));            
        };
        new FluentWait<WebDriver>(Utils.getWebDriver())
            .withTimeout(10, TimeUnit.SECONDS)
            .pollingEvery(1, TimeUnit.SECONDS)
            .until(predicate);
        
        return result;
    }
    
    /**
     * Navigate to the the given path in the list view
     * 
     * @param path
     * @return
     */
    @SuppressWarnings("unchecked")
    public <T extends BrowsePage<?,?,?>> T navigateTo(Class<T> clazz, String ... path)
    {
        return (T)navigateTo(this, path);
    }
    
    /**
     * Navigate to the the given path in the list view
     * 
     * @param path
     * @return
     */
    public BrowsePage<?,?,?> navigateTo(String ... path)
    {
        return navigateTo(this, path);
    }
    
    /**
     * Helper to navigate to a given path in the list view from the given browse page
     * 
     * @param browsePage    browse page
     * @param path
     * @return
     */
    private BrowsePage<?,?,?> navigateTo(BrowsePage<?,?,?> browsePage, String ... path)
    {
        for (String name : path)
        {
            // try and find the list item
            ListItem listItem = browsePage.getList().getByPartialName(name);
            if (listItem == null)
            {
                throw new RuntimeException("Unable to navigate to " + name);
            }
            
            // click on the list item
            browsePage = listItem.clickOnLink(this);
        }        
        return browsePage;
    }


    public String getBreadcrumbPath()
    {
        String path = "";
        for (WebElement node : breadCrumbNodes) {
            path = path + node.getText();
            if (breadCrumbNodes.size() <= breadCrumbNodes.indexOf(node) + 1) {
                path = path + "/";
            }
        }
        return path;
    }

    public Renderable clickOnParentInBreadcrumb(String parentName, Renderable pageToRender)
    {
        for(WebElement node : breadCrumbNodes)
        {
            if(node.getText().equals(parentName))
            {
                node.click();
                // Wait for the parent link to become stale.
                Utils.waitForStalenessOf(node);
                // Wait for the browse page to have loaded (by waiting for the breadcrumbs to appear).
                Utils.waitForVisibilityOf(By.cssSelector("div.crumb"));
                return pageToRender.render();
            }
        }
        return null;
    }

}
