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
    /** users */
    public static final String USER1 = "userone";
    
    /** site id's */
    public static final String RM_SITE_ID = "rm";
    public static final String COLLAB_SITE_ID = "my-site" + System.currentTimeMillis();

    /** file plan test data */
    public static final String RECORD_CATEGORY_NAME = "record-category";
    public static final String RECORD_CATEGORY_ONE = "record-category-one";
    public static final String RECORD_CATEGORY_TWO = "record-category-two";
    public static final String SUB_RECORD_CATEGORY_NAME = "sub-record-category";
    public static final String RECORD_FOLDER_ONE = "record-folder-one";
    public static final String RECORD_FOLDER_TWO = "record-folder-two";
    public static final String NON_ELECTRONIC_RECORD = "non-electronic-record";
    public static final String RECORD = "record";
    public static final String FOLDER = "folder";
    public static final String CLASSIFIED_RECORD = "classified-record";
    public static final String CLASSIFIED_NON_ELECTRONIC_RECORD = "classified-non-electronic-record";
    public static final String UNFILED_RECORD_FOLDER = "unfiled-record-folder";
    public static final String HOLD1 = "hold-1";
    public static final String HOLD2 = "hold2";
    public static final String CATEGORY_FOLDER_DISPOSITION = "category-disp-folder";
    public static final String CATEGORY_RECORD_DISPOSITION = "category-disp-record";
    public static final String CLASSIFIED_RECORD_CLASSIFICATION = "TopSecret";
    public static final String CLASSIFIED_RECORD_AUTHORITY = "ClassificationAuthority";
    public static final String CLASSIFIED_RECORD_REASON = "1.4(c)";

    /** record category/folder identifier */
    public static final String RECORD_IDENTIFIER = "id-";
    
    /** collaboration site data */
    public static final String COLLAB_SITE_NAME = "My Site";
    public static final String DOCUMENT = "my-document";
    public static final String IN_PLACE_RECORD = "my-inplace-record";
    public static final String DOCUMENT_LIBRARY = "documentLibrary";
    public static final String CLASSIFIED_DOCUMENT = "my-classified-document";
    public static final String CLASSIFIED_DOCUMENT_CLASSIFICATION = "TopSecret";
    public static final String CLASSIFIED_DOCUMENT_AUTHORITY = "ClassificationAuthority";
    public static final String CLASSIFIED_DOCUMENT_REASON = "1.4(c)";

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



}
