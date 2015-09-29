package org.alfresco.po.rm.console.audit;

/**
 * Audit Events enumeration
 *
 * @author Tatiana Kalinovskaya
 */
public enum AuditEvents
{
    ADD_TO_HOLD("Add to Hold"),
    ALL("All"),
    AUDIT_VIEW("Audit View"),
    COPY_TO("Copy to"),
    COMPLETE_RECORD("Complete record"),
    CREATED_OBJECT("Created Object"),
    FILE_TO("File to"),
    MOVE_TO("Move to"),
    REMOVE_FROM_HOLD("Remove from Hold"),
    UPDATED_METADATA("Updated Metadata");

    private String value;

    AuditEvents(String value)
    {
        this.value = value;
    }

    public static final AuditEvents fromValue(String value)
    {
        switch (value)
        {
            case "Audit View":
                return AUDIT_VIEW;
            case "Created Object":
                return CREATED_OBJECT;
            case "File to":
                return FILE_TO;
            case "Move to":
                return MOVE_TO;
            case "Updated Metadata":
                return UPDATED_METADATA;
            default:
                return ALL;
        }
    }
    @Override
    public String toString()
    {
        return this.value;
    }
}
