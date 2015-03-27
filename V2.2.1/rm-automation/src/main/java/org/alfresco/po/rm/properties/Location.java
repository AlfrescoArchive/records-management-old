package org.alfresco.po.rm.properties;

import static org.alfresco.po.common.util.Utils.clearAndType;

import org.openqa.selenium.support.FindBy;

import ru.yandex.qatools.htmlelements.element.HtmlElement;
import ru.yandex.qatools.htmlelements.element.TextInput;

/**
 * @author Tatiana Kalinovskaya
 */
public class Location extends HtmlElement
{
    /** location text input */
    @FindBy(name = "prop_rma_location")
    private TextInput locationTextInput;

    /**
     * Get location value
     */
    public String getLocationField()
    {
        return locationTextInput.getText();
    }

    /**
     * Set location value
     */
    public Location setLocationField(String location)
    {
        clearAndType(locationTextInput, location);
        return this;
    }

    /**
     * is location field enabled
     */
    public boolean isLocationEnabled()
    {
        return locationTextInput.isEnabled();
    }

}
