package org.alfresco.po.rm.actions.edit;

import org.alfresco.po.rm.properties.IdentifierAndVitalInformation;
import org.alfresco.po.rm.properties.Location;
import org.alfresco.po.share.form.FormPage;
import org.alfresco.po.share.properties.Content;
import org.openqa.selenium.support.FindBy;
import org.springframework.stereotype.Component;

/**
 * Edit Record Folder page
 *
 * @author Tatiana Kalinovskaya
 */
@Component
public class EditRecordFolderPage extends FormPage
{
    @FindBy(xpath = "//*[@class='set'][1]")
    private Content content;

    @FindBy(xpath = "//div[contains(@id,'-form-fields')]")
    private IdentifierAndVitalInformation identifierAndVitalInformation;

    @FindBy(xpath = "//div[contains(@id,'-form-fields')]")
    private Location location;

    public IdentifierAndVitalInformation getIdentifierAndVitalInformation()
    {
        return identifierAndVitalInformation;
    }

    public Location getLocation()
    {
        return location;
    }

    public Content getContent()
    {
        return content;
    }
}
