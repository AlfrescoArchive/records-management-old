package org.alfresco.po.rm.properties;

import static org.alfresco.po.common.util.Utils.clearAndType;

import org.alfresco.po.rm.dialog.VitalReviewPeriod;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

import ru.yandex.qatools.htmlelements.element.CheckBox;
import ru.yandex.qatools.htmlelements.element.HtmlElement;
import ru.yandex.qatools.htmlelements.element.Select;
import ru.yandex.qatools.htmlelements.element.TextInput;

/**
 * @author Tatiana Kalinovskaya
 */
public class IdentifierAndVitalInformation extends HtmlElement
{
    /** identifier text input */
    @FindBy(name = "prop_rma_identifier")
    private TextInput identifierTextInput;

    /** vital indicator checkbox */
    @FindBy(css = "input[id$='prop_rma_vitalRecordIndicator-entry']")
    private CheckBox vitalIndicatorCheckbox;

    /** vital reviewPeriod select box */
    @FindBy(css = "select[id$='prop_rma_reviewPeriod-cntrl-type']")
    private Select reviewPeriodSelectBox;

    /** vital period expression text input */
    @FindBy(css = "input[id$='prop_rma_reviewPeriod-cntrl-expression']")
    private TextInput periodExpressionTextInput;

    /**
     * Get identifier value
     */
    public String getIdentifier()
    {
        return identifierTextInput.getText();
    }

    /**
     * Set identifier value
     */
    public IdentifierAndVitalInformation setIdentifier(String identifier)
    {
        clearAndType(identifierTextInput, identifier);
        return this;
    }

    /**
     * Get site reviewPeriod
     */
    public VitalReviewPeriod getReviewPeriod()
    {
        WebElement selectedElement = reviewPeriodSelectBox.getFirstSelectedOption();
        String value = selectedElement.getAttribute("value");
        return VitalReviewPeriod.fromValue(value);
    }

    /**
     * Set site reviewPeriod
     */
    public IdentifierAndVitalInformation setReviewPeriod(VitalReviewPeriod period)
    {
        reviewPeriodSelectBox.selectByValue(period.toString());
        return this;
    }

    /**
     * Get vital period expression value
     */
    public String getPeriodExpression()
    {
        return periodExpressionTextInput.getText();
    }

    /**
     * Set vital period expression value
     */
    public IdentifierAndVitalInformation setPeriodExpression(String periodExpression)
    {
        periodExpressionTextInput.sendKeys(periodExpression);
        return this;
    }

    /**
     * Check vital indicator
     */
    public IdentifierAndVitalInformation checkVitalIndicator(boolean check)
    {
        vitalIndicatorCheckbox.set(check);
        return this;
    }

    /**
     * is vital indicator selected
     */
    public boolean isVitalIndicatorSelected()
    {
        return vitalIndicatorCheckbox.isSelected();
    }

}
