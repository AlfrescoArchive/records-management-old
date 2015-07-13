/**
 *
 */
package org.alfresco.dataprep;

import java.text.MessageFormat;

import org.apache.commons.httpclient.HttpStatus;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.json.simple.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author Roy Wetherall
 * @since 3.0.a
 */
@Service
public class RecordsManagementService
{
    @Autowired private AlfrescoHttpClientFactory alfrescoHttpClientFactory;

    @Autowired private UserService userService;

    public void createUserAndAssignToRole(
            String adminUser, 
            String adminPassword,
            String userName, 
            String password, 
            String email, 
            String role, 
            String firstName,
            String lastName)
    {
        if (!userService.userExists(adminUser, adminPassword, userName))
        {
            userService.create(adminUser, adminPassword, userName, password, email, firstName, lastName);
            assignUserToRole(adminUser, adminPassword, userName, role);
        }
    }

    /**
     * assign user to records management role
     */
    public boolean assignUserToRole(String adminUser, String adminPassword, String userName, String role)
    {
        AlfrescoHttpClient client = alfrescoHttpClientFactory.getObject();
        String reqURL = MessageFormat.format(
                    "{0}rm/roles/{1}/authorities/{2}?alf_ticket={3}",
                    client.getApiUrl(),
                    role,
                    userName,
                    client.getAlfTicket(adminUser, adminPassword));

        HttpPost request = null;
        HttpResponse response = null;
        try
        {
            request = client.generatePostRequest(reqURL, new JSONObject());
            response = client.executeRequest(request);
            switch (response.getStatusLine().getStatusCode())
            {
                case HttpStatus.SC_OK:
                    return true;
                case HttpStatus.SC_CONFLICT:
                    break;
                default:
                    break;
            }
        }
        finally
        {
            if (request != null)
            {
                request.releaseConnection();
            }
            client.close();
        }

        return false;
    }

}
