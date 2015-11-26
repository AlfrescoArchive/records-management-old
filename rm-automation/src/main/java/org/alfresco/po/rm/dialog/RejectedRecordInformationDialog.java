
package org.alfresco.po.rm.dialog;

import org.alfresco.po.common.Dialog;
import org.alfresco.po.common.buttonset.StandardButtons;
import org.alfresco.po.common.renderable.Renderable;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.springframework.stereotype.Component;
import ru.yandex.qatools.htmlelements.element.Button;

import java.util.List;

import static org.alfresco.po.common.util.Utils.waitForVisibilityOf;
import static org.alfresco.po.common.util.Utils.waitForInvisibilityOf;
import static org.alfresco.po.common.util.Utils.getVisibleElement;

/**
 * Rejected record information dialog
 *
 * @author Oana Nechiforescu
 */
@Component
public class RejectedRecordInformationDialog extends Dialog
{
    /** rejection description */
    By DESCRIPTION_SELECTOR = By.cssSelector("#rejectedRecordInfoDialog-description");

    /** rejection made by user */
    By BY_USER_SELECTOR = By.cssSelector("#rejectedRecordInfoDialog-userId");

    /** rejection reason */
    By REASON_SELECTOR = By.cssSelector("#rejectedRecordInfoDialog-rejectReason");

    /** close information about rejection dialog button */
    By CLOSE_DIALOG_SELECTOR = By.cssSelector("#rejectedRecordInfoDialog-cancel-button");

    /** all the rejection information dialogs */
    By REJECTED_DIALOGS_SELECTOR = By.cssSelector("#rejectedRecordInfoDialog");

    /** retrieve the visible rejection information dialog */
    private WebElement getDialog()
    {
        waitForVisibilityOf(REJECTED_DIALOGS_SELECTOR);
        return getVisibleElement(REJECTED_DIALOGS_SELECTOR);
    }

    /** get the user that initiated the rejection */
    public String getByUser()
    {
        return getDialog().findElement(BY_USER_SELECTOR).getAttribute("value");
    }

    /** get the description of the rejection */
    public String getDescription()
    {
        return getDialog().findElement(DESCRIPTION_SELECTOR).getText();
    }

    /** get the reason for the rejection */
    public String getReason()
    {
        return getDialog().findElement(REASON_SELECTOR).getText();
    }

    public void close()
    {
        getDialog().findElement(CLOSE_DIALOG_SELECTOR).click();
        waitForInvisibilityOf(REJECTED_DIALOGS_SELECTOR);
    }
}
