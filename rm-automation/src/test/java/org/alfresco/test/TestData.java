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
package org.alfresco.test;

/**
 * Test data used in tests
 *
 * @author Roy Wetherall
 */
public interface TestData
{
    /* Users */

    /** A user used by the sanity tests. */
    public static final String USER1 = "userone";

    /**
     * A user who will be assigned the to the RM MANAGER role and then given 'Secret' clearance.
     * <p>"GROUP_RM_MANAGER_EXISTS" The RM_MANAGER has been created.
     * <p>"GROUP_RM_MANAGER_HAS_SECRET_CLEARANCE" The RM_MANAGER has been given 'Secret' clearance.
     * <p>"GROUP_RM_MANAGER_IN_COLLAB_SITE" The RM_MANAGER is a manager of the COLLAB_SITE.
     * <p>"GROUP_RM_MANAGER_READ_CATEGORY_ONE" The RM_MANAGER can read records in RECORD_CATEGORY_ONE.
     */
    public static final String RM_MANAGER = "rm_manager_user";

    /**
     * A user who will have no security clearance.
     * <p>"GROUP_UNCLEARED_USER_EXISTS" The UNCLEARED_USER has been created.
     * <p>"GROUP_UNCLEARED_USER_IN_COLLAB_SITE" The UNCLEARED_USER is a manager of the COLLAB_SITE.
     * <p>"GROUP_UNCLEARED_USER_FILE_CATEGORY_ONE" The UNCLEARED_USER can file in RECORD_CATEGORY_ONE.
     */
    public static final String UNCLEARED_USER = "uncleared_user";

    /** The default password used when creating test users. */
    public static final String DEFAULT_PASSWORD = "password";
    public static final String DEFAULT_EMAIL = "default@alfresco.com";


    /* site identifiers */

    /**
     * The id of the RM site.
     * <p>"GROUP_RM_SITE_EXISTS" The RM site has been created.
     */
    public static final String RM_SITE_ID = "rm";

    /**
     * An id for the collaboration site used by the UI integration tests.
     * <p>"GROUP_COLLABORATION_SITE_EXISTS" The collaboration site has been created.
     */
    public static final String COLLAB_SITE_ID = "my-site" + System.currentTimeMillis();

    /** An id for the collaboration site used by the sanity tests. */
    public static final String SANITY_COLLAB_SITE_ID = "my-site-sanity" + System.currentTimeMillis();


    /* file plan test data */

    public static final String RECORD_CATEGORY_NAME = "record-category";

    /**
     * An RM category.
     * <p>"GROUP_CATEGORY_ONE_EXISTS" CATEGORY_ONE has been created in the root of the fileplan.
     */
    public static final String RECORD_CATEGORY_ONE = "record-category-one";

    public static final String RECORD_CATEGORY_TWO = "record-category-two";

    /**
     * An RM category.
     * <p>"GROUP_SUB_CATEGORY_EXISTS" SUB_RECORD_CATEGORY_NAME has been created inside CATEGORY_ONE.
     */
    public static final String SUB_RECORD_CATEGORY_NAME = "sub-record-category";

    /**
     * A folder in the fileplan.
     * <p>"GROUP_RECORD_FOLDER_ONE_EXISTS" RECORD_FOLDER_ONE has been created in CATEGORY_ONE.
     */
    public static final String RECORD_FOLDER_ONE = "record-folder-one";

    /**
     * A folder in the fileplan.
     * <p>"GROUP_RECORD_FOLDER_TWO_EXISTS" RECORD_FOLDER_TWO has been created in CATEGORY_ONE.
     */
    public static final String RECORD_FOLDER_TWO = "record-folder-two";

    /**
     * A folder in fileplan to use for search.
     * <p>"GROUP_RECORD_FOLDER_SEARCH_EXISTS" RECORD_FOLDER_SEARCH has been created in CATEGORY_ONE.
     * <p>"GROUP_SEARCH_RECORDS_EXIST" SEARCH_RECORDS have been created and classified in RECORD_FOLDER_SEARCH
     */
     public static final String RECORD_FOLDER_SEARCH = "record-folder-search";

    /* "GROUP_FILE_PLAN_EXISTS" The file plan structure exists. */

    /**
     * A non-electronic record.
     * <p>"GROUP_NON_ELECTRONIC_RECORD_EXISTS" The NON_ELECTRONIC_RECORD has been created in RECORD_FOLDER_ONE.
     */
    public static final String NON_ELECTRONIC_RECORD = "non-electronic-record";

