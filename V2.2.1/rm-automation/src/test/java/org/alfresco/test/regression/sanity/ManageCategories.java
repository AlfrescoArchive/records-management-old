package org.alfresco.test.regression.sanity;

import static org.alfresco.po.rm.console.audit.AuditEntryTypes.RECORD_CATEGORY;
import static org.alfresco.po.rm.console.audit.AuditEvents.*;
import static org.junit.Assert.*;

import org.alfresco.po.rm.actions.edit.EditRecordCategoryPage;
import org.alfresco.po.rm.actions.viewaudit.AuditEntry;
import org.alfresco.po.rm.actions.viewaudit.AuditLogPage;
import org.alfresco.po.rm.browse.fileplan.FilePlan;
import org.alfresco.po.rm.browse.fileplan.RecordCategory;
import org.alfresco.po.rm.details.category.CategoryActionsPanel;
import org.alfresco.po.rm.details.category.CategoryDetailsPage;
import org.alfresco.po.rm.details.record.RecordActionsPanel;
import org.alfresco.po.rm.dialog.VitalReviewPeriod;
import org.alfresco.test.BaseTest;
import org.springframework.beans.factory.annotation.Autowired;
import org.testng.annotations.Test;

/**
 * Manage incomplete records regression test
 *
 * @author Tatiana Kalinovskaya
 */
public class ManageCategories extends BaseTest
{
    /**
     * file plan
     */
    @Autowired
    private FilePlan filePlan;

    /**
     * category details
     */
    @Autowired
    private CategoryDetailsPage categoryDetailsPage;

    /**
     * edit category page
     */
    @Autowired
    private EditRecordCategoryPage editRecordCategoryPage;

    /**
     * audit log page
     */
    @Autowired
    private AuditLogPage auditLogPage;

    /**
     * Main regression test execution
     */
    @Test
            (
                    groups = { "RMA-2669", "sanity" },
                    description = "Manage Categories",
                    dependsOnGroups = { "RMA-2668" }
            )
    public void manageCategories()
    {
        // open category
        openPage(filePlan, RM_SITE_ID, "documentlibrary")
                .navigateTo(RECORD_CATEGORY_ONE);

        assertEquals("Sub-category should be present in record category", 1, filePlan.getList().size());

        // verify sub-category actions
        assertNull(filePlan.getRecordCategory(SUB_RECORD_CATEGORY_NAME).isActionsClickable(
                RecordCategory.VIEW_DETAILS,
                RecordCategory.EDIT_METADATA,
                RecordCategory.COPY,
                RecordCategory.MOVE,
                RecordCategory.DELETE,
                RecordCategory.VIEW_AUDIT));

        // navigate to the sub-category details page
        filePlan.getRecordCategory(SUB_RECORD_CATEGORY_NAME).clickOnViewDetails();

        // verify that all the expected actions are available
        assertTrue(categoryDetailsPage.getCategoryActionsPanel().isActionsClickable(
                CategoryActionsPanel.EDIT_METADATA,
                CategoryActionsPanel.MANAGE_PERMISSIONS,
                CategoryActionsPanel.COPY,
                CategoryActionsPanel.MOVE,
                CategoryActionsPanel.DELETE,
                CategoryActionsPanel.VIEW_AUDIT));

        //edit metadata
        categoryDetailsPage.getCategoryActionsPanel().clickOnAction(RecordActionsPanel.EDIT_METADATA, editRecordCategoryPage);
        String SubCategoryName = SUB_RECORD_CATEGORY_NAME + MODIFIED;
        editRecordCategoryPage
                .getContent().setNameValue(SUB_RECORD_CATEGORY_NAME + MODIFIED)
                .setTitle(TITLE + MODIFIED)
                .setDescription(DESCRIPTION + MODIFIED);
        editRecordCategoryPage.getIdentifierAndVitalInformation()
                .checkVitalIndicator(true)
                .setReviewPeriod(VitalReviewPeriod.WEEK)
                .setPeriodExpression("3");
        editRecordCategoryPage.clickOnSave();
        // TODO verify entered values are displayed on details page

        //TODO Copy the Sub-Category to Folder2

        //navigate to root of File Plan/ click on File Plan on breadcrumb
        categoryDetailsPage.navigateUp(3);

        // create new category2
        filePlan
                .getToolbar()
                .clickOnNewCategory()
                .setName(RECORD_CATEGORY_TWO)
                .setTitle(TITLE)
                .clickOnSave();

        //navigate inside category-one
        filePlan.getRecordCategory(RECORD_CATEGORY_ONE).clickOnLink();

        //copy sub-category to category-two
        filePlan
                .getRecordCategory(SubCategoryName)
                .clickOnCopyTo()
                .select(RECORD_CATEGORY_TWO)
                .clickOnCopy();
        assertEquals("Sub-category should be present in category after copy", 1, filePlan.getList().size());

        //delete sub-category from category
        filePlan
                .getRecordCategory(SubCategoryName)
                .clickOnDelete()
                .clickOnConfirm();
        assertNull(filePlan.getRecordCategory(SubCategoryName));
        assertEquals("No items should be in category after move", 0, filePlan.getList().size());

        //navigate inside Category two
        filePlan
                .navigateUp()
                .getRecordCategory(RECORD_CATEGORY_TWO)
                .clickOnLink();

        //move sub-category to category
        filePlan
                .getRecordCategory(SubCategoryName)
                .clickOnMoveTo()
                .select(RECORD_CATEGORY_ONE)
                .clickOnMove();
        assertEquals("No items should be in category two after move", 0, filePlan.getList().size());

        //navigate inside category
        filePlan
                .navigateUp()
                .getRecordCategory(RECORD_CATEGORY_ONE)
                .clickOnLink();

        //TODO Manage Permissions

        //view audit log
        auditLogPage = filePlan
                .getRecordCategory(SubCategoryName)
                .clickOnViewAuditLog();
        //verify audit log page
         verifyAuditLog(SubCategoryName);
        auditLogPage.close();

        //TODO Manage Rules
    }

