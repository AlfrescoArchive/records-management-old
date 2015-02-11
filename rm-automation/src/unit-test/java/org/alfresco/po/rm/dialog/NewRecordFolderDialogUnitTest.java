package org.alfresco.po.rm.dialog;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.alfresco.po.rm.browse.fileplan.FilePlan;
import org.alfresco.po.rm.dialog.create.NewRecordCategoryDialog;
import org.alfresco.po.rm.dialog.create.NewRecordFolderDialog;
import org.alfresco.test.BaseRmUnitTest;
import org.springframework.beans.factory.annotation.Autowired;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

/**
 * Created by tatiana.kalinovskaya on 06.11.2014.
 */
@Test (groups = {"unit-test"})
public class NewRecordFolderDialogUnitTest extends BaseRmUnitTest
{
    @Autowired
    private NewRecordCategoryDialog newRecordCategoryDialog;

    @Autowired
    private NewRecordFolderDialog newRecordFolderDialog;
    
    /** file plan */
    @Autowired
    private FilePlan filePlan;

    @BeforeMethod
    public void beforeMethod()
    {
        // create RM site
        createRMSite();
        
        // open record category dialog
        openPage(filePlan, RM_SITE_ID, "documentlibrary");
        
        // create record category if it doesn't exist
        if (filePlan.getRecordCategory(RECORD_CATEGORY_NAME) == null)
        {         
            filePlan
                .getToolbar()
                .clickOnNewCategory()
                .setName(RECORD_CATEGORY_NAME)
                .setTitle(TITLE)
                .clickOnSave();
        }
        
        // navigate to the record category
        filePlan
            .getList()
            .get(RECORD_CATEGORY_NAME)
            .clickOnLink();
        
        // open the new record folder dialog
        filePlan.getToolbar().clickOnNewRecordFolder();
    }

    @Test
    public void verifyDialogDefaultValues()
    {
        assertTrue(newRecordFolderDialog.getName().isEmpty());
        assertTrue(newRecordFolderDialog.getTitle().isEmpty());
        assertTrue(newRecordFolderDialog.getDescription().isEmpty());
        assertFalse(newRecordFolderDialog.getIdentifierAndVitalInformation().getIdentifier().isEmpty());
        assertTrue(newRecordFolderDialog.getLocation().getLocationField().isEmpty());
        assertFalse(newRecordFolderDialog.getIdentifierAndVitalInformation().isVitalIndicatorSelected());
        assertEquals(VitalReviewPeriod.NONE, newRecordFolderDialog.getIdentifierAndVitalInformation().getReviewPeriod());
        assertTrue(newRecordFolderDialog.getIdentifierAndVitalInformation().getPeriodExpression().isEmpty());
    }

    @Test(dependsOnMethods="verifyDialogDefaultValues")
    public void verifySettingValues()
    {
        int periodExpression = 3;
        String recordIdentifier = RECORD_IDENTIFIER +  + System.currentTimeMillis();
        // enter details of new record category
        newRecordFolderDialog.setName(RECORD_FOLDER_ONE).setTitle(TITLE).setDescription(DESCRIPTION);
        newRecordFolderDialog.getIdentifierAndVitalInformation().setIdentifier(recordIdentifier)
                .checkVitalIndicator(true).setReviewPeriod(VitalReviewPeriod.DAY)
                .setPeriodExpression(String.valueOf(periodExpression));
        newRecordFolderDialog.getLocation().setLocationField(LOCATION);
        // verify that entered details are correct
        assertEquals(RECORD_FOLDER_ONE, newRecordFolderDialog.getName());
        assertEquals(TITLE, newRecordFolderDialog.getTitle());
        assertEquals(DESCRIPTION, newRecordFolderDialog.getDescription());
        assertEquals(recordIdentifier, newRecordFolderDialog.getIdentifierAndVitalInformation().getIdentifier());
        assertEquals(LOCATION, newRecordFolderDialog.getLocation().getLocationField());
        assertEquals(VitalReviewPeriod.DAY, newRecordFolderDialog.getIdentifierAndVitalInformation().getReviewPeriod());
    }

    @AfterClass
    public void afterClass()
    {
        deleteRMSite();
    }
}
