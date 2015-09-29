package org.alfresco.po.rm.actions.edit;

import org.alfresco.po.rm.properties.IdentifierAndVitalInformation;
import org.alfresco.po.share.form.FormPage;
import org.alfresco.po.share.properties.Content;
import org.openqa.selenium.support.FindBy;
import org.springframework.stereotype.Component;

/**
 * Edit Record Category page
 *
 * @author Tatiana Kalinovskaya
 */
@Component
public class EditRecordCategoryPage extends FormPage
{
    @FindBy(xpath = "//*[@class='set'][1]")
    private Content content;

    @FindBy(xpath = "//div[contains(@id,'-form-fields')]")
    private IdentifierAndVitalInformation identifierAndVitalInformation;

    public IdentifierAndVitalInformation getIdentifierAndVitalInformation()
    {
        return identifierAndVitalInformation;
    }

    public Content getContent()
    {
        return content;
    }
}