    /**
     * Verify the contents of the audit log
     */
    public void verifyAuditLog(String SubCategoryName)
    {
        // Verify header information
        assertEquals("Audit log for " + SubCategoryName, auditLogPage.getAuditPageHeader());
        // verify Export and File As Record buttons are displayed and enabled
        assertTrue(auditLogPage.isExportButtonDisplayed());
        assertTrue(auditLogPage.isExportButtonEnabled());
        assertTrue(auditLogPage.isFileAsRecordButtonDisplayed());
        assertTrue(auditLogPage.isFileAsRecordButtonEnabled());

        //TODO specify all values as static elements
        // Verify 4 entries are displayed
        assertEquals(4, auditLogPage.getAuditEntryCount());

        // Verify the first 3 audit entries: when the folder was copied
        AuditEntry auditEntry;
        for (int i = 0; i < 3; i++)
        {
            auditEntry = auditLogPage.getAuditEntry(i);
            assertNotNull(auditEntry.getAuditEntryTimestamp());
            //TODO verify the timestamp equals to cm:created
            assertEquals(auditEntry.getAuditEntryUser(), "Administrator");
            //TODO verify the currently logged in user is displayed
            assertTrue(auditEntry.getAuditEntryEvent().equals(UPDATED_METADATA.toString())
                    || auditEntry.getAuditEntryEvent().equals(CREATED_OBJECT.toString())
                    || auditEntry.getAuditEntryEvent().equals(COPY_TO.toString()));
            // assertEquals(identifier, auditEntry.getAuditEntryIdentifier());
            assertEquals(RECORD_CATEGORY.toString(), auditEntry.getAuditEntryType());
            assertEquals("/" + DOCUMENT_LIBRARY + "/"
                            + RECORD_CATEGORY_TWO + "/"
                            + SubCategoryName,
                    auditEntry.getAuditEntryLocation());
        }

        // Verify next entry: folder was moved
        auditEntry = auditLogPage.getAuditEntry(3);
        assertNotNull(auditEntry.getAuditEntryTimestamp());
        //TODO verify the timestamp equals to time when folder was renamed
        assertEquals(auditEntry.getAuditEntryUser(), "Administrator");
        //TODO verify the currently logged in user is displayed
        assertTrue(auditEntry.getAuditEntryEvent().equals(UPDATED_METADATA.toString())
                || auditEntry.getAuditEntryEvent().equals(MOVE_TO.toString()));
        //  assertEquals(identifier, auditEntry.getAuditEntryIdentifier());
        assertEquals(RECORD_CATEGORY.toString(), auditEntry.getAuditEntryType());
        assertEquals("/" + DOCUMENT_LIBRARY + "/"
                        + RECORD_CATEGORY_ONE + "/"
                        + SubCategoryName,
                auditEntry.getAuditEntryLocation());

    }
}
