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
package org.alfresco.po.rm.browse.fileplan;

/**
 * @author Roy Wetherall
 */
public interface Actions
{
    /** actions */
    static final String DOWNLOAD = "rm-document-download";
    static final String EDIT_METADATA = "rm-edit-details";
    static final String COMPLETE_RECORD = "rm-declare";
    static final String REOPEN_RECORD = "rm-undeclare";
    static final String ADD_TO_HOLD = "rm-add-to-hold";
    static final String COPY_RECORD = "rm-copy-record-to";
    static final String MOVE_RECORD = "rm-move-record-to";
    static final String LINK = "rm-link-to";
    static final String DELETE = "rm-delete";
    static final String VIEW_AUDIT = "rm-view-audit-log";
    static final String REQUEST_INFORMATION = "rm-request-info";
    static final String MANAGE_PERMISSIONS = "rm-manage-permissions";
    static final String REMOVE_FROM_HOLD = "rm-remove-from-hold";
    static final String FOLDER_VIEW_DETAILS = "rm-record-folder-view-details";
    static final String CLOSE_FOLDER = "rm-close-folder";
    static final String REOPEN_FOLDER = "rm-open-folder";
    static final String COPY_FOLDER = "rm-copy-recordfolder-to";
    static final String MOVE_FOLDER = "rm-move-recordfolder-to";
    static final String CATEGORY_VIEW_DETAILS = "rm-record-category-view-details";
    static final String COPY_CATEGORY = "rm-copy-recordcategory-to";
    static final String MOVE_CATEGORY = "rm-move-recordcategory-to";
    static final String EDIT_DISPOSITION_DATE = "rm-edit-disposition-as-of-date";
    static final String CUTOFF = "rm-cutoff";
    static final String UNDO_CUTOFF = "rm-undo-cutoff";
    static final String TRANSFER = "rm-transfer";
    static final String END_RETENTION = "rm-end-retention";
    static final String ACCESSION = "rm-accession";
    static final String DESTROY = "rm-destroy";
    static final String GENERATE_DESTRUCTION_REPORT = "rm-file-destruction-report";
    static final String MANAGE_RULES = "rm-manage-rules-filed";
    static final String ADD_RELATIONSHIP = "rm-add-relationship";

    /** transfer actions */
    static final String COMPLETE_TRANSFER ="rm-transfer-complete";
    static final String COMPLETE_ACCESSION ="rm-accession-complete";

    /** incomplete record actions with download */
    static final String[] INCOMPLETE_RECORD_ACTIONS_WITH_DOWNLOAD = new String[]
    {
        DOWNLOAD,
        EDIT_METADATA, 
        COMPLETE_RECORD,
        ADD_TO_HOLD, 
        COPY_RECORD,
        MOVE_RECORD,
        LINK, 
        DELETE, 
        VIEW_AUDIT,
        REQUEST_INFORMATION,
        MANAGE_PERMISSIONS,
        ADD_RELATIONSHIP
    };
    
    /** incomplete record actions */
    static final String[] INCOMPLETE_RECORD_ACTIONS = new String[]
    {
        EDIT_METADATA, 
        COMPLETE_RECORD,
        ADD_TO_HOLD, 
        COPY_RECORD,
        MOVE_RECORD,
        LINK, 
        DELETE, 
        VIEW_AUDIT,
        REQUEST_INFORMATION,
        MANAGE_PERMISSIONS,
        ADD_RELATIONSHIP
    };

}
