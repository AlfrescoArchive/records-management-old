/**
 * 
 */
package org.alfresco.test.integration.dataSetup;

import org.alfresco.test.TestData;
import org.alfresco.test.integration.dataSetup.http.HttpClient;
import org.alfresco.test.integration.dataSetup.http.Response;
import org.json.JSONException;
import org.json.JSONObject;
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
            JSONObject obj = new JSONObject();
            try
            {
                obj.put("userName", userName);    
                obj.put("firstName", userName);
                obj.put("lastName", userName);
                obj.put("email", userName + "@alfresco.com");
                obj.put("password", DEFAULT_PASSWORD);
            }
           catch (JSONException e)
           {
               throw new RuntimeException(e);
           }
        
           httpClient.sendPostRequest("/service/api/people", obj.toString());
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
            httpClient.sendDeleteRequest("/service/api/people/{0}", userName);
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
        JSONObject obj = new JSONObject();
        try
        {
            obj.put("role", role);                
            JSONObject person = new JSONObject();
            person.put("userName", userName);
            obj.put("person", person);            
        }
       catch (JSONException e)
       {
           throw new RuntimeException(e);
       }        
        
       System.out.println(obj.toString()); 
       httpClient.sendPostRequest("/service/api/sites/{0}/memberships", obj.toString(), siteId);
    }
}
