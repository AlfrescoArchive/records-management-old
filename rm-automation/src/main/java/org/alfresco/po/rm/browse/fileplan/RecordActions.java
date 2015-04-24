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
 * Record Actions
 * @author Roy Wetherall, Tatiana Kalinovskaya
 */
public interface RecordActions extends Actions
{
    /** actions */
    static final String DOWNLOAD = "rm-document-download";
    static final String COMPLETE_RECORD = "rm-declare";
    static final String REOPEN_RECORD = "rm-undeclare";
    static final String COPY = "rm-copy-record-to";
    static final String MOVE = "rm-move-record-to";
    static final String LINK = "rm-link-to";
    static final String REQUEST_INFORMATION = "rm-request-info";
    static final String ADD_RELATIONSHIP = "rm-add-relationship";
    static final String CLASSIFY = "rm-classify-content";

    /** incomplete record actions with download */
    static final String[] INCOMPLETE_RECORD_ACTIONS_WITH_DOWNLOAD = new String[]
    {
        DOWNLOAD,
        EDIT_METADATA,
        COMPLETE_RECORD,
        HoldActions.ADD_TO_HOLD,
        COPY,
        MOVE,
        LINK,
        DELETE,
        VIEW_AUDIT,
        REQUEST_INFORMATION,
        MANAGE_PERMISSIONS,
        ADD_RELATIONSHIP,
        CLASSIFY
    };

    /** incomplete record actions */
    static final String[] INCOMPLETE_RECORD_ACTIONS = new String[]
    {
        EDIT_METADATA,
        COMPLETE_RECORD,
        HoldActions.ADD_TO_HOLD,
        COPY,
        MOVE,
        LINK,
        DELETE,
        VIEW_AUDIT,
        REQUEST_INFORMATION,
        MANAGE_PERMISSIONS,
        ADD_RELATIONSHIP,
        CLASSIFY
    };

}
