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
package org.alfresco.po.rm.dialog;

import static org.alfresco.po.common.util.Utils.waitForInvisibilityOf;

import java.io.File;
import java.util.UUID;

import org.alfresco.po.common.Dialog;
import org.alfresco.po.common.Page;
import org.alfresco.po.common.util.Utils;
import org.alfresco.po.share.page.SharePage;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.springframework.stereotype.Component;

/**
 * The upload new version dialog.
 *
 * @author Tom Page
 * @since 2.4.a
 */
@Component
public class UploadNewVersionDialog extends Dialog
{
    @FindBy(css=".dnd-file-selection-button")
    private WebElement fileSelectButton;
    @FindBy(css="#template_x002e_dnd-upload_x002e_documentlibrary_x0023_default-upload-button-button")
    private WebElement uploadButton;

    /**
     * Select a file to upload.
     *
     * @param path The path to the file.
     * @return The 'upload new version' dialog.
     */
    public UploadNewVersionDialog selectFile(String path)
    {
        fileSelectButton.sendKeys(path);
        return this;
    }

    /** Confirm that the file should be uploaded. */
    public Page upload()
    {
        uploadButton.click();
        waitForInvisibilityOf(uploadButton);
        return SharePage.getLastRenderedPage().render();
    }

    /** Upload a new version of a document. */
    public Page uploadFakeDocument()
    {
        File localFile = Utils.createTempFile(UUID.randomUUID().toString());
        return this.selectFile(localFile.getAbsolutePath())
                   .upload();
    }
}
