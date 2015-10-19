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
package org.alfresco.po.share.page;

import java.util.List;

import org.alfresco.po.common.annotations.RenderableChild;
import org.alfresco.po.common.renderable.Renderable;
import org.alfresco.po.common.util.Utils;
import org.alfresco.po.share.admin.AdminToolsNavigation;
import org.alfresco.po.share.login.LoginPage;
import org.alfresco.po.share.userdashboard.UserDashboardPage;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import ru.yandex.qatools.htmlelements.element.Link;

/**
 * Share page implementation
 *
 * @author Tuna Aksoy
 * @since 2.2
 * @version 1.0
 */
@Component
public class SharePageNavigation extends Renderable
{
    @FindBy(css="a[title='Home']")
    private Link homeLink;

    @FindBy(css="a[title='Admin Tools']")
    private Link adminToolsLink;

    @FindBy(css=".tool-link")
    private List<WebElement> toolsLinks;

    private @Autowired UserDashboardPage userDashboardPage;
    private @Autowired AdminToolsNavigation adminToolsNavigation;
    private @Autowired @RenderableChild UserDropdown userDropdown;

    public UserDashboardPage clickOnHome()
    {
        homeLink.click();
        return userDashboardPage.render();
    }

    /**
     * Click on the admin tools link.
     *
     * @return The admin tools navigation menu.
     */
    public AdminToolsNavigation clickOnAdminTools()
    {
        adminToolsLink.click();
        Utils.waitFor(ExpectedConditions.visibilityOfAllElements(toolsLinks));
        return adminToolsNavigation.render();
    }

    /**
     * logout current user
     */
    public LoginPage logout()
    {
        return userDropdown.logout();
    }
}
