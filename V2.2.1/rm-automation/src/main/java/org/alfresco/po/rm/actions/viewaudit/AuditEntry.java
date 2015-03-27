package org.alfresco.po.rm.actions.viewaudit;

import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;

/**
 * Audit Entry block for Audit Log page
 *
 * @author Tatiana Kalinovskaya
 */
public class AuditEntry 
{
    private WebElement auditEntry;

    /*package*/ AuditEntry(WebElement auditEntry)
    {
        this.auditEntry = auditEntry;
    }
    
    private String getHeaderValue(int index)
    {
        List<WebElement> webElements = auditEntry.findElements(By.cssSelector("div.audit-entry-header span.value"));
        return webElements.get(index).getText();
    }
    
    private String getEntryValue(int index)
    {
        List<WebElement> webElements = auditEntry.findElements(By.cssSelector("div.audit-entry-node span.value"));
        return webElements.get(index).getText();
    }
    
    public String getAuditEntryTimestamp()
    {
        return getHeaderValue(0);
    }

    public String getAuditEntryUser()
    {
        return getHeaderValue(1);
    }

    public String getAuditEntryEvent()
    {
        return getHeaderValue(2);
    }

    public String getAuditEntryIdentifier()
    {
        return getEntryValue(0);
    }

    public String getAuditEntryType()
    {
        return getEntryValue(1);
    }

    public String getAuditEntryLocation()
    {
        return getEntryValue(2);
    }
}
