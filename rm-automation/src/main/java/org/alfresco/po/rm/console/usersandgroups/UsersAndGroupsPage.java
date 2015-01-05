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
package org.alfresco.po.rm.console.usersandgroups;

import java.util.ArrayList;
import java.util.List;

import org.alfresco.po.common.ConfirmationPrompt;
import org.alfresco.po.share.console.ConsolePage;
import org.openqa.selenium.By;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import ru.yandex.qatools.htmlelements.element.Button;
import ru.yandex.qatools.htmlelements.element.Link;

/**
 * RM users and groups page
 * 
 * @author Roy Wetherall
 */
@Component
public class UsersAndGroupsPage extends ConsolePage
{
    /** standard roles */
    public static final String ROLE_RM_ADMIN = "Administrator";
    public static final String ROLE_RM_MANAGER = "RecordsManager";
    public static final String ROLE_RM_POWER_USER = "PowerUser";
    public static final String ROLE_RM_SECURITY_OFFICER = "SecurityOfficer";
    public static final String ROLE_RM_USER = "User";
    
    /** id prefixes */
    private static final String PREFIX_ROLE = "role-";
    private static final String PREFIX_GROUP = "group-";
    private static final String PREFIX_USER = "user-";
    
    /** value prefixes */
    private static final String VALUE_GROUP = "GROUP_";
    
    /** page url */
    private static final String PAGE_URL = "/page/console/rm-console/rm-users-and-groups";
    
    /** selectors */
    private static final By LINK_SELECTOR = By.cssSelector("a");
    private static final By SELECTED_LINK_SELECTOR = By.cssSelector("li.selected a");
    
    /** roles element */
    @FindBy(id="roles")
    private WebElement roles;
    
    /** selected role groups */
    @FindBy(id="roleGroups")
    private WebElement roleGroups;
    
    /** selected role users */
    @FindBy(id="roleUsers")
    private WebElement roleUsers;
    
    /** add group button */
    @FindBy(id="addGroup-button")
    private Button addGroupButton;
    
    /** remove group button */
    @FindBy(id="removeGroup-button")
    private Button removeGroupButton;
    
    /** add user button */
    @FindBy(id="addUser-button")
    private Button addUserButton;
    
    /** remove user button */
    @FindBy(id="removeUser-button")
    private Button removeUserButton;
    
    /** confirmation prompt */
    @Autowired
    private ConfirmationPrompt confirmationPrompt;
    
    /** add group dialog */
    @Autowired
    private AddGroupDialog addGroupDialog;
    
    /** add user dialog */
    @Autowired
    private AddUserDialog addUserDialog;
    
    /**
     * @see org.alfresco.po.share.console.ConsolePage#getPageURL(java.lang.String[])
     */
    @Override
    public String getPageURL(String... context)
    {
        return PAGE_URL;
    }
    
    /**
     * Get the available roles
     */
    public List<String> getRoles()
    {
        return getValues(roles, PREFIX_ROLE);
    }
    
    /**
     * Select a role
     */
    public UsersAndGroupsPage selectRole(String role)
    {
        return selectValue(roles, PREFIX_ROLE, role);
    }
    
    /**
     * Get the currently selected role
     */
    public String getSelectedRole()
    {
        return getSelectedValue(roles, PREFIX_ROLE);     
    }
    
    /**
     * Get the selected roles groups
     */
    public List<String> getRoleGroups()
    {
        return getValues(roleGroups, PREFIX_GROUP);
    }
    
    /**
     * Select a role group
     */
    public UsersAndGroupsPage selectRoleGroup(String value)
    {
        return selectValue(roleGroups, PREFIX_GROUP, VALUE_GROUP + value);
        
    }
    
    /**
     * Get the current selected role group
     */
    public String getSelectedRoleGroup()
    {
        return getSelectedValue(roleGroups, PREFIX_GROUP);
    }
    
    /**
     * Get the selected roles users
     */
    public List<String> getRoleUsers()
    {
        return getValues(roleUsers, PREFIX_USER);
    }
    
    /**
     * Select a role user
     */
    public UsersAndGroupsPage selectRoleUser(String value)
    {
        return selectValue(roleUsers, PREFIX_USER, value);
        
    }
    
    /**
     * Get the current selected role user
     */
    public String getSelectedRoleUser()
    {
        return getSelectedValue(roleUsers, PREFIX_USER);
    }
    
    /**
     * Helper method to get the values of a list
     */
    private List<String> getValues(WebElement rootElement, String idPrefix)
    {
        // get the role links
        List<WebElement> links = rootElement.findElements(LINK_SELECTOR);
        List<String> result = new ArrayList<String>(links.size());
        for (WebElement link : links)
        {
            // parse the id to get the name of the role
            String id = link.getAttribute("id").substring(idPrefix.length());
            
            // remove group prefix, if present
            if (id.startsWith(VALUE_GROUP))
            {
                id = id.substring(6);
            }
            
            // add the result
            result.add(id);
        }
        return result;
    }
    
   /**
    * Helper method to select the value of a list
    */
    private UsersAndGroupsPage selectValue(WebElement rootElement, String idPrefix, String value)
    {
        String id = idPrefix + value;
        Link link = new Link(rootElement.findElement(By.id(id)));
        link.click();
        return this;
    }
    
    /**
     * Helper method to get the currently selected value of a list
     */
    private String getSelectedValue(WebElement rootElement, String idPrefix)
    {
        String result = null;
        try
        {
            // find the element and process 
            WebElement selectedValue = rootElement.findElement(SELECTED_LINK_SELECTOR);
            result = selectedValue.getAttribute("id").substring(idPrefix.length());
            
            // remove group prefix, if present
            if (result.startsWith(VALUE_GROUP))
            {
                result = result.substring(6);
            }
            
        }
        catch (NoSuchElementException exception)
        {
            // handle and return null, to indicate no selected value
        }
        return result;
    }
    
    /**
     * Is add group button enabled
     */
    public boolean isEnabledAddGroup()
    {
        return addGroupButton.isEnabled();
    }
    
    /**
     * Click on add group button
     */
    public AddGroupDialog clickOnAddGroup()
    {
        addGroupButton.click();
        return addGroupDialog.render();
    }
    
    /**
     * Is remove group button enabled
     */
    public boolean isEnabledRemoveGroup()
    {
        return removeGroupButton.isEnabled();
    }
    
    /**
     * Click on remove group button
     */
    public ConfirmationPrompt clickOnRemoveGroup()
    {
        removeGroupButton.click();
        return confirmationPrompt.render();
    }
    
    /**
     * Is add user button enabled
     */
    public boolean isEnabledAddUser()
    {
        return addUserButton.isEnabled();
    }
    
    /**
     * Click on add user button
     */
    public AddUserDialog clickOnAddUser()
    {
        addUserButton.click();
        return addUserDialog.render();
    }
    
    /**
     * Is remove user button enabled
     */
    public boolean isEnabledRemoveUser()
    {
        return removeUserButton.isEnabled();
    }
    
    /**
     * Click on remove user button
     */
    public ConfirmationPrompt clickOnRemoveUser()
    {
        removeUserButton.click();
        return confirmationPrompt.render();
    }
}
