package org.alfresco.po.rm.dialog;

/**
 * Request information dialog
 *
 * @author Tatiana Kalinovskaya
 */

import org.alfresco.po.common.Dialog;
import org.alfresco.po.common.buttonset.StandardButtons;
import org.alfresco.po.common.buttonset.SubmitCancelButtonSet;
import org.alfresco.po.common.renderable.Renderable;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import ru.yandex.qatools.htmlelements.element.Button;
import ru.yandex.qatools.htmlelements.element.TextInput;

import static org.alfresco.po.common.util.Utils.clearAndType;

@Component
public class RequestInformationDialog extends Dialog implements StandardButtons
{
    /** name of the record the information is requested for */
    @FindBy(xpath=".//h3[@class='name']")
    private WebElement recordName;

    /** select users and groups button */
    @FindBy(xpath="//button[text()='Select']")
    private Button selectButton;

    /** Requested Information text area */
    @FindBy(xpath="//textarea")
    private TextInput requestedInfoArea;

    /** button set */
    @FindBy(css=".form-buttons")
    private SubmitCancelButtonSet buttonset;

    /** select users and groups dialog */
    @Autowired
    private SelectDialog selectDialog;

    /**
     * get name of record listed in "Information requested for record" block
     */
    public String getRecordName()
    {
        return recordName.getText();
    }

    /**
     * click on select button
     */
    public SelectDialog clickOnSelectUsersAndGroups()
    {
        selectButton.click();
        return selectDialog.render();
    }

    /**
     *fill in Requested information area
     */
    public RequestInformationDialog setRequestedInfoArea(String requestedInfo)
    {
        clearAndType(requestedInfoArea, requestedInfo);
        return this;
    }

    /**
     * Click on Request Information
     * @return render file plan or detail page
     */
    public <T extends Renderable> T clickRequestInformation(T renderable)
    {
        return buttonset.click(REQUEST_INFORMATION, renderable);
    }

    /**
     * Click on cancel
     */
    public <T extends Renderable> T clickOnCancel(T renderable)
    {
        return buttonset.click(CANCEL, renderable);
    }

}
