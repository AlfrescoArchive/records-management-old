package org.alfresco.po.rm.browse.fileplan;

import org.alfresco.po.common.ConfirmationPrompt;
import org.alfresco.po.common.renderable.Renderable;
import org.alfresco.po.rm.actions.viewaudit.AuditLogPage;
import org.alfresco.po.share.browse.ListItem;
import org.openqa.selenium.support.FindBy;
import org.springframework.beans.factory.annotation.Autowired;

import ru.yandex.qatools.htmlelements.element.Link;

/**
 * File Plan list item (common for record category, record folder, record)
 *
 * @author Tatiana Kalinovskaya
 */
public abstract class FilePlanListItem extends ListItem  implements Actions
{

    /** link selector */
    @FindBy (xpath = ".//td[contains(@class,'fileName')]/div/div[1]")
    private Link identifier;

    /** view audit log page */
    @Autowired
    private AuditLogPage auditLogPage;

    /** confirmation prompt */
    @Autowired
    private ConfirmationPrompt confirmationPrompt;

    /**
     * Click on view audit action
     */
    public AuditLogPage clickOnViewAuditLog()
    {
        return clickOnAction(VIEW_AUDIT, auditLogPage);
    }

    /**
     * Click on edit metadata action
     */
    public <T extends Renderable> T clickOnEditMetadata(T renderable)
    {
        return clickOnAction(EDIT_METADATA, renderable);
    }

    /**
     * @return get identifier
     */
    public String getIdentifier()
    {
        String identifierString = identifier.getText();
        return identifierString.substring(identifierString.indexOf(':')+2, identifierString.length());
    }

    /**
     * Click on delete
     */
    public ConfirmationPrompt clickOnDelete()
    {
        return clickOnAction(DELETE, confirmationPrompt);
    }

    //@TODO clickOnManagePermissions
}
