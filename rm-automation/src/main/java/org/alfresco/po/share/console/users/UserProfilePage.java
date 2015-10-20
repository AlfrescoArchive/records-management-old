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
package org.alfresco.po.share.console.users;

import java.text.MessageFormat;

import org.alfresco.po.common.ConfirmationPrompt;
import org.alfresco.po.common.util.Utils;
import org.alfresco.po.share.console.ConsolePage;
import org.apache.commons.lang.StringUtils;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import ru.yandex.qatools.htmlelements.element.Button;

/**
 * User profile page
 *
 * @author Roy Wetherall
 * @author David Webster
 */
@Component
public class UserProfilePage extends ConsolePage
{
    /** page url */
    private static final String PAGE_URL = "/page/console/admin-console/users#state=panel%3Dview%26userid%3D{0}";
    private static final By USERNAME_SELECTOR = By.cssSelector("[id$='default-view-username']");
    private static final By BACK_BUTTON_SELECTOR = By.cssSelector("button[id$='default-goback-button-button']");
    private static final By EDIT_BUTTON_SELECTOR = By.cssSelector("button[id$='edituser-button-button']");

    /** confirmation prompt */
    @Autowired
    private ConfirmationPrompt confirmationPrompt;
    
    /** Edit user page */
    @Autowired
    private EditUserPage editUserPage;
    
    /** delete user button */
    @FindBy(css="button[id$='deleteuser-button-button']")
    private Button deleteUserButton;
    
    /**
     * @see org.alfresco.po.share.console.ConsolePage#getPageURL(java.lang.String[])
     */
    @Override
    public String getPageURL(String... context)
    {
        if (context.length == 1)
        {
            // get the user id
            String userId = context[0];
            if (!StringUtils.isNotBlank(userId))
            {
                throw new RuntimeException("User id is empty in site page URL context.");
            }

            return MessageFormat.format(PAGE_URL, userId);
        }
        else
        {
            throw new RuntimeException("User id is expected context.");
        }
    }

    /**
     * @return  true if the delete user button is enabled, false otherwise
     */
    public boolean isEnabledDeleteUser()
    {
        return deleteUserButton.isEnabled();
    }

    /**
     * Click on delete user
     */
    public ConfirmationPrompt clickOnDeleteUser()
    {
        deleteUserButton.click();
        return confirmationPrompt.render();
    }
    
    /**
     * Click on edit user
     */
    public EditUserPage clickOnEditUser()
    {
        WebElement editButton = Utils.waitForVisibilityOf(EDIT_BUTTON_SELECTOR);
        Utils.mouseOver(editButton);
        editButton.click();
        return editUserPage.render();
    }

    /**
     * Click on the back button
     */
    public void clickOnBack()
    {
        WebElement backButton = Utils.waitForVisibilityOf(BACK_BUTTON_SELECTOR);
        Utils.mouseOver(backButton);
        backButton.click();
    }

    /**
     * Get the username
     * @return String
     */
    public String getUserName()
    {
        return Utils.getWebDriver().findElement(USERNAME_SELECTOR).getText();
    }
}
