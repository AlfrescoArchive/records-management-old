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
package org.alfresco.po.share.browse.documentlibrary;

import org.alfresco.po.rm.browse.fileplan.Actions;

/**
 * Actions that may be performed on a document.
 *
 * @author David Webster
 */
public interface DocumentActions extends Actions
{
    /** Lock the document for editing offline. */
    static final String EDIT_OFFLINE = "document-edit-offline";
    /** Cancel edit */
    static final String CANCEL_EDIT = "document-cancel-editing";
    /** Delete the document. */
    static final String DELETE = "document-delete";
    /** Classify the document. */
    static final String CLASSIFY = "rm-classify-content";
    /** Edit Classification */
    static final String EDIT_CLASSIFICATION= "rm-edit-classified-content";
    /** Declare record */
    static final String ACTION_DECLARE_RECORD = "rm-create-record";
    /** Upload new version. */
    static final String UPLOAD_NEW_VERSION = "document-upload-new-version";
}
