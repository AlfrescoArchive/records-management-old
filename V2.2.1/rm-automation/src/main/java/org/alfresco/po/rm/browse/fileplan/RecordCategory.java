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

import org.alfresco.po.rm.details.category.CategoryDetailsPage;
import org.alfresco.po.rm.dialog.copymovelinkfile.CopyDialog;
import org.alfresco.po.rm.dialog.copymovelinkfile.MoveDialog;
import org.alfresco.po.rm.managepermissions.ManagePermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

/**
 * @author Roy Wetherall
 */
@Scope("prototype")
@Component
public class RecordCategory extends FilePlanListItem implements CategoryActions
{
    /** copy dialog */
    @Autowired
    private CopyDialog copyDialog;

    /** move dialog */
    @Autowired
    private MoveDialog moveDialog;

    /** category details */
    @Autowired
    private CategoryDetailsPage categoryDetailsPage;
    
    /** manage permissions */
    @Autowired
    private ManagePermissions managePermissions;

    /**
     * Click on View Details
     */
    public CategoryDetailsPage clickOnViewDetails()
    {
        return clickOnAction(VIEW_DETAILS, categoryDetailsPage);
    }

    /**
     * Click on copy action
     */
    public CopyDialog clickOnCopyTo()
    {
        return clickOnAction(COPY, copyDialog);
    }

    /**
     * Click on move action
     */
    public MoveDialog clickOnMoveTo()
    {
        return clickOnAction(MOVE, moveDialog);
    }
    /**Click on manage permissions action */
    public ManagePermissions clickonManagePermissions()
    {
        return clickOnAction(MANAGE_PERMISSIONS, managePermissions);
    }


}
