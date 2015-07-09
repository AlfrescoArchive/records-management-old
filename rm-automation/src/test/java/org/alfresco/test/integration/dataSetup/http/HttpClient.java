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
package org.alfresco.test.integration.dataSetup.http;

import java.io.IOException;
import java.text.MessageFormat;
import java.util.Map;

import javax.annotation.PostConstruct;

import org.alfresco.test.ModuleProperties;
import org.apache.commons.httpclient.HttpMethod;
import org.apache.commons.httpclient.UsernamePasswordCredentials;
import org.apache.commons.httpclient.auth.AuthScope;
import org.apache.commons.httpclient.methods.ByteArrayRequestEntity;
import org.apache.commons.httpclient.methods.DeleteMethod;
import org.apache.commons.httpclient.methods.GetMethod;
import org.apache.commons.httpclient.methods.PostMethod;
import org.apache.commons.httpclient.methods.PutMethod;
import org.apache.commons.httpclient.params.HttpClientParams;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;


/**
 * Http Client for REST API calls
 * 
 * @author Roy Wetherall
 * @since 3.0.a
 */
@Component
public class HttpClient
{
    @Autowired
    private ModuleProperties moduleProperties;
    
    /** http client */
    private org.apache.commons.httpclient.HttpClient httpClient = null;
 
    /**
     * Init method
     */
    @PostConstruct
    public void init() throws Exception
    {
        httpClient = new org.apache.commons.httpclient.HttpClient();
        httpClient.getParams().setBooleanParameter(HttpClientParams.PREEMPTIVE_AUTHENTICATION, true);
        httpClient.getState().setCredentials(
                    new AuthScope(AuthScope.ANY_HOST, AuthScope.ANY_PORT), 
                    new UsernamePasswordCredentials(moduleProperties.getAdminName(), moduleProperties.getAdminPassword()));           
    }
    
    /**
     * Send get request
     */
    public Response sendGetRequest(String uri, Object ... uriContext)
    {
        try
        {
            return sendRequest(
                        new Request.GetRequest(
                                    MessageFormat.format(uri, uriContext)));
        }
        catch (IOException e)
        {
            throw new RuntimeException(e);
        }
    }
    
    /**
     * Send post request
     */
    public Response sendPostRequest(String uri, String body, Object ... uriContext)
    {
        try
        {

           Request request = new Request.PostRequest(
                                           MessageFormat.format(uri, uriContext), 
                                           body, 
                                           Request.APPLICATION_JSON);           
           return sendRequest(request);
        }
        catch (IOException e)
        {
            throw new RuntimeException(e);
        }
    }
    
    /** 
     * Send delete request
     */
    public Response sendDeleteRequest(String uri, Object ... uriContext)
    {
        try
        {      
            Request request = new Request.DeleteRequest(MessageFormat.format(uri, uriContext));            
            return sendRequest(request);
        }
        catch (IOException e)
        {
            throw new RuntimeException(e);
        }
    }
    
    /**
     * Send request to http client
     */
    public Response sendRequest(Request req) throws IOException
    {
        String uri = req.getFullUri();
        if (!uri.startsWith("http"))
        {
            uri = moduleProperties.getAlfrescoURL() + uri;
        }
        
        // construct method
        HttpMethod httpMethod = null;
        String method = req.getMethod();
        if (method.equalsIgnoreCase("GET"))
        {
            GetMethod get = new GetMethod(uri);
            httpMethod = get;
        }
        else if (method.equalsIgnoreCase("POST"))
        {
            PostMethod post = new PostMethod(uri);
            post.setRequestEntity(new ByteArrayRequestEntity(req.getBody(), req.getType()));
            httpMethod = post;
        }
        else if (method.equalsIgnoreCase("PUT"))
        {
            PutMethod put = new PutMethod(uri);
            put.setRequestEntity(new ByteArrayRequestEntity(req.getBody(), req.getType()));
            httpMethod = put;
        }
        else if (method.equalsIgnoreCase("DELETE"))
        {
            DeleteMethod del = new DeleteMethod(uri);
            httpMethod = del;
        }
        else
        {
            throw new RuntimeException("Http Method " + method + " not supported");
        }
        if (req.getHeaders() != null)
        {
            for (Map.Entry<String, String> header : req.getHeaders().entrySet())
            {
                httpMethod.setRequestHeader(header.getKey(), header.getValue());
            }
        }

        // execute method
        httpClient.executeMethod(httpMethod);
        return new Response(httpMethod);
    }  
}
