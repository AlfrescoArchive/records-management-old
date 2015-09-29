package org.alfresco.po.rm.actions.viewaudit;

import java.util.List;

import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

import ru.yandex.qatools.htmlelements.element.HtmlElement;

/**
 * Audit Entry block for Audit Log page
 *
 * @author Tatiana Kalinovskaya
 */
public class AuditEntry extends HtmlElement
{
    @FindBy(xpath = "./div[@class='audit-entry-header']/*")
    private List<WebElement> auditEntryHeader;

    @FindBy(xpath = "./div[@class='audit-entry-node']/*")
    private List<WebElement> auditEntryNode;

    public String getAuditEntryTimestamp()
    {
        return auditEntryHeader.get(1).getText();
    }

    public String getAuditEntryUser()
    {
        return auditEntryHeader.get(3).getText();
    }

    public String getAuditEntryEvent()
    {
        return auditEntryHeader.get(5).getText();
    }

    public String getAuditEntryIdentifier()
    {
        return auditEntryNode.get(1).getText();
    }

    public String getAuditEntryType()
    {
        return auditEntryNode.get(3).getText();
    }

    public String getAuditEntryLocation()
    {
        return auditEntryNode.get(5).getText();
    }
}
