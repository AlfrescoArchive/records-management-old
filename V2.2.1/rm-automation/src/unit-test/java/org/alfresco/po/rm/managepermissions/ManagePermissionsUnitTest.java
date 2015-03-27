package org.alfresco.po.rm.managepermissions;

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
import static org.junit.Assert.assertTrue;
// import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import java.util.List;
import org.alfresco.po.common.ConfirmationPrompt;
import org.alfresco.po.rm.browse.fileplan.FilePlan;
import org.alfresco.po.rm.browse.fileplan.RecordCategory;
import org.alfresco.po.rm.browse.fileplan.RecordFolder;
import org.alfresco.po.rm.details.record.RecordDetails;
import org.alfresco.po.rm.dialog.AuthoritySelectDialog;
import org.alfresco.po.rm.dialog.create.NewRecordFolderDialog;
import org.alfresco.test.BaseRmUnitTest;
import org.junit.BeforeClass;
import org.springframework.beans.factory.annotation.Autowired;
import org.testng.annotations.AfterClass;
import org.testng.annotations.Test;

/**
 * Folder Details page Unit Test
 *
 * @author Hema
 */
@Test(groups = { "unit-test" })
public class ManagePermissionsUnitTest extends BaseRmUnitTest
{
    /** Manage Permissions */
    @Autowired
    private ManagePermissions managePermissions;

    /** confirmation Prompt */
    @Autowired
    private ConfirmationPrompt confirmationPrompt;

    /** select Dialog */
    @Autowired
    private AuthoritySelectDialog authoritySelectDialog;

    /** file plan */
    @Autowired
    private FilePlan filePlan;

    /** record details */
    @Autowired
    private RecordDetails recordDetails;

    /** record Category */
    @Autowired
    private RecordCategory recordCategtory;

    /** record folder */
    @Autowired
    private RecordFolder recordFolder;

    private static String actualPermissionsName;
    private static String testUserName = "user1 user1";
    private static String testUser = "user1";
    private static String expectedPermissionName = "Read Only";

    /**
     * Before class
     */
    @BeforeClass
    public void beforeClass()
    {
        // create RM site
        createRMSite();
        createUser(testUser);

    }

    // verify cateogory managepermissions default settings
    @Test
    public void verifyDefaultCategoryManagePermissions()
    {
        // open the RM site and navigate to file planS
        openPage(userDashboardPage).getMySitesDashlet().clickOnRMSite(RM_SITE_ID).getNavigation().clickOnFilePlan();

        // create new category
        if (filePlan.getRecordCategory(RECORD_CATEGORY_NAME) == null)
        {
            filePlan.getToolbar().clickOnNewCategory().setName(RECORD_CATEGORY_NAME).setTitle(TITLE).clickOnSave();
        }

        // click on manage permissions for category
        filePlan.getRecordCategory(RECORD_CATEGORY_NAME).clickonManagePermissions();

        // verify managepermissions titile
        assertTrue(managePermissions.isManagePermissionsTitleVisible());

        // verify inheritpermissions is disabled for category
        assertFalse(managePermissions.isInheritPermissionsEnabled());

        // verify add users/groups is enabled
        assertTrue(managePermissions.isAddUserGroupEnabled());

    }

    // Add a user to category local permissions
    @Test(dependsOnMethods = "verifyDefaultCategoryManagePermissions")
    public void setCategoryLocalPermissions()
    {
        // search forsitecollaborator from the users and groups
        authoritySelectDialog = managePermissions.clickOnSelectUsersAndGroups();

        // add the user listed from the result
        authoritySelectDialog.authoritySearch("Collaborator").clickAddButton();

        // get the roles list from inherit panel
        List<String> rolesList = managePermissions.getLocalRoles();

        // verify whehter the collaborator user is appearing in the inherit list
        assertTrue(rolesList.contains("site_rm_SiteCollaborator"));

        // save changes made.
        managePermissions.clickOnOK();
    }

