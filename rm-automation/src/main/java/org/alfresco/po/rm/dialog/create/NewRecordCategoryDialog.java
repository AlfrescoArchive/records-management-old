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
package org.alfresco.po.rm.dialog.create;

import org.alfresco.po.rm.properties.IdentifierAndVitalInformation;
import org.alfresco.po.share.form.FormDialog;
import org.openqa.selenium.support.FindBy;
import org.springframework.stereotype.Component;

/**
 * New record category dialog
 * 
 * @author Roy Wetherall
 */
@Component
public class NewRecordCategoryDialog extends FormDialog
{
    @FindBy(xpath = "//div[contains(@id,'-form-fields')]")
    private IdentifierAndVitalInformation identifierAndVitalInformation;

    public IdentifierAndVitalInformation getIdentifierAndVitalInformation()
    {
        return identifierAndVitalInformation;
    }

    public void setIdentifierAndVitalInformation(IdentifierAndVitalInformation identifierAndVitalInformation)
    {
        this.identifierAndVitalInformation = identifierAndVitalInformation;
    }

}
