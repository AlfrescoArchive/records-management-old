package org.alfresco.po.rm.details.record;

import org.alfresco.po.share.panel.Panel;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

/**
 * Properties panel
 *
 * @author Tatiana Kalinovskaya
 */
public class PropertiesPanel extends Panel
{
    @FindBy(css="div.document-metadata-header h2")
    private WebElement clickableTitle;

    @Override
    protected WebElement getClickableTitle()
    {
        return clickableTitle;
    }
}