    // verify folder default manage permission settings.
    @Test(dependsOnMethods = "setCategoryLocalPermissions")
    public void verifyDefaultFolderManagePermissions()
    {
        // get record category
        RecordCategory recordCategory = filePlan.getRecordCategory(RECORD_CATEGORY_NAME);
        recordCategory.clickOnLink();

        // create new folder
        NewRecordFolderDialog dialog = filePlan.getToolbar().clickOnNewRecordFolder();

        // enter details of new record folder and click save
        dialog.setName(RECORD_FOLDER_ONE).setTitle(TITLE).clickOnSave();

        // click on manage permissions
        managePermissions = filePlan.getRecordFolder(RECORD_FOLDER_ONE).clickonManagePermissions();

        // verify the title for managepermissions page
        assertTrue(managePermissions.isManagePermissionsTitleVisible());

        // verify inheritpermissions button is enabled
        assertTrue(managePermissions.isInheritPermissionsEnabled());

        // verify add user/group button is enabled
        assertTrue(managePermissions.isAddUserGroupEnabled());

    }

    // add a user to folder local permissions
    @Test(dependsOnMethods = "verifyDefaultFolderManagePermissions")
    public void setFolderLocalPermissions()
    {
        // Add a user to localset permissions from the local users and groups section.
        authoritySelectDialog = managePermissions.clickOnSelectUsersAndGroups();
        authoritySelectDialog.authoritySearch("Administrator").clickAddButton();

        // verify the above user in the local permissions user list.
        List<String> localRoleList = managePermissions.getLocalRoles();
        assertTrue(localRoleList.contains("Administrator"));

        // Save the changes
        managePermissions.clickOnOK();

    }

    // turnoff inherit permissions for record folder
    @Test(dependsOnMethods = "setFolderLocalPermissions")
    public void verifyTurnOffInheritPermissions()
    {
        // turn off inherit permissions
        managePermissions = filePlan.getRecordFolder(RECORD_FOLDER_ONE).clickonManagePermissions();
        managePermissions.clickOnInheritPermissionButton().clickOnConfirm();

        // verify inheritpermissions is enabled for folder
        assertTrue(managePermissions.isInheritPermissionsEnabled());

        // verify add users/groups button is enabled
        assertTrue(managePermissions.isAddUserGroupEnabled());
    }

    // change authority permission type
    @Test(dependsOnMethods = "verifyTurnOffInheritPermissions")
    public void changeAuthorityPermissionType()
    {
        // add test authority
        // managePermissions.clickOnInheritPermissionButton().clickOnCancel();
        authoritySelectDialog = managePermissions.clickOnSelectUsersAndGroups();
        authoritySelectDialog.authoritySearch(testUserName).clickAddButton();

        //get the authority permission type
        //actualPermissionsName = managePermissions.getPermission(testUserName);
        actualPermissionsName = managePermissions.getPermission("Administrator");

        assertTrue(actualPermissionsName.equals(expectedPermissionName));
        
       //set the authority permission type
        expectedPermissionName = "Read and File";
        managePermissions.setPermissions("Administrator", expectedPermissionName);

        expectedPermissionName = "Read Only";
        // get the test authority permission type and assert ther results
        actualPermissionsName = managePermissions.getPermission(testUserName);
        assertTrue(actualPermissionsName.equals(expectedPermissionName));

        // set the authority permission type
        expectedPermissionName = "Read and File";
        managePermissions.setPermissions(testUserName, expectedPermissionName);

        // get the test authority permission type and assert ther results
        actualPermissionsName = managePermissions.getPermission(testUserName);
        assertTrue(actualPermissionsName.equals(expectedPermissionName));
    }

    @Test(dependsOnMethods = "changeAuthorityPermissionType")
    public void deletelocalUSer()
    {

        // delete the user from locallysetRoles
        managePermissions.deleteAuthority(testUserName);

        // verify the delete user from above method do not exist anymore
        List<String> localRoleList = managePermissions.getLocalRoles();
        assertFalse(localRoleList.contains(testUserName));

        // cancel any changes
        managePermissions.clickOnOkCancel();
    }

    @AfterClass
    public void afterClass()
    {
        deleteRMSite();
        deleteUser(testUser);
    }
}
