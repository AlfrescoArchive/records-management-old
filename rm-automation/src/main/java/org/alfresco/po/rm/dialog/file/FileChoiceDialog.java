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
package org.alfresco.po.rm.dialog.file;

import org.alfresco.po.common.Dialog;
import org.alfresco.po.common.renderable.Renderable;
import org.alfresco.po.rm.dialog.create.NewNonElectronicRecordDialog;
import org.alfresco.po.share.page.SharePage;
import org.alfresco.po.share.upload.UploadDialog;
import org.openqa.selenium.support.FindBy;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import ru.yandex.qatools.htmlelements.element.Button;

/**
 * File choice dialog
 * 
 * @author Roy Wetherall
 */
@Component
public class FileChoiceDialog extends Dialog
{
    /** button names */
    public static final String ELECTRONIC ="electronic";
    public static final String NON_ELECTRONIC = "nonElectronic";
    
    /** electronic button */
    @FindBy(xpath=".//span[@class='button-group']/span[1]/span/button")
    private Button electronic;
    
    /** non electronic button */
    @FindBy(xpath=".//span[@class='button-group']/span[2]/span/button")
    private Button nonElectronic;
    
    /** cancel button */
    @FindBy(xpath=".//span[@class='button-group']/span[3]/span/button")
    private Button cancel;
    
    /** non electronic record dialog */
    @Autowired
    private NewNonElectronicRecordDialog newNonElectronicDialog;
    
    /** file upload dialog */
    @Autowired
    private UploadDialog uploadDialog;
    
    /**
     * Click on non-electronic
     * 
     * @return {@link NewNonElectronicRecordDialog} new non-electronic record dialog
     */
    public NewNonElectronicRecordDialog clickOnNonElectronic()
    {
        nonElectronic.click();
        return newNonElectronicDialog.render();        
    }
    
    /**
     * Click on electronic 
     * 
     * @return {@link UploadDialog} upload dialog
     */
    public UploadDialog clickOnElectronic()
    {
        electronic.click();
        return uploadDialog.render();
    }
    
    /**
     * Click on cancel
     * 
     * @return {@link Renderable}   last rendered page
     */
    public Renderable clickOnCancel()
    {
        cancel.click();
        return SharePage.getLastRenderedPage().render();
    }
}
