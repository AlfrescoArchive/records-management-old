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
package org.alfresco.po.share.admin;

import org.alfresco.po.common.renderable.Renderable;
import org.alfresco.po.share.console.users.SecurityClearancePage;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * A generic page object for Admin Tools pages that can be extended.
 *
 * @author tpage
 * @since 3.0
 */
public class AdminToolsNavigation extends Renderable
{
    @FindBy(css = "a[href=security-clearance]")
    private WebElement securityClearanceLink;

    @Autowired
    private SecurityClearancePage securityClearancePage;

    /**
     * Click on the security clearance link.
     *
     * @return The security clearance page.
     */
    public SecurityClearancePage clickOnSecurityClearanceLink()
    {
        securityClearanceLink.click();
        return securityClearancePage.render();
    }
}
