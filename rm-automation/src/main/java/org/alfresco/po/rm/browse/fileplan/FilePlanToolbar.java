package org.alfresco.po.rm.browse.fileplan;

import org.alfresco.po.common.Toolbar;
import org.alfresco.po.rm.dialog.create.NewRecordCategoryDialog;
import org.alfresco.po.rm.dialog.create.NewRecordFolderDialog;
import org.alfresco.po.rm.dialog.file.FileChoiceDialog;
import org.openqa.selenium.support.FindBy;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * File plan toolbar
 * 
 * @author Roy Wetherall
 */
@Component
public class FilePlanToolbar extends Toolbar
{
    /** button names */
    public static final String NEW_CATEGORY = "newCategory";
    public static final String NEW_RECORD_FOLDER = "newRecordFolder";
    public static final String FILE_UPLOAD = "fileUpload";
    
    /** toolbar */
    @FindBy(css="div.header-bar")
    private FilePlanToolbarButtonSet toolbarButtons;

    /** new record category dialog */
    @Autowired
    private NewRecordCategoryDialog newRecordCategoryDialog;
    
    /** new record folder dialog */
    @Autowired 
    private NewRecordFolderDialog newRecordFolderDialog;
    
    @Autowired
    private FileChoiceDialog fileChoiceDialog;    

    /**
     * @return  true if new category button is clickable, false otherwise
     */
    public boolean isNewCategoryClickable()
    {
        return toolbarButtons.isButtonClickable(NEW_CATEGORY);
    }
    
    /**
     * Click on the new category button
     * 
     * @return  {@link NewRecordCategoryDialog} new record category dialog
     */
    public NewRecordCategoryDialog clickOnNewCategory()
    {
        return toolbarButtons.click(NEW_CATEGORY, newRecordCategoryDialog);
    }
    
    /**
     * @return  boolen  true if the new record folder button is clickable, false otherwise
     */
    public boolean isNewRecordFolderClickable()
    {
        return toolbarButtons.isButtonClickable(NEW_RECORD_FOLDER);
    }
    
    /**
     * Click on the new record folder button
     * 
     * @return  {@link NewRecordFolderDialog}   new record folder dialog
     */
    public NewRecordFolderDialog clickOnNewRecordFolder()
    {
        return toolbarButtons.click(NEW_RECORD_FOLDER, newRecordFolderDialog);
    }
    
    /**
     * @return  boolean true if the file upload button is clickable, false otherwise
     */
    public boolean isFileUploadClickable()
    {
        return toolbarButtons.isButtonClickable(FILE_UPLOAD);
    }
    
    /**
     * Click on file button
     * 
     * @return {@link FileChoiceDialog} file choice dialog
     */
    public FileChoiceDialog clickOnFile()
    {
        return toolbarButtons.click(FILE_UPLOAD, fileChoiceDialog);
    }
}
