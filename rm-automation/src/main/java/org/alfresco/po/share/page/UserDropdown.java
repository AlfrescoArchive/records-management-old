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
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.springframework.stereotype.Component;

/**
 * The user dropdown menu in the Share page header.
 *
 * @author tpage
 * @since 3.0
 */
@Component
public class UserDropdown extends Renderable
{
    @FindBy(css = "#HEADER_USER_MENU_POPUP")
    private WebElement dropdownButton;

    @FindBy(css = "#HEADER_USER_MENU_LOGOUT_text")
    private WebElement logoutButton;

    public UserDropdown revealDropdown()
    {
        dropdownButton.click();
        return this;
    }

    public void logout()
    {
        logoutButton.click();
    }
}
