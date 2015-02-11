package org.alfresco.po.rm.browse.unfiledrecords;

import org.alfresco.po.common.Toolbar;
import org.alfresco.po.rm.dialog.create.NewUnfiledRecordFolderDialog;
import org.openqa.selenium.support.FindBy;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * Unfiled records toolbar
 * 
 * @author Roy Wetherall
 */
@Component
public class UnfiledRecordsToolbar extends Toolbar
{
    /** buttons */
    private static final String NEW_UNFILED_RECORD_FOLDER = "newUnfiledRecordsFolder";
    
    /** toolbar buttons */
    @FindBy(css="div.header-bar")
    private UnfiledRecordsToolbarButtonSet toolbarButtons;
    
    /** new unfiled record folder dialog */
    @Autowired
    private NewUnfiledRecordFolderDialog unfiledRecordFolderDialog;
    
    /**
     * is new unfiled record folder clickable
     */
    public boolean isClickableNewUnfiledRecordFolder()
    {
        return toolbarButtons.isButtonClickable(NEW_UNFILED_RECORD_FOLDER);
    }
    
    /**
     * click on new unfiled record folder
     */
    public NewUnfiledRecordFolderDialog clickOnNewUnfiledRecordFolder()
    {
        return toolbarButtons.click(NEW_UNFILED_RECORD_FOLDER, unfiledRecordFolderDialog);
    }
}
