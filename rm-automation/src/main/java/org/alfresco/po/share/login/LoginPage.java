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

import static org.alfresco.po.common.util.Utils.checkMandatoryParam;
import static org.alfresco.po.common.util.Utils.clearAndType;

import java.util.HashMap;
import java.util.Map;

import org.alfresco.po.common.renderable.Renderable;
import org.alfresco.po.common.util.Utils;
import org.alfresco.po.share.userdashboard.UserDashboardPage;
import org.openqa.selenium.By;
import org.openqa.selenium.support.FindBy;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;
import org.testng.Reporter;

import ru.yandex.qatools.htmlelements.element.Button;
import ru.yandex.qatools.htmlelements.element.Form;
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
    /** currently logged in user */
    private volatile Map<String, String> currentUserByWindow = new HashMap<String, String>();
    
    /** login form selector */
    private static final String LOGIN_FORM = "form.login";
    private static final By LOGIN_FORM_SELECTOR = By.cssSelector(LOGIN_FORM);
    
    /** error selector */
    private static final By ERROR_SELECTOR = By.cssSelector("div.error");
    
    /** form */
    @FindBy(css = LOGIN_FORM)
    private Form loginForm;
    
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
     * Clear current user when login page is rendered
     * 
     * @see org.alfresco.po.common.renderable.Renderable#render()
     */
    @Override
    public <T extends Renderable> T render()
    {
        // super render
        T result = super.render();
        
        // clear the user
        currentUserByWindow.remove(Utils.getWebDriver().getWindowHandle());
        
        return result;
    }
    
    /**
     * get user name
     */
    public String getUsername()
    {
        return usernameField.getText();
    }
    
    /**
     * set user name
     */
    public LoginPage setUsername(String username)
    {
    	checkMandatoryParam("username", username);
    	clearAndType(usernameField, username);
        return this;
    }

    /**
     * set password
     */
    public LoginPage setPassword(String password)
    {
    	checkMandatoryParam("password", password);
    	clearAndType(passwordField, password);
        return this;
    }

    /**
     * click login button
     */
    public UserDashboardPage clickOnLoginButton()
    {
        return clickOnLoginButton(userDashboardPage);
    }
    
    /**
     * click login button, rendering as provided
     */
    private <T extends Renderable> T clickOnLoginButton(T renderable)
    {
        // click login button
        String userName = getUsername();
        loginButton.click();
        
        // check for error
        if (Utils.elementExists(ERROR_SELECTOR))
        {
            // TODO typed exception 
            // TODO get displayed message and add to exception details
            throw new RuntimeException("Couldn't login to Share as '" + userName + "'. Please check credentials.");
        }
        
        // else successful login 
        currentUserByWindow.put(Utils.getWebDriver().getWindowHandle(), userName);
        return renderable.render();        
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
    
    /** 
     * Indicates whether the login page is being shown
     */
    public boolean isShown()
    {
        return Utils.elementExists(LOGIN_FORM_SELECTOR);
    }
    
    /**
     * Try to login.
     *
     * @param userName The user to log in as.
     * @param password The password to use.
     * @return {@link UserDashboardPage} user dashboard page
     */
    public UserDashboardPage login(String userName, String password)
    {
        return login(userName, password, userDashboardPage);
    }
    
    /**
     * Try to login.
     *
     * @param userName The user to log in as.
     * @param password The password to use.
     * @param renderable The renderable item which will be the destination of the log-in process
     * @return T rendered destination item
     */
    public <T extends Renderable> T login(String userName, String password, T renderable)
    {
        Reporter.log("Logging in as " + userName + " (" + password + ")");

        // ensure the login page is rendered
        render();
        
        // enter details and login
        setUsername(userName)
            .setPassword(password)
            .clickOnLoginButton(renderable);

        Reporter.log("Logged in as " + userName);
        
        // render destination page
        return renderable.render();
    }
    
    /**
     * Is a user currently logged in
     */
    public boolean isUserLoggedIn()
    {
        return (getCurrentUser() != null);
    }
    
    /**
     * Get the currently logged in user
     */
    public String getCurrentUser()
    {
        return currentUserByWindow.get(Utils.getWebDriver().getWindowHandle());
    }   
}
