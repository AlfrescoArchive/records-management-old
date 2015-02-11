package org.alfresco.po.rm.console.audit;

/**
 * Audit Entry Types enumeration
 *
 * @author Tatiana Kalinovskaya
 */
public enum AuditEntryTypes
{
    CONTENT("Content"),
    DISPOSITION_SCHEDULE("Disposition Schedule"),
    DISPOSITION_ACTION_DEFINITION("Disposition Action Definition"),
    FILE_PLAN("File Plan"),
    HOLD("Hold"),
    HOLD_CONTAINER("Hold Container"),
    NON_ELECTRONIC_DOCUMENT("Non-Electronic Document"),
    PERSON("Person"),
    RECORD_CATEGORY("Record Category"),
    RECORD_FOLDER("Record Folder"),
    THUMBNAIL("Thumbnail"),
    TRANSFER_CONTAINER("Transfer Container"),
    UNFILED_RECORD_CONTAINER("Unfiled Record Container");


    private String value;

    AuditEntryTypes(String value)
    {
        this.value = value;
    }

    public static final AuditEntryTypes fromValue(String value)
    {
        switch (value)
        {
            case "Content":
                return CONTENT;
            case "Disposition Schedule":
                return DISPOSITION_SCHEDULE;
            case "Disposition Action Definition":
                return DISPOSITION_ACTION_DEFINITION;
            case "File Plan":
                return FILE_PLAN;
            case "Hold":
                return HOLD;
            case "Hold Container":
                return HOLD_CONTAINER;
            case "Non-Electronic Document":
                return NON_ELECTRONIC_DOCUMENT;
            case "Person":
                return PERSON;
            case "Record Category":
                return RECORD_CATEGORY;
            case "Record Folder":
                return RECORD_FOLDER;
            case "Thumbnail":
                return THUMBNAIL;
            case "Transfer Container":
                return TRANSFER_CONTAINER;
            case "Unfiled Record Container":
                return UNFILED_RECORD_CONTAINER;
            default:
                return null;
        }
    }
    @Override
    public String toString()
    {
        return this.value;
    }
}
