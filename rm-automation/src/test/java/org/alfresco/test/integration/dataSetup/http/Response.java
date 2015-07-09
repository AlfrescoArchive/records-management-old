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
import java.io.UnsupportedEncodingException;

import org.apache.commons.httpclient.Header;
import org.apache.commons.httpclient.HttpMethod;

/**
 * Response implementation
 * 
 * @author Roy Wetherall
 * @since 3.0.a
 */
public class Response
{
    private HttpMethod method;
    
    public Response(HttpMethod method)
    {
        this.method = method;
    }

    public byte[] getContentAsByteArray()
    {
        try
        {
            return method.getResponseBody();
        }
        catch (IOException e)
        {
            return null;
        }
    }

    public String getContentAsString() throws UnsupportedEncodingException
    {
        try
        {
            return method.getResponseBodyAsString();
        }
        catch (IOException e)
        {
            return null;
        }
    }

    public String getContentType()
    {
        return getHeader("Content-Type");
    }

    public int getContentLength()
    {
        try
        {
            return method.getResponseBody().length;
        }
        catch (IOException e)
        {
            return 0;
        }
    }

    public String getHeader(String name)
    {
        Header header = method.getResponseHeader(name);
        return (header != null) ? header.getValue() : null;
    }

    public int getStatus()
    {
        return method.getStatusCode();
    }

}