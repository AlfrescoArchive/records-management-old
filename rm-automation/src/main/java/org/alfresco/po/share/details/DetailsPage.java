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
package org.alfresco.po.share.details;

import java.util.List;

import org.alfresco.po.common.renderable.Renderable;
import org.alfresco.po.common.site.SiteNavigation;
import org.alfresco.po.common.site.SitePage;
import org.alfresco.po.common.util.Utils;
import org.alfresco.po.share.browse.BrowsePage;
import org.alfresco.po.share.browse.documentlibrary.ContentBanner;
import org.alfresco.po.share.page.SharePage;
import org.openqa.selenium.By;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

/**
 * Details page
 *
 * @author Roy Wetherall
 */
public class DetailsPage<N extends SiteNavigation> extends SitePage<N>
{
    /** node path element */
    @FindBy(css="div.node-path")
    private WebElement path;

    /** breadcrumb link selector */
    private By breadcrumbSelector = By.cssSelector("div.node-path a");

    /** browse page to return to */
    private Renderable browsePage;

    /**
     * @see org.alfresco.po.share.page.SharePage#render()
     */
    @Override
    public <T extends Renderable> T render()
    {
        Renderable renderable = SharePage.getLastRenderedPage();
        if (renderable instanceof BrowsePage)
        {
            browsePage = renderable;
        }
        return super.render();
    }

    /**
     * Get the URL of the page
     */
    @Override
    public String getPageURL(String ... context)
    {
        throw new UnsupportedOperationException("This page does not have a direct access URL set.");
    }

    /**
     * Click on last item on breadcrumb
     * @return  parent folder browse page in case of record details
     *          current folder/category browse page otherwise
     */
    public Renderable navigateUp()
    {
        return navigateUp(1);
    }

    /**
     * Click on (breadcrumb.size() - index) item on breadcrumb
     * @return - folder/category/file plan browse page
     * @param index - navigate index elements up
     */
    public Renderable navigateUp(int index)
    {
        // get the breadcrumb links
        List<WebElement> breadcrumb = webDriver.findElements(breadcrumbSelector);
        if (breadcrumb.size() - index < 0)
        {
            throw new RuntimeException("There is no such element on breadcrumb.");
        }
        // click on the parent link
        WebElement parentLink = breadcrumb.get(breadcrumb.size() - index);
        parentLink.click();

        // Wait for the parent link to become stale.
        Utils.waitForStalenessOf(parentLink);
        // Wait for the browse page to have loaded (by waiting for the breadcrumbs to appear).
        Utils.waitForVisibilityOf(By.cssSelector("div.crumb a"));

        // render the browse page
        return browsePage.render();
    }

    /**
     * Get the text on a banner.
     *
     * @param The type of banner to look for.
     * @return The text from the first banner of the specified type.
     * @throws NoSuchElementException If no such banner can be found.
     */
    public String getBannerText(ContentBanner banner) throws NoSuchElementException
    {
        return Utils.getWebDriver().findElement(banner.getSelector()).getText();
    }

    /**
     * Check if a given banner is present.
     *
     * @param The type of banner to look for.
     * @return true if the banner exists.
     */
    public boolean hasBanner(ContentBanner banner)
    {
        return Utils.elementExists(banner.getSelector());
    }

    public String getBreadcrumbPath()
    {
        String path = "";
        List<WebElement> breadcrumb = webDriver.findElements(breadcrumbSelector);
        for(WebElement node : breadcrumb)
        {
            path = path + node.getText();
            if(breadcrumb.size() <= breadcrumb.indexOf(node) + 1)
            {
                path = path + "/";
            }

        }
        return path;
    }

    public Renderable clickOnParentInBreadcrumb(String parentName, Renderable pageToRender)
    {
        List<WebElement> breadcrumb = webDriver.findElements(breadcrumbSelector);
        for(WebElement node : breadcrumb)
        {
            if(node.getText().equals(parentName))
            {
                node.click();
                return pageToRender.render();
            }
        }
        return null;
    }

    /**
     * Check if the content of the file is available for being previewed
     * @return true if the content is available and the message that the file has no content is not displayed
     */
    public boolean isContentAvailable()
    {
        try
        {
            Utils.getWebDriver().findElement(By.cssSelector("div[id*='document-details'] .message"));
        }
        catch (NoSuchElementException e)
        {
            return true;
        }
        return false;
    }

}
