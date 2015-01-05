package org.alfresco.po.rm.disposition;

/**
 * Disposition level enumeration
 *
 * @author Tatiana Kalinovskaya
 */
public enum DispositionLevel
{
    RECORD("Record"),
    RECORD_FOLDER("Record Folder");

    private String value;

    DispositionLevel(String value)
    {
        this.value = value;
    }

    public static final DispositionLevel fromValue(String value)
    {
        switch (value)
        {
            case "Record":
                return RECORD;
            case "Record Folder":
                return RECORD_FOLDER;
            default:
                return RECORD_FOLDER;
        }
    }

    @Override
    public String toString()
    {
        return this.value;
    }
}