    /**
     * An electronic record.
     * <p>"GROUP_ELECTRONIC_RECORD_EXISTS" The RECORD has been created in RECORD_FOLDER_ONE.
     */
    public static final String RECORD = "record";

    /**
     * A complete record.
     * <p>"GROUP_COMPLETE_RECORD_EXISTS" The COMPLETE_RECORD has been created in RECORD_FOLDER_ONE.
     * <p>"GROUP_COMPLETE_RECORD_IS_CLASSIFIED" The COMPLETE_RECORD has been classified as secret.
     */
    public static final String COMPLETE_RECORD = "complete-record";

    public static final String FOLDER = "folder";
    public static final String UNFILED_RECORD_FOLDER = "unfiled-record-folder";

    /* "GROUP_HOLDS_EXIST" The holds have been created. */
    public static final String HOLD1 = "hold-1";
    public static final String HOLD2 = "hold-2";

    public static final String CATEGORY_FOLDER_DISPOSITION = "category-disp-folder";
    public static final String CATEGORY_RECORD_DISPOSITION = "category-disp-record";

    /** record category/folder identifier */
    public static final String RECORD_IDENTIFIER = "id-";


    /* collaboration site data */

    public static final String COLLAB_SITE_NAME = "My Site";

    /**
     * A document within the collaboration site.
     * <p>"GROUP_DOCUMENT_EXISTS" The DOCUMENT has been created.
     */
    public static final String DOCUMENT = "my-document";

    /**
     * A document that is declared an in-place record.
     * <p>"GROUP_IN_PLACE_RECORD_EXISTS" The IN_PLACE_RECORD has been created.
     */
    public static final String IN_PLACE_RECORD = "my-inplace-record";

    public static final String DOCUMENT_LIBRARY = "documentLibrary";

    /**
     * A document that is shared.
     * <p>"GROUP_SHARED_DOCUMENT_EXISTS" The SHARED_DOCUMENT has been created.
     */
    public static final String SHARED_DOCUMENT = "shared-document";

    /**
     * A document that is locked for editing.
     * <p>"GROUP_LOCKED_DOCUMENT_EXISTS" The LOCKED_DOCUMENT has been created.
     */
    public static final String LOCKED_DOCUMENT = "locked-document";

    /** standard property values */
    public static final String TITLE = "Title";
    public static final String DESCRIPTION = "Description";
    public static final String LOCATION = "Location";
    public static final String REASON = "Reason";
    public static final String PHYSICALSIZE = "100";
    public static final String NUMBEROFCOPIES = "2";
    public static final String STORAGELOCATION = "storage";
    public static final String SHELF = "shelf";
    public static final String BOX = "box";
    public static final String FILE = "file";

    /** modified mark */
    public static final String MODIFIED = "_modified";

    /** disposition information */
    public static final String DISPOSITION_AUTHORITY = "Disposition Authority";
    public static final String DISPOSITION_INSTRUCTIONS = "Disposition Instructions";
    public static final String CUTOFF_LABEL = "Cut off";
    public static final String RETAIN_LABEL = "Retain";
    public static final String TRANSFER_LABEL = "Transfer";
    public static final String ACCESSION_LABEL = "Accession";
    public static final String DESTROY_LABEL = "Destroy";

    /** disposition action position on Add step menu of Edit Disposition Schedule page */
    public static final int ACCESSION_INDEX = 0;
    public static final int DESTROY_INDEX = 1;
    public static final int RETAIN_INDEX = 2;
    public static final int TRANSFER_INDEX = 3;
    public static final int CUTOFF_INDEX = 4;

    /** disposition events */
    public static final String ABOLISHED = "Abolished";
    public static final String SEPARATION = "Separation";
    public static final String CASE_COMPLETE = "Case Complete";
    public static final String STUDY_COMPLETE = "Study Complete";
    public static final String TRAINING_COMPLETE = "Training Complete";
    public static final String OBSOLETE = "Obsolete";
    public static final String SUPERSEDED = "Superseded";
    public static final String NO_LONGER_NEEDED = "No longer needed";
    public static final String CASE_CLOSED = "Case Closed";


    /* Classification data. */

    /**
     * A classified document at the level 'Top Secret'.
     * <p>"GROUP_TOP_SECRET_DOCUMENT_EXISTS" The TOP_SECRET_DOCUMENT has been created.
     */
    public static final String TOP_SECRET_DOCUMENT = "classified-top-secret-document";

    /**
     * A classified document at the level 'Secret'.
     * <p>"GROUP_SECRET_DOCUMENT_EXISTS" The SECRET_DOCUMENT has been created.
     */
    public static final String SECRET_DOCUMENT = "classified-secret-document";

    /**
     * A document explicitly classified at the level 'Unclassified'.
     * <p>"GROUP_UNCLASSIFIED_DOCUMENT_EXISTS" The UNCLASSIFIED_DOCUMENT has been created.
     */
    public static final String UNCLASSIFIED_DOCUMENT = "unclassified-document";

    /**
     * A record classified at the level 'Secret'.
     * <p>"GROUP_CLASSIFIED_RECORD_EXISTS" The CLASSIFIED_RECORD has been created.
     */
    public static final String CLASSIFIED_RECORD = "classified-record";

    public static final String RECORD_SEARCH_SUFFIX = "_RECORD_SEARCH";
    public static final String TOP_SECRET_RECORD_SEARCH = "search-top-secret" + RECORD_SEARCH_SUFFIX;
    public static final String CONFIDENTIAL_RECORD_SEARCH = "search-confidential" + RECORD_SEARCH_SUFFIX;
    public static final String UNCLASSIFIED_RECORD_SEARCH = "search-unclassified" + RECORD_SEARCH_SUFFIX;

    public static final String CLASSIFIED_NON_ELECTRONIC_RECORD = "classified-non-electronic-record";
    public static final String TOP_SECRET_CLASSIFICATION_LEVEL_TEXT = "Top Secret";
    public static final String SECRET_CLASSIFICATION_LEVEL_TEXT = "Secret";
    public static final String CONFIDENTIAL_CLASSIFICATION_LEVEL_TEXT = "Confidential";
    public static final String UNCLASSIFIED_CLASSIFICATION_LEVEL_TEXT = "Unclassified";
    public static final String DEFAULT_CLASSIFICATION_LEVEL_TEXT = "Top Secret";
    public static final String CLASSIFIED_BY = "ClassifiedByText";
    public static final String CLASSIFICATION_AGENCY = "ClassificationAgency";
    public static final String CLASSIFICATION_REASON = "1.4(c)";
  
    /**
     * A record has been classified and its initial edit classification state has been checked.
     * <p>"EDIT_CLASSIFICATION_INITIAL_STATE" The classified record initial edit classification state has been verified.
     */
    public static final String EDIT_CLASSIFICATION_RECORD = "edit-classification-record";
    public static final String EDIT_CLASSIFICATION_DOCUMENT = "edit-classification-document";
    // Field values to use for the editing of the classification
    public static final String EDITED_CLASSIFICATION_REASON = "1.4(a)";
    public static final String EDITED_CLASSIFIED_BY = "Edited Classified By Text";
    public static final String EDITED_CLASSIFICATION_AGENCY = "Edited Classification Agency";
    // Information message that appears when successfully editing a classification meta-data
    public static final String SUCCESSFULLY_EDITED_CLASSIFICATION = "Successfully edited classified content";
    
    /** The downgrade date to use. Here we set the date and month to the same thing to avoid cross-locale issues. */
    public static final String DOWNGRADE_DATE_EDIT = "11/11/2045";
    public static final String DOWNGRADE_DATE_INPUT = "07/07/2045";
    public static final String DOWNGRADE_DATE_OUTPUT = "Fri 7 Jul 2045";
    public static final String DOWNGRADE_EVENT = "Downgrade Event";
    public static final String DOWNGRADE_INSTRUCTIONS = "Downgrade Instructions";
    /** The declassification date to use. Here we set the date and month to the same thing to avoid cross-locale issues. */
    public static final String DECLASSIFICATION_DATE_EDIT = "12/12/2045";
    public static final String DECLASSIFICATION_DATE_INPUT = "08/08/2045";
    public static final String DECLASSIFICATION_DATE_OUTPUT = "Tue 8 Aug 2045";
    public static final String DECLASSIFICATION_EVENT = "Declassification Event";
    public static final String EXEMPTION_CATEGORY = "9";
    
    public static final String VALIDATION_BLANK_FIELD = "Must not be blank and cannot start with a whitespace";

    public static final String FIRST_NAME = "firstName";
    public static final String LAST_NAME = "lastName";

    /** Test Content */
    public static final String TEST_CONTENT = "This is some test content";

}
