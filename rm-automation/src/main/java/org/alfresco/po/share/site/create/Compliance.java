package org.alfresco.po.share.site.create;

/**
 * Site compliance enumeration
 * 
 * @author Tatiana Kalinovskaya
 */
public enum Compliance
{
    STANDARD    ("{http://www.alfresco.org/model/recordsmanagement/1.0}rmsite"), 
    DOD50152STD ("http://www.alfresco.org/model/dod5015/1.0}site");

    private String value;

    Compliance(String value)
    {
        this.value = value;
    }

    @Override
    public String toString()
    {
        return this.value;
    }

    public static final Compliance fromValue(String value)
    {
        if (value.equals("{http://www.alfresco.org/model/recordsmanagement/1.0}rmsite")) { return STANDARD; }
        if (value.equals("http://www.alfresco.org/model/dod5015/1.0}site")) { return DOD50152STD; }
        throw new RuntimeException("Can't get SiteType from value " + value);
    }
}