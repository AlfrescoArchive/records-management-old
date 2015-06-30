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

    /** A user who will be assigned the to the RM MANAGER role and then given 'Secret' clearance. */
    public static final String RM_MANAGER = "rm_manager";
    /** The RM_MANAGER has been created. */
    public static final String GROUP_RM_MANAGER_EXISTS = "integration-dataSetup-users-rmManager";
    /** The RM_MANAGER has been given 'Secret' clearance. */
    public static final String GROUP_RM_MANAGER_HAS_SECRET_CLEARANCE = "rmManagerHasSecretClearance";
    /** The RM_MANAGER is a manager of the COLLAB_SITE. */
    public static final String GROUP_RM_MANAGER_IN_COLLAB_SITE = "GROUP_RM_MANAGER_IN_COLLAB_SITE";
    /** The RM_MANAGER can read records in RECORD_CATEGORY_ONE. */
    public static final String GROUP_RM_MANAGER_READ_CATEGORY_ONE = "GROUP_RM_MANAGER_FILE_CATEGORY_ONE";

    /** A user who will have no security clearance. */
    public static final String UNCLEARED_USER = "uncleared_user";
    /** The UNCLEARED_USER has been created. */
    public static final String GROUP_UNCLEARED_USER_EXISTS = "integration-dataSetup-users-unclearedUser";
    /** The UNCLEARED_USER is a manager of the COLLAB_SITE. */
    public static final String GROUP_UNCLEARED_USER_IN_COLLAB_SITE = "GROUP_UNCLEARED_USER_IN_COLLAB_SITE";
    /** The UNCLEARED_USER can file in RECORD_CATEGORY_ONE. */
    public static final String GROUP_UNCLEARED_USER_FILE_CATEGORY_ONE = "GROUP_UNCLEARED_USER_FILE_CATEGORY_ONE";

    /** The default password used when creating test users. */
    public static final String DEFAULT_PASSWORD = "password";


    /* site identifiers */

    /** The id of the RM site. */
    public static final String RM_SITE_ID = "rm";
    /** The RM site has been created. */
    public static final String GROUP_RM_SITE_EXISTS = "GROUP_RM_SITE_EXISTS";

    /** An id for the collaboration site used by the UI integration tests. */
    public static final String COLLAB_SITE_ID = "my-site" + System.currentTimeMillis();
    /** The collaboration site has been created. */
    public static final String GROUP_COLLABORATION_SITE_EXISTS = "GROUP_COLLABORATION_SITE_EXISTS";

    /** An id for the collaboration site used by the sanity tests. */
    public static final String SANITY_COLLAB_SITE_ID = "my-site-sanity" + System.currentTimeMillis();


    /* file plan test data */

    public static final String RECORD_CATEGORY_NAME = "record-category";

    /** An RM category. */
    public static final String RECORD_CATEGORY_ONE = "record-category-one";
    /** CATEGORY_ONE has been created in the root of the fileplan. */
    public static final String GROUP_CATEGORY_ONE_EXISTS = "GROUP_CATEGORY_ONE_EXISTS";

    public static final String RECORD_CATEGORY_TWO = "record-category-two";

    /** An RM category. */
    public static final String SUB_RECORD_CATEGORY_NAME = "sub-record-category";
    /** SUB_RECORD_CATEGORY_NAME has been created inside CATEGORY_ONE. */
    public static final String GROUP_SUB_CATEGORY_EXISTS = "GROUP_SUB_CATEGORY_EXISTS";

    /** A folder in the fileplan. */
    public static final String RECORD_FOLDER_ONE = "record-folder-one";
    /** RECORD_FOLDER_ONE has been created in CATEGORY_ONE. */
    public static final String GROUP_RECORD_FOLDER_ONE_EXISTS = "GROUP_RECORD_FOLDER_ONE_EXISTS";

    /** A folder in the fileplan. */
    public static final String RECORD_FOLDER_TWO = "record-folder-two";
    /** RECORD_FOLDER_TWO has been created in CATEGORY_ONE. */
    public static final String GROUP_RECORD_FOLDER_TWO_EXISTS = "GROUP_RECORD_FOLDER_TWO_EXISTS";

    /** The file plan structure exists. */
    public static final String GROUP_FILE_PLAN_EXISTS = "GROUP_FILE_PLAN_EXISTS";

    /** A non-electronic record. */
    public static final String NON_ELECTRONIC_RECORD = "non-electronic-record";
    /** The NON_ELECTRONIC_RECORD has been created in RECORD_FOLDER_ONE. */
    public static final String GROUP_NON_ELECTRONIC_RECORD_EXISTS = "GROUP_NON_ELECTRONIC_RECORD_EXISTS";

    /** An electronic record. */
    public static final String RECORD = "record";
    /** The RECORD has been created in RECORD_FOLDER_ONE. */
    public static final String GROUP_ELECTRONIC_RECORD_EXISTS = "GROUP_ELECTRONIC_RECORD_EXISTS";

    /** A complete record. */
    public static final String COMPLETE_RECORD = "complete-record";
    /** The COMPLETE_RECORD has been created in RECORD_FOLDER_ONE. */
    public static final String GROUP_COMPLETE_RECORD_EXISTS = "GROUP_COMPLETE_RECORD_EXISTS";
    /** The COMPLETE_RECORD has been classified as secret. */
    public static final String GROUP_COMPLETE_RECORD_IS_CLASSIFIED = "GROUP_COMPLETE_RECORD_IS_CLASSIFIED";

    public static final String FOLDER = "folder";
    public static final String UNFILED_RECORD_FOLDER = "unfiled-record-folder";

    public static final String HOLD1 = "hold-1";
    public static final String HOLD2 = "hold-2";
    /** The holds have been created. */
    public static final String GROUP_HOLDS_EXIST = "GROUP_HOLDS_EXIST";

    public static final String CATEGORY_FOLDER_DISPOSITION = "category-disp-folder";
    public static final String CATEGORY_RECORD_DISPOSITION = "category-disp-record";

    /** record category/folder identifier */
    public static final String RECORD_IDENTIFIER = "id-";


    /* collaboration site data */

    public static final String COLLAB_SITE_NAME = "My Site";

    /** A document within the collaboration site. */
    public static final String DOCUMENT = "my-document";
    /** The DOCUMENT has been created. */
    public static final String GROUP_DOCUMENT_EXISTS = "integration-dataSetup-document";

    /** A document that is declared an in-place record. */
    public static final String IN_PLACE_RECORD = "my-inplace-record";
    /** The IN_PLACE_RECORD has been created. */
    public static final String GROUP_IN_PLACE_RECORD_EXISTS = "integration-dataSetup-inplaceRecord";

    public static final String DOCUMENT_LIBRARY = "documentLibrary";

    /** A document that is shared. */
    public static final String SHARED_DOCUMENT = "shared-document";
    /** The SHARED_DOCUMENT has been created. */
    public static final String GROUP_SHARED_DOCUMENT_EXISTS = "integration-dataSetup-sharedDocument";

    /** A document that is locked for editing. */
    public static final String LOCKED_DOCUMENT = "locked-document";
    /** The LOCKED_DOCUMENT has been created. */
    public static final String GROUP_LOCKED_DOCUMENT_EXISTS = "integration-dataSetup-lockedDocument";

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

    /** A classified document at the level 'Top Secret'. */
    public static final String TOP_SECRET_DOCUMENT = "classified-top-secret-document";
    /** The TOP_SECRET_DOCUMENT has been created. */
    public static final String GROUP_TOP_SECRET_DOCUMENT_EXISTS = "GROUP_TOP_SECRET_DOCUMENT_EXISTS";

    /** A classified document at the level 'Secret'. */
    public static final String SECRET_DOCUMENT = "classified-secret-document";
    /** The SECRET_DOCUMENT has been created. */
    public static final String GROUP_SECRET_DOCUMENT_EXISTS = "GROUP_SECRET_DOCUMENT_EXISTS";

    /** A document explicitly classified at the level 'Unclassified'. */
    public static final String UNCLASSIFIED_DOCUMENT = "unclassified-document";
    /** The UNCLASSIFIED_DOCUMENT has been created. */
    public static final String GROUP_UNCLASSIFIED_DOCUMENT_EXISTS = "GROUP_UNCLASSIFIED_DOCUMENT_EXISTS";

    /** A record classified at the level 'Secret'. */
    public static final String CLASSIFIED_RECORD = "classified-record";
    /** The CLASSIFIED_RECORD has been created. */
    public static final String GROUP_CLASSIFIED_RECORD_EXISTS = "classifiedRecordIsClassified";

    public static final String CLASSIFIED_NON_ELECTRONIC_RECORD = "classified-non-electronic-record";
    public static final String TOP_SECRET_CLASSIFICATION_LEVEL_TEXT = "Top Secret";
    public static final String SECRET_CLASSIFICATION_LEVEL_TEXT = "Secret";
    public static final String UNCLASSIFIED_CLASSIFICATION_LEVEL_TEXT = "Unclassified";
    public static final String DEFAULT_CLASSIFICATION_LEVEL_TEXT = "Top Secret";
    public static final String CLASSIFIED_BY = "ClassifiedByText";
    public static final String CLASSIFICATION_AGENCY = "ClassificationAgency";
    public static final String CLASSIFICATION_REASON = "1.4(c)";

}
