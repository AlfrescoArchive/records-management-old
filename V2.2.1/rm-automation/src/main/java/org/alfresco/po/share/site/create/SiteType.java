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
package org.alfresco.po.share.site.create;

/**
 * Site type enumeration
 * 
 * @author Roy Wetherall
 */
public enum SiteType
{
    COLLABORATION_SITE("site-dashboard"), RM_SITE("rm-site-dashboard");

    private String value;

    SiteType(String value)
    {
        this.value = value;
    }

    @Override
    public String toString()
    {
        return this.value;
    }

    public static final SiteType fromValue(String value)
    {
        if (value.equals("site-dashboard")) { return COLLABORATION_SITE; }
        if (value.equals("rm-site-dashboard")) { return RM_SITE; }
        throw new RuntimeException("Can't get SiteType from value " + value);
    }
}