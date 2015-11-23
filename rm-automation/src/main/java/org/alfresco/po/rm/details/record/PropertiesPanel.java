package org.alfresco.po.rm.details.record;

import org.alfresco.po.common.util.Utils;
import org.alfresco.po.share.panel.Panel;
import org.openqa.selenium.By;
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
    
    public enum Properties
    {       
        NAME(0),
        TITLE(1),
        DESCRIPTION(2),
        MIMETYPE_OR_IDENTIFIER(3),
        AUTHOR(4),
        SIZE(5),
        CREATOR(6),
        CREATED_DATE(7),
        MODIFIER(8),
        MODIFIED_DATE(9),
        OWNER(10);
        
         private int index;

        private Properties(int index) {
            this.index = index;
        }
        
        public int getIndex()
        {
            return index;
        }        
    }
    
    public static String getPropertyValue(Properties prop)
    {
        return Utils.getWebDriver().findElements(By.cssSelector(".form-field .viewmode-value")).get(prop.getIndex()).getText();
    }        
    
}
