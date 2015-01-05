package org.alfresco.po.rm.dialog;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.alfresco.po.rm.browse.fileplan.FilePlan;
import org.alfresco.po.rm.dialog.create.NewRecordCategoryDialog;
import org.alfresco.test.BaseRmUnitTest;
import org.springframework.beans.factory.annotation.Autowired;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

/**
 * New Record Category Dialog Unit Test
 *
 * @author Tatiana Kalinovskaya
 */
@Test (groups = {"unit-test"})
public class NewRecordCategoryDialogUnitTest extends BaseRmUnitTest
{
    @Autowired
    private NewRecordCategoryDialog newRecordCategoryDialog;

    /** file plan */
    @Autowired
    private FilePlan filePlan;

    @BeforeMethod
    public void beforeMethod()
    {
        // create RM site
        createRMSite();
        
        // open record category dialog
        openPage(filePlan, RM_SITE_ID, "documentlibrary")
            .getToolbar()
            .clickOnNewCategory();
    }

    @Test
    public void verifyDialogDefaultValues()
    {
        assertTrue(newRecordCategoryDialog.getName().isEmpty());
        assertTrue(newRecordCategoryDialog.getTitle().isEmpty());
        assertTrue(newRecordCategoryDialog.getDescription().isEmpty());
        assertFalse(newRecordCategoryDialog.getIdentifierAndVitalInformation().getIdentifier().isEmpty());
        assertFalse(newRecordCategoryDialog.getIdentifierAndVitalInformation().isVitalIndicatorSelected());
        assertEquals(VitalReviewPeriod.NONE, 
                     newRecordCategoryDialog.getIdentifierAndVitalInformation().getReviewPeriod());
        assertTrue(newRecordCategoryDialog.getIdentifierAndVitalInformation().getPeriodExpression().isEmpty());
    }

    @Test(dependsOnMethods="verifyDialogDefaultValues")
    public void verifySettingValues()
    {
        int periodExpression = 2;
        // enter details of new record category
        String recordIdentifier = RECORD_IDENTIFIER +  + System.currentTimeMillis();
        newRecordCategoryDialog.setName(RECORD_CATEGORY_NAME).setTitle(TITLE).setDescription(DESCRIPTION);
        newRecordCategoryDialog.getIdentifierAndVitalInformation().setIdentifier(recordIdentifier)
                .checkVitalIndicator(true).setReviewPeriod(VitalReviewPeriod.DAY)
                .setPeriodExpression(String.valueOf(periodExpression));
        // verify that entered details are correct
        assertEquals(RECORD_CATEGORY_NAME, newRecordCategoryDialog.getName());
        assertEquals(TITLE, newRecordCategoryDialog.getTitle());
        assertEquals(DESCRIPTION, newRecordCategoryDialog.getDescription());
        assertEquals(recordIdentifier, newRecordCategoryDialog.getIdentifierAndVitalInformation().getIdentifier());
        assertEquals(VitalReviewPeriod.DAY, newRecordCategoryDialog.getIdentifierAndVitalInformation()
                .getReviewPeriod());

    }

    @AfterClass
    public void afterClass()
    {
        deleteRMSite();
    }
}
