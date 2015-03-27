package org.alfresco.po.rm.disposition.edit.steps;

import static org.alfresco.po.common.util.Utils.clearAndType;

import org.alfresco.po.rm.disposition.PeriodUnit;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

import ru.yandex.qatools.htmlelements.element.CheckBox;
import ru.yandex.qatools.htmlelements.element.HtmlElement;
import ru.yandex.qatools.htmlelements.element.Select;
import ru.yandex.qatools.htmlelements.element.TextInput;

/**
 * Period section of disposition step block
 *
 * @author Tatiana Kalinovskaya
 */
public class PeriodSection extends HtmlElement
{
    @FindBy (css = ".period-enabled")
    private CheckBox periodCheckBox;

    @FindBy (css = ".period-amount")
    private TextInput periodAmountTextInput;

    @FindBy (css = ".period-unit")
    private Select periodUnitSelectBox;

    @FindBy (css = ".period-action")
    private Select periodActionSelectBox;

    /**
     * Is ghostOnDestroy Checkbox selected
     */
    public Boolean isPeriodCheckboxSelected()
    {
        return periodCheckBox.isSelected();
    }

    /**
     * Get period amount
     */
    public String getPeriodAmount()
    {
        return periodAmountTextInput.getText();
    }


    /**
     * Get Period Unit
     */
    public String getPeriodUnit()
    {
        WebElement selectedElement = periodUnitSelectBox.getFirstSelectedOption();
        return selectedElement.getText();
    }

    /**
     * Get Period Action
     */
    public String getPeriodAction()
    {
        WebElement selectedElement = periodActionSelectBox.getFirstSelectedOption();
        return selectedElement.getText();
    }

    /**
     * Enable/Disable period checkbox
     */
    public PeriodSection checkPeriod(boolean check)
    {
        periodCheckBox.set(check);
        return this;
    }

    /**
     * Set period amount
     */
    public PeriodSection setPeriodAmount(String periodAmount)
    {
        clearAndType(periodAmountTextInput, periodAmount);
        return this;
    }

    /**
     * Set period unit
     */
    public PeriodSection setPeriodUnit(PeriodUnit periodUnit)
    {
        periodUnitSelectBox.selectByValue(periodUnit.toString());
        return this;
    }

    /**
     * Set period action
     */
    //TODO create enum for period action
    public PeriodSection setPeriodAction(String periodAction)
    {
        periodActionSelectBox.selectByValue(periodAction);
        return this;
    }
}
