/*
 * Copyright (C) 2005-2014 Alfresco Software Limited.
 * This file is part of Alfresco
 * Alfresco is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 * Alfresco is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU Lesser General Public License for more details.
 * You should have received a copy of the GNU Lesser General Public License
 * along with Alfresco. If not, see <http://www.gnu.org/licenses/>.
 */
package org.alfresco.po.rm.managepermissions;

import java.util.ArrayList;
import java.util.List;

import org.alfresco.po.common.ConfirmationPrompt;
import org.alfresco.po.common.util.Utils;
import org.alfresco.po.rm.dialog.AuthoritySelectDialog;
import org.alfresco.po.share.form.FormPage;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import ru.yandex.qatools.htmlelements.element.Button;

/**
 * Manage permissions Page.
 * 
 * @author hamara
 * @since  2.3
 */
@Component
public class ManagePermissions extends FormPage
{
    @Autowired
    private ConfirmationPrompt confirmationPrompt;

    @Autowired
    private AuthoritySelectDialog authoritySelectDialog;

    private By userListSection = By.cssSelector(".yui-dt-liner");

    @FindBy(css = "div[id*= 'manage-permissions']")
    private WebElement managePermissionsTitle;

    @FindBy(css = "button[id*='inheritedButton-button']")
    private Button inheritPermissionsButton;

    @FindBy(css = "button[id$='addUserGroupButton-button']")
    private Button addUserGroupButton;

    @FindBy(css = "div[id*='inheritedPermissions']")
    private WebElement inheritedPermissionsPanel;

    @FindBy(css = "div[id*='directPermissions']")
    private WebElement localPermissionsPanel;

    /** user rows selector */
    private By userListSelector = By.cssSelector("div[id$='directPermissions'] tbody[class$='data'] tr");

    /** user delete selector */
    private By useractionSelector = By.cssSelector("div[class='onActionDelete'] a");

    /** user name selector */
    private By userNameSelector = By.cssSelector("td[class$='displayName'] div");

    /** permission selector */
    private By premissionElementSelector = By.cssSelector("td[class$='col-role'] button");

    private By permissionTypeSelector = By.cssSelector("td[class$='col-role'] li");

    /** user permissionsData */
    @FindBy(css = "div[id$='directContainer'] tbody[class$='data']")
    private WebElement userPermissionsData;

    /**
     * click on inerit permissions button
     * 
     * @return ConfirmationPrompt confirmation prompt
     */
    public ConfirmationPrompt clickOnInheritPermissionButton()
    {
        inheritPermissionsButton.click();
        return confirmationPrompt.render();
    }

    /**
     * click on add user and groups button
     * 
     * @return SelectDialog selectdialog
     */
    public AuthoritySelectDialog clickOnSelectUsersAndGroups()
    {
        addUserGroupButton.click();
        return authoritySelectDialog.render();
    }

    /**
     * check is Manage Permissions title is displayed
     * 
     * @return boolean: true if title is displayed, false otherwise
     */
    public boolean isManagePermissionsTitleVisible()
    {
        return managePermissionsTitle.isDisplayed();
    }

    /**
     * check is inheritpermissions button is enabled
     * 
     * @return boolean: true if enabled, false otherwise
     */
    public boolean isInheritPermissionsEnabled()
    {
        return inheritPermissionsButton.isEnabled();
    }

    /**
     * check is add user/group button is enabled
     * 
     * @return boolean: true if enabled, false otherwise
     */
    public boolean isAddUserGroupEnabled()
    {
        return addUserGroupButton.isEnabled();
    }

    /**
     * get users and groups
     *
     * @return list&ltString&gt
     */
    public List<String> getInheritRoles()
    {
        return getValues(inheritedPermissionsPanel);
    }

    /**
     * get users and groups
     * 
     * @return list&ltString&gt
     */
    public List<String> getLocalRoles()
    {
        return getValues(localPermissionsPanel);
    }

    /**
     * Get list of values in Local permissions/Inheirtted permissions
     * 
     * @param webElement
     * @return list&ltString&gt
     */
    private List<String> getValues(WebElement webElement)
    {
        List<WebElement> userlist = webElement.findElements(userListSection);
        List<String> result = new ArrayList<String>(userlist.size());
        for (WebElement useritem : userlist)
        {
            String userRoleName = useritem.getText();
            result.add(userRoleName);
        }
        return result;
    }

    /**
     * Deletes the given authority from the lsit of users and groups
     * 
     * @param name
     */

    public void deleteAuthority(String name)
    {

        List<WebElement> userElements = userPermissionsData.findElements(userListSelector);
        WebElement userName;
        WebElement deleteButton;

        for (WebElement userElement : userElements)
        {
            userName = userElement.findElement(userNameSelector);
            if (userName.getText().equals(name))
            {

                deleteButton = userElement.findElement(useractionSelector);
                Utils.mouseOver(userElement);
                deleteButton.click();

                break;

            }
        }

    }

    /**
     * get the petmissions set for the given user name
     * 
     * @param name
     * @return permission type
     */

    public String getPermission(String name)
    {

        List<WebElement> userElements = userPermissionsData.findElements(userListSelector);
        WebElement userName;
        WebElement permissionElement;
        String PermissionName = null;

        for (WebElement userElement : userElements)
        {
            userName = userElement.findElement(userNameSelector);
            if (userName.getText().equals(name))
            {
                permissionElement = userElement.findElement(premissionElementSelector);
                PermissionName = permissionElement.getText();
                break;
            }
        }
        return PermissionName;
    }

    /**
     * set the permissions for specific user
     * 
     * @param uname
     * @param permissionName
     */

    public void setPermissions(String name, String permissionName)
    {
        List<WebElement> userElements = userPermissionsData.findElements(userListSelector);
        WebElement userName;
        WebElement permissionElement;

        for (WebElement userElement : userElements)
        {
            userName = userElement.findElement(userNameSelector);

            if (userName.getText().equals(name))
            {
                permissionElement = userElement.findElement(premissionElementSelector);
                permissionElement.click();
                List<WebElement> permissionTypes = userElement.findElements(permissionTypeSelector);
                for (WebElement permissionType : permissionTypes)
                {
                    if (permissionType.getText().equals(permissionName))
                    {
                        permissionType.click();
                        break;

                    }
                }

            }

        }

    }
}
