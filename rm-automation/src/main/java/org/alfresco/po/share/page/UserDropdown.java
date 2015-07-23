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
package org.alfresco.po.share.page;

import org.alfresco.po.common.renderable.Renderable;
import org.alfresco.po.common.util.Utils;
import org.alfresco.po.share.login.LoginPage;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * The user dropdown menu in the Share page header.
 *
 * @author tpage
 * @since 3.0.a
 */
@Component
public class UserDropdown extends Renderable
{
    /** selectors */
    private static final By HEADER_MENU_SELECTOR = By.cssSelector("#HEADER_USER_MENU_POPUP");
    private static final By LOGOUT_SELECTOR = By.cssSelector("#HEADER_USER_MENU_LOGOUT_text");

    @Autowired
    private LoginPage loginPage;

    /**
     * logout user
     */
    public LoginPage logout()
    {
        WebElement dropdownButton = Utils.waitForFind(HEADER_MENU_SELECTOR);
        Utils.mouseOver(dropdownButton);
        dropdownButton.click();

        Utils.waitFor(ExpectedConditions.elementToBeClickable(LOGOUT_SELECTOR));
        Utils.waitForFind(LOGOUT_SELECTOR).click();

        return loginPage.render();
    }
}
