/*
 * Copyright (C) 2005-2015 Alfresco Software Limited.
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
package org.alfresco.dataprep;

import java.text.MessageFormat;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.apache.commons.httpclient.HttpStatus;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.json.simple.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Records management data prep service
 * 
 * @author Roy Wetherall
 */
@Service
public class RecordsManagementService
{
    /** uri's */
    private static final String RM_ROLES_AUTHORITIES = "{0}rm/roles/{1}/authorities/{2}?alf_ticket={3}";
    
    /** http client factory */
    @Autowired private AlfrescoHttpClientFactory alfrescoHttpClientFactory;

    /** user service */
    @Autowired private UserService userService;
    
    /**
     * create user and assign to records management role
     */
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
                    RM_ROLES_AUTHORITIES,
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
    
    /**
     * invite user to site with given role
     */
    public void inviteUserToSite(
            String adminUser, 
            String adminPassword,
            String userName, 
            String siteName,
            String role)
    {
        if (userService.userExists(adminUser, adminPassword, userName))
        {
            try {
                userService.inviteUserToSiteAndAccept(adminUser, adminPassword, userName, siteName, role);
            } catch (Exception ex) {
                Logger.getLogger(RecordsManagementService.class.getName()).log(Level.SEVERE, null, "The user could not be invited to site. " + ex);
            }
        }
    }


}
