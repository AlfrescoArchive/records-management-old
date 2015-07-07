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
package org.alfresco.po.share.upload;

import static org.alfresco.po.common.util.Utils.createTempFile;

import java.io.File;
import java.io.IOException;

import org.alfresco.po.common.Dialog;
import org.alfresco.po.common.renderable.Renderable;
import org.alfresco.po.common.util.Utils;
import org.alfresco.po.share.page.SharePage;
import org.openqa.selenium.By;
import org.openqa.selenium.TimeoutException;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.springframework.stereotype.Component;

import ru.yandex.qatools.htmlelements.element.FileInput;

/**
 * File upload dialog
 * 
 * @author Roy Wetherall
 */
@Component
public class UploadDialog extends Dialog
{
    /** file input */
    @FindBy(className="dnd-file-selection-button")
    private FileInput fileInput;
    
    /**
     * Upload file with given name.
     * 
     * @param  name                 file name
     * @return {@link Renderable}   last rendered page
     */
    public Renderable uploadFile(String name)
    {
        // create temporary file
        return uploadFile(createTempFile(name));
    }
    
    /**
     * Upload file with given name
     * 
     * @param name          file name
     * @param renderable    resulting renderable page
     * @return T            rendered return page
     */
    public <T extends Renderable> T uploadFile(String name, T renderable)
    {
        return uploadFile(createTempFile(name), renderable);
    }
    
    /**
     * Upload file
     * 
     * @param  file                 file
     * @return {@link Renderable}   last rendered page
     */
    public Renderable uploadFile(File file)
    {
        return uploadFile(file, SharePage.getLastRenderedPage());
    }
    
    /**
     * Upload file
     * 
     * @param  file                 file
     * @param  renderable           resulting renderable page
     * @return T                    rendered return page
     */
    public <T extends Renderable> T uploadFile(File file, T renderable)
    {
        try
        {
            // set the file to upload
            fileInput.setFileToUpload(file.getCanonicalPath());
            
            // wait for the file to be visible
            String fileName = file.getName().split("\\.")[0];
            By selector = By.xpath("//a[contains(., 'a" + fileName + "')]");            
            try { Utils.webDriverWait(2).until(ExpectedConditions.visibilityOfElementLocated(selector)); } catch (TimeoutException exception) { /*ignore and carry on */ }
            
            // render and return last page
            return renderable.render();
        }
        catch (IOException e)
        {
            throw new RuntimeException("Unable to upload file.", e);
        }
    }
}
