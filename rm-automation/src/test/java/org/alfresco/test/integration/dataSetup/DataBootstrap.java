/**
 * 
 */
package org.alfresco.test.integration.dataSetup;

import org.alfresco.dataprep.UserService;
import org.alfresco.test.ModuleProperties;
import org.alfresco.test.TestData;
import org.alfresco.test.integration.dataSetup.http.HttpClient;
import org.alfresco.test.integration.dataSetup.http.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * @author Roy Wetherall
 * @since 3.0.a
 */
@Component
public class DataBootstrap implements TestData
{
    @Autowired
    private HttpClient httpClient;
    @Autowired
    UserService userService;
    @Autowired
    ModuleProperties moduleProperties;
    /**
     * exists user
     */
    public boolean existsUser(String userName)
    {
        return (httpClient.sendGetRequest("/service/api/people/{0}", userName).getStatus() == 200);
    }
    
    /**
     * create user
     */
    public void createUser(String userName)
    {
        if (!existsUser(userName))
        {
            userService.create(moduleProperties.getAdminName(), moduleProperties.getAdminPassword(), userName, DEFAULT_PASSWORD, userName + "@alfresco.com");
        }
    }
    
    /**
     * create user with RM role
     */
    public void createUser(String userName, String role)
    {
        // check that the user doesn't already exist
        if (!existsUser(userName))
        {
            // create user
            createUser(userName);

            // if a role is provided
            if (role != null)
            {
                assignUserToRole(userName, role);
            }
        }
    }
    
    /**
     * delete user
     */
    public void deleteUser(String userName)
    {
        if (existsUser(userName))
        {
            userService.delete(moduleProperties.getAdminName(), moduleProperties.getAdminPassword(), userName);
        }
    }
    
    /**
     * assign user to role
     */
    public void assignUserToRole(String userName, String role)
    {
        Response response = httpClient.sendPostRequest("/service/api/rm/roles/{0}/authorities/{1}", "", role, userName);
        if (response.getStatus() != 200)
        {
            throw new RuntimeException("Unable to assign user to role.  Error status " + response.getStatus() + " - " + response.getContentAsString());
        }
    }    
    
    public void addSiteMembership(String siteId, String userName, String role)
    {
        userService.createSiteMember(moduleProperties.getAdminName(), moduleProperties.getAdminPassword(), userName, siteId, role);
    }
}
