/**
 * 
 */
package org.alfresco.test.integration.dataSetup;

import org.alfresco.po.share.console.users.UsersPage;
import org.alfresco.test.BaseTest;
import org.junit.Assert;
import org.springframework.beans.factory.annotation.Autowired;
import org.testng.annotations.Test;

/**
 * @author Roy Wetherall
 * @since 3.0.a
 */
public class DataBootstrapTest extends BaseTest
{
    private static final String TEST_USER = "bob";
    
    @Autowired
    private DataBootstrap dataBootstrap;
    
    @Autowired
    private UsersPage usersPage;
    
    @Test
    public void testUserBootstap()
    {
        Assert.assertFalse(dataBootstrap.existsUser(TEST_USER));     
        Assert.assertFalse(
            openPage(usersPage)
                     .setSearch(TEST_USER)
                     .clickOnSearch()
                     .isUserFound(TEST_USER));  
        
        dataBootstrap.createUser(TEST_USER);
        Assert.assertTrue(dataBootstrap.existsUser(TEST_USER));     
        Assert.assertTrue(
            openPage(usersPage)
                     .setSearch(TEST_USER)
                     .clickOnSearch()
                     .isUserFound(TEST_USER));
        
        dataBootstrap.deleteUser(TEST_USER);
        Assert.assertFalse(dataBootstrap.existsUser(TEST_USER));          
        Assert.assertFalse(
           openPage(usersPage)
                    .setSearch(TEST_USER)
                    .clickOnSearch()
                    .isUserFound(TEST_USER));
    }
}
