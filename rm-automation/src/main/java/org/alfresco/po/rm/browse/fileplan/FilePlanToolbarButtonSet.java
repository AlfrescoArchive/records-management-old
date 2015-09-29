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
package org.alfresco.po.rm.browse.fileplan;

import org.alfresco.po.share.browse.BrowseToolbarButtonSet;
import org.openqa.selenium.support.FindBy;

import ru.yandex.qatools.htmlelements.element.Button;

/**
 * @author Roy Wetherall
 */
public class FilePlanToolbarButtonSet extends BrowseToolbarButtonSet
{
    /** new record category button */
    @FindBy(css="button[id$='newCategory-button-button']")
    private Button newCategory;
    
    /** new record folder button */
    @FindBy(css="button[id$='newFolder-button-button']")
    private Button newRecordFolder;
}
