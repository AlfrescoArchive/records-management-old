package org.alfresco.po.rm.browse.fileplan;

/**
 * Folder Actions
 * @author Tatiana Kalinovskaya
 */
public interface FolderActions extends Actions, HoldActions
{
    static final String VIEW_DETAILS = "rm-record-folder-view-details";
    static final String CLOSE_FOLDER = "rm-close-folder";
    static final String REOPEN_FOLDER = "rm-open-folder";
    static final String COPY = "rm-copy-recordfolder-to";
    static final String MOVE = "rm-move-recordfolder-to";
    static final String MANAGE_RULES = "rm-manage-rules-filed";
}
