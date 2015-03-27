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

import static org.alfresco.po.common.util.Utils.clearAndType;

import org.alfresco.po.share.console.ConsolePage;
import org.alfresco.po.share.page.Message;
import org.openqa.selenium.support.FindBy;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import ru.yandex.qatools.htmlelements.element.Button;
import ru.yandex.qatools.htmlelements.element.TextInput;

/**
 * New users page
 * 
 * @author Roy Wetherall
 */
@Component
public class NewUsersPage extends ConsolePage
{
    /** page url */
    private static final String URL = "/page/console/admin-console/users#state=panel%3Dcreate";
    
    /** users page */
    @Autowired
    private UsersPage usersPage;
    
    /** message */
    @Autowired
    private Message message;
    
    /** first name input */
    @FindBy(css="input[id$='firstname']")
    private TextInput firstName;
    
    /** last name input */
    @FindBy(css="input[id$='lastname']")
    private TextInput lastName;

    /** email input */
    @FindBy(css="input[id$='email']")
    private TextInput email;

    /** user name input */
    @FindBy(css="input[id$='username']")
    private TextInput userName;

    /** password input */
    @FindBy(css="input[id$='create-password']")
    private TextInput password;

    /** verify password input */
    @FindBy(css="input[id$='verifypassword']")
    private TextInput verifyPassword;
    
    /** create user button */
    @FindBy(css="button[id$='createuser-ok-button-button']")
    private Button createUserButton;

    /** create user and then another button */
    @FindBy(css="button[id$='createuser-another-button-button']")
    private Button createAnotherUserButton;

    /** cancel button */
    @FindBy(css="button[id$='createuser-cancel-button-button']")
    private Button cancelButton;
    
    /**
     * @see org.alfresco.po.share.console.ConsolePage#getPageURL(java.lang.String[])
     */
    @Override
    public String getPageURL(String... context)
    {
        return URL;
    }
    
    /**
     * set first name
     */
    public NewUsersPage setFirstName(String value)
    {
        clearAndType(firstName, value);
        return this;
    }
    
    /**
     * get first name
     */
    public String getFirstName()
    {
        return firstName.getText();
    }
    
    /**
     * set last name
     */
    public NewUsersPage setLastName(String value)
    {
        clearAndType(lastName, value);
        return this;
    }
    
    /**
     * get last name
     */
    public String getLastName()
    {
        return lastName.getText();
    }
    
    /**
     * set email
     */
    public NewUsersPage setEmail(String value)
    {
        clearAndType(email, value);
        return this;
    }
        
    /**
     * get email name
     */
    public String getEmail()
    {
        return email.getText();
    }
    
    /**
     * set user name
     */
    public NewUsersPage setUserName(String value)
    {
        clearAndType(userName, value);
        return this;
    }
    
    /**
     * get user name
     */
    public String getUserName()
    {
        return userName.getText();
    }
    
    /**
     * set password
     */
    public NewUsersPage setPassword(String value)
    {
        clearAndType(password, value);
        return this;
    }
    
    /**
     * get password
     */
    public String getPassword()
    {
        return password.getText();
    }
    
    /**
     * set password verification
     */
    public NewUsersPage setVerifyPassword(String value)
    {
        clearAndType(verifyPassword, value);
        return this;
    }
    
    /**
     * get verify password
     */
    public String getVerifyPassword()
    {
        return verifyPassword.getText();
    }
    
    /**
     * @return  true if the create user button is enabled, false otherwise
     */
    public boolean isEnabledCreateUser()
    {
        return createUserButton.isEnabled();
    }
    
    /**
     * Click on create users
     */
    public UsersPage clickOnCreateUser()
    {
        // click on the create user button
        createUserButton.click();
        
        // wait until the message is visible
        message.waitUntillVisible();
        
        // return the rendered users page
        return usersPage.render();
    }
    
    /**
     * @return  true if the create antoher user button is enabled, false otherwise
     */
    public boolean isEnabledCreateAndCreateAnother()
    {
        return createAnotherUserButton.isEnabled();
    }
    
    /**
     * Click on create and create another
     */
    public NewUsersPage clickOnCreateAndCreateAnother()
    {
        createAnotherUserButton.click();
        return render();
    }
    
    /**
     * @return  true if the cancel user button is enabled, false otherwise
     */
    public boolean isEnabledCancel()
    {
        return cancelButton.isEnabled();
    }
    
    /**
     * Click on cancel 
     */
    public UsersPage clickOnCancel()
    {
        cancelButton.click();
        return usersPage.render();
    }
}
