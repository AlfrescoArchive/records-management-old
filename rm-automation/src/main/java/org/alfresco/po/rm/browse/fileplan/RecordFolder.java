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

import static org.alfresco.po.common.util.Utils.elementExists;

import org.alfresco.po.rm.details.folder.FolderDetailsPage;
import org.alfresco.po.rm.dialog.copymovelinkfile.CopyDialog;
import org.alfresco.po.rm.dialog.copymovelinkfile.MoveDialog;
import org.openqa.selenium.By;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

/**
 * @author Roy Wetherall
 */
@Scope("prototype")
@Component
public class RecordFolder extends DisposableItem implements FolderActions
{
    /** cutoff indicator */
    private static By cutoffFolderIndicatorSelector = By.cssSelector("img[alt='rm-cutoff-folder']");

    /** closed indicator */
    private static By closedSelector = By.cssSelector("img[alt='rm-closed']");

    /** copy dialog */
    @Autowired
    private CopyDialog copyDialog;

    /** move dialog */
    @Autowired
    private MoveDialog moveDialog;

    /** record folder details page */
    @Autowired
    private FolderDetailsPage folderDetailsPage;

    /**
     * @return  true if folder is closed, false otherwise
     */
    public boolean isClosed()
    {
        return elementExists(getRow(), closedSelector);
    }

    /**
     * Click on View Details
     */
    public FolderDetailsPage clickOnViewDetails()
    {
        return clickOnAction(VIEW_DETAILS, folderDetailsPage);
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

    /**
     * Click on close folder action
     */
    public FilePlan clickOnCloseFolder()
    {
        return (FilePlan)clickOnAction(CLOSE_FOLDER);
    }

    /**
     * Click on reopen folder action
     */
    public FilePlan clickOnReopenFolder()
    {
        return (FilePlan)clickOnAction(REOPEN_FOLDER);
    }

    /**
     * @return  true if folder is cut off, false otherwise
     */
    public boolean isCutOff()
    {
        return elementExists(getRow(), cutoffFolderIndicatorSelector);
    }

}
