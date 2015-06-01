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
package org.alfresco.po.share.panel;

import static org.alfresco.po.common.util.Utils.waitForStalenessOf;

import java.text.MessageFormat;

import org.alfresco.po.common.renderable.Renderable;
import org.alfresco.po.common.util.Utils;
import org.alfresco.po.share.page.SharePage;
import org.openqa.selenium.By;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import ru.yandex.qatools.htmlelements.element.Link;

/**
 * @author Roy Wetherall, Tatiana Kalinovskaya
 */
public abstract class ActionPanel extends Panel
{
    @FindBy(css="div.action-set")
    private WebElement actionSet;

    /** action selector */
    private static final String ACTION_SELECTOR_CSS = "div .{0} a";

    /** is action panel expanded
     *
     * @return true is panel is expanded
     */
    public boolean isPanelExpanded()
    {
        return actionSet.isDisplayed();
    }

    /**
     * Helper method to check whether the specified actions are clickable
     *
     * @param actionNames   action names
     * @return boolean      true if all clickable, false otherwise
     */
    public boolean isActionsClickable(String ... actionNames)
    {
        boolean result = true;

        // check each action
        for (String actionName : actionNames)
        {
            if (!getActionLink(actionName).isEnabled())
            {
                result = false;
                break;
            }
        }

        return result;
    }

    /**
     * Is action available
     */
    public boolean isActionAvailable(String actionName)
    {
        return (getActionLink(actionName)!=null);
    }

    /**
     * Is action clickable
     */
    public boolean isActionClickable(String actionName)
    {
        return getActionLink(actionName).isEnabled();
    }

    /**
     * Click on action
     */
    public Renderable clickOnAction(String actionName)
    {
        return clickOnAction(actionName, SharePage.getLastRenderedPage(),true);
    }

    /**
     * Click on action and don't try to return a rendered object.
     */
    public void clickOnActionAndDontRender(String actionName)
    {
        clickOnAction(actionName, null, true);
    }

    /**
     * Click on action
     */
    public <T extends Renderable> T clickOnAction(String actionName, T renderable)
    {
        return clickOnAction(actionName, renderable, false);
    }

    /**
     * Click on action
     */
    private <T extends Renderable> T clickOnAction(String actionName, T renderable, boolean waitForActionStaleness)
    {
        // mouse over and click on the action
        Link action = getActionLink(actionName);
        Utils.mouseOver(action);
        action.click();

        // wait for the action link to become stale
        if (waitForActionStaleness == true)
        {
            waitForStalenessOf(action);
        }

        if (renderable == null)
        {
            return null;
        }
        // render the return page
        return renderable.render();
    }

    /**
     * Helper method to get the action link
     */
    private Link getActionLink(String actionName)
    {
        WebElement link;
        try
        {
            link = actionSet.findElement(getActionSelector(actionName));
        }
        catch (NoSuchElementException e)
        {
            return null;
        }

        return new Link(link);
    }

    /**
     * Helper method to get the action selector
     */
    private By getActionSelector(String actionName)
    {
        String actionCSS = MessageFormat.format(ACTION_SELECTOR_CSS, actionName);
        return By.cssSelector(actionCSS);
    }

}
