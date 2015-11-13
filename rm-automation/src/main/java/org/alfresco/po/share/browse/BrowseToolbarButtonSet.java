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
package org.alfresco.po.share.browse;

import org.alfresco.po.common.buttonset.ButtonSet;
import org.openqa.selenium.support.FindBy;

import ru.yandex.qatools.htmlelements.element.Button;

/**
 * Browse toolbar button set
 * 
 * @author Roy Wetherall
 */
public abstract class BrowseToolbarButtonSet extends ButtonSet
{
    /** file upload button */
    @FindBy(css="button[id$='fileUpload-button-button']")
    protected Button fileUpload;
    
     /** create content button */
    @FindBy(css="button[id$='createContent-button-button']")
    protected Button createContent;
}
