package org.alfresco.po.rm.disposition.edit.general;

import static org.alfresco.po.common.util.Utils.clearAndType;

import org.alfresco.po.rm.disposition.DispositionLevel;
import org.alfresco.po.share.form.FormPage;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.springframework.stereotype.Component;

import ru.yandex.qatools.htmlelements.element.Select;
import ru.yandex.qatools.htmlelements.element.TextInput;

/**
 * Edit General disposition properties page
 *
 * @author Tatiana Kalinovskaya
 */
@Component
public class EditGeneralDispositionInformationPage extends FormPage
{
    @FindBy (name="prop_rma_dispositionAuthority")
    private TextInput dispositionAuthorityInput;

    @FindBy (name="prop_rma_dispositionInstructions")
    private TextInput dispositionInstructionsInput;

    @FindBy (name="prop_rma_recordLevelDisposition")
    private Select dispositionLevelSelectBox;


    /**
     * Get Disposition Authority
     */
    public String getDispositionAuthority()
    {
        return dispositionAuthorityInput.getText();
    }

    /**
     * Set Disposition Authority
     */
    public EditGeneralDispositionInformationPage setDispositionAuthority(String dispositionAuthority)
    {
        clearAndType(dispositionAuthorityInput, dispositionAuthority);
        return this;
    }

    /**
     * Get Disposition Instructions
     */
    public String getDispositionInstructions()
    {
        return dispositionInstructionsInput.getText();
    }

    /**
     * Set Disposition Instructions
     */
    public EditGeneralDispositionInformationPage setDispositionInstructions(String dispositionInstructions)
    {
        clearAndType(dispositionInstructionsInput, dispositionInstructions);
        return this;
    }

    /**
     * Get Disposition Level
     */
    public DispositionLevel getDispositionLevel()
    {
        WebElement selectedElement = dispositionLevelSelectBox.getFirstSelectedOption();
        String value = selectedElement.getAttribute("value");
        return DispositionLevel.fromValue(value);
    }

    /**
     * Set Disposition level
     */
    public EditGeneralDispositionInformationPage setDispositionLevel(DispositionLevel dispositionLevel)
    {
        dispositionLevelSelectBox.selectByVisibleText(dispositionLevel.toString());
        return this;
    }

    /**
     * Is disposition level enabled
     */
    public Boolean isDispositionLevelEnabled()
    {
        return dispositionLevelSelectBox.isEnabled();
    }
}
