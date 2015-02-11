package org.alfresco.po.rm.actions.edit;

import org.alfresco.po.rm.properties.Location;
import org.alfresco.po.share.form.FormPage;
import org.alfresco.po.share.properties.Content;
import org.openqa.selenium.support.FindBy;
import org.springframework.stereotype.Component;

/**
 * Edit electronic record page
 *
 * @author Tatiana Kalinovskaya
 */
@Component
public class EditElectronicRecordPage extends FormPage
{
    @FindBy(xpath = "//*[@class='set'][1]")
    private Content content;

    @FindBy(xpath = "//*[@class='set'][2]")
    private Location location;

    public Content getContent()
    {
        return content;
    }

    public Location getLocation()
    {
        return location;
    }

}
