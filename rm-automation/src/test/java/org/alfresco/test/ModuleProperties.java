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
package org.alfresco.test;

import org.springframework.stereotype.Component;

import ru.yandex.qatools.properties.PropertyLoader;
import ru.yandex.qatools.properties.annotations.Property;
import ru.yandex.qatools.properties.annotations.Resource;

/**
 * This class gives the possibility to use the properties
 * defined in module.properties in the code.
 *
 * @author Tuna Aksoy
 * @since 2.2
 * @version 1.0
 */
@Component("ModuleProperties")
@Resource.Classpath("module.properties")
public class ModuleProperties
{
    /**
     * Constructor
     */
    public ModuleProperties()
    {
        PropertyLoader.populate(this);
    }
    
    @Property("share.url")
    private String shareUrl;

    /** Property for admin user name */
    @Property("admin.name")
    private String adminName;

    /** Property for admin password */
    @Property("admin.password")
    private String adminPassword;

    /**
     * Gets the Share URL
     *
     * @return {@link String} Share URL
     */
    public String getShareURL()
    {
        return shareUrl;
    }

    /**
     * Gets the admin user name
     *
     * @return {@link String} Admin user name
     */
    public String getAdminName()
    {
        return adminName;
    }

    /**
     * Gets the admin user password
     *
     * @return {@link String} Admin password
     */
    public String getAdminPassword()
    {
        return adminPassword;
    }
}
