package org.alfresco.po.common.buttonset;

import org.openqa.selenium.support.FindBy;
import ru.yandex.qatools.htmlelements.element.Button;

/**
 * Submit Cancel button set
 *
 * @author Tatiana Kalinovskaya
 */
public class SubmitCancelButtonSet extends ButtonSet
{
    /** submit button */
    @FindBy(css = "button[id*='submit-button']")
    private Button submit;

    /** cancel button */
    @FindBy(css = "button[id*='cancel-button']")
    private Button cancel;
}
