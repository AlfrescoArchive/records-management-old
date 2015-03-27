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
package org.alfresco.po.rm.actions.edit;

import org.alfresco.po.rm.properties.Location;
import org.alfresco.po.rm.properties.NonElectronicRecord;
import org.alfresco.po.share.form.FormPage;
import org.alfresco.po.share.properties.Content;
import org.openqa.selenium.support.FindBy;
import org.springframework.stereotype.Component;

/**
 * Edit non electronic record page
 * 
 * @author Tatiana Kalinovskaya
 */
@Component
public class EditNonElectronicRecordPage extends FormPage
{
    @FindBy(xpath = "//*[@class='set'][1]")
    private NonElectronicRecord nonElectronicRecord;

    @FindBy(xpath = "//*[@class='set'][1]")
    private Content content;

    @FindBy(xpath = "//*[@class='set'][2]")
    private Location location;

    public NonElectronicRecord getNonElectronicRecord()
    {
        return nonElectronicRecord;
    }

    public Content getContent()
    {
        return content;
    }

    public Location getLocation()
    {
        return location;
    }
}
