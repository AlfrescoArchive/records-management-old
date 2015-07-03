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

import static junit.framework.Assert.fail;

import java.util.concurrent.TimeUnit;

import org.alfresco.po.common.renderable.Renderable;
import org.alfresco.po.common.util.Utils;
import org.openqa.selenium.By;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebElement;
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
    private static final By HEADER_MENU_SELECTOR = By.cssSelector("#HEADER_USER_MENU_POPUP");
    private static final By LOGOUT_SELECTOR = By.cssSelector("#HEADER_USER_MENU_LOGOUT_text");

    /**
     * Reveal the user dropdown menu.
     *
     * @return The user dropdown menu.
     * @throws IllegalStateException If the dropdown menu cannot be found.
     */
    public UserDropdown revealDropdown()
    {
        try
        {
            WebElement dropdownButton = Utils.getWebDriver().findElement(HEADER_MENU_SELECTOR);
            dropdownButton.click();
        }
        catch (NoSuchElementException e)
        {
            throw new IllegalStateException("Header menu not found - is a user logged in?", e);
        }
        return this;
    }

    public void logout()
    {
        WebElement logoutButton;
        int attempts = 0;

        while(attempts < 3){
        try {
         logoutButton = Utils.getWebDriver().findElement(LOGOUT_SELECTOR);
         if (logoutButton != null) {
            logoutButton.click();
            return;
        }
        } catch (NoSuchElementException e) {

            Utils.getWebDriver().manage().timeouts().implicitlyWait(3, TimeUnit.SECONDS);
            revealDropdown();
            attempts = attempts + 1;
            if(attempts ==  3){
            fail("The logout did not take place due to the fact that the drop down did not open or wasn't found.");
            }
        }
        }
    }
}
