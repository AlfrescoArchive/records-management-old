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
package org.alfresco.po.share.login;

import static org.alfresco.po.common.util.Utils.clearAndType;
import static org.alfresco.po.common.util.Utils.checkMandotaryParam;

import org.alfresco.po.common.renderable.Renderable;
import org.alfresco.po.share.userdashboard.UserDashboardPage;
import org.openqa.selenium.support.FindBy;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;

import ru.yandex.qatools.htmlelements.element.Button;
import ru.yandex.qatools.htmlelements.element.TextBlock;
import ru.yandex.qatools.htmlelements.element.TextInput;

/**
 * Login page object representing the login page.
 * It holds the html elements that can be found on the page.
 *
 * @author Tuna Aksoy
 * @since 2.2
 * @version 1.0
 */
@Component
@Lazy
public class LoginPage extends Renderable
{
    /** User name field element  */
    @FindBy(css = "input[id$='username']")
    private TextInput usernameField;

    /** Password field element */
    @FindBy(css = "input[id$='password']")
    private TextInput passwordField;

    /** Login button element */
    @FindBy(css = "button[id$='submit-button']")
    private Button loginButton;

    /** Error message text element */
    @FindBy(css = "div.error")
    private TextBlock errorMessage;

    /** User dashboard page */
    @Autowired
    private UserDashboardPage userDashboardPage;
    
    /**
     * set user name
     */
    public LoginPage setUsername(String username)
    {
    	checkMandotaryParam("username", username);
    	clearAndType(usernameField, username);
        return this;
    }

    /**
     * set password
     */
    public LoginPage setPassword(String password)
    {
    	checkMandotaryParam("password", password);
    	clearAndType(passwordField, password);
        return this;
    }

    /**
     * click login button
     */
    public UserDashboardPage clickOnLoginButton()
    {
        // FIXME: Handle the login failure case
        loginButton.click();
        return userDashboardPage.render();
    }

    /**
     * has login failed
     */
    public boolean isLoginFailed()
    {
        return errorMessage.isDisplayed();
    }

    /**
     * get error message
     */
    public String getErrorMessage()
    {
        return errorMessage.getText();
    }
}
