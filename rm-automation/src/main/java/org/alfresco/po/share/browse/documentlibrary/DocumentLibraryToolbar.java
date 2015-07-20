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
package org.alfresco.po.share.browse.documentlibrary;

import org.alfresco.po.common.Toolbar;
import org.alfresco.po.rm.dialog.file.FileChoiceDialog;
import org.alfresco.po.share.upload.UploadDialog;
import org.openqa.selenium.support.FindBy;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * Document library toolbar
 * 
 * @author Roy Wetherall
 */
@Component
public class DocumentLibraryToolbar extends Toolbar
{
    public static final String FILE_UPLOAD = "fileUpload";
    
    /** toolbar buttons */
    @FindBy(css="div.header-bar")
    private DocumentLibraryToolbarButtonSet toolbarButtons;
    
    @Autowired
    private UploadDialog uploadDialog;
    
    /**
     * @return  boolean true if the file upload button is clickable, false otherwise
     */
    public boolean isUploadClickable()
    {
        return toolbarButtons.isButtonClickable(FILE_UPLOAD);
    }
    
    /**
     * Click on file button
     * 
     * @return {@link FileChoiceDialog} file choice dialog
     */
    public UploadDialog clickOnUpload()
    {
        return toolbarButtons.click(FILE_UPLOAD, uploadDialog);
    }
}
