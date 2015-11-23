package org.alfresco.po.rm.actions.edit;

import org.alfresco.po.common.renderable.Renderable;
import org.alfresco.po.common.util.Utils;
import org.alfresco.po.rm.details.category.CategoryDetailsPage;
import org.alfresco.po.rm.properties.IdentifierAndVitalInformation;
import org.alfresco.po.share.form.FormPage;
import org.alfresco.po.share.properties.Content;
import org.openqa.selenium.By;
import org.openqa.selenium.support.FindBy;
import org.springframework.beans.factory.annotation.Autowired;
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

    @Autowired
    private CategoryDetailsPage detailsPage;

    public IdentifierAndVitalInformation getIdentifierAndVitalInformation()
    {
        return identifierAndVitalInformation;
    }

    public Content getContent()
    {
        return content;
    }

    public Renderable saveChanges(Renderable renderable)
    {
        Utils.getWebDriver().findElement(By.cssSelector("button[id$='submit-button']")).click();
        return renderable.render();
    }
}
