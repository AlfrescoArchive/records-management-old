package org.alfresco.po.rm.dialog;

import org.alfresco.po.common.buttonset.StandardButtons;
import org.alfresco.po.common.renderable.Renderable;
import org.alfresco.po.common.util.Utils;
import org.openqa.selenium.support.FindBy;
import org.springframework.stereotype.Component;

import ru.yandex.qatools.htmlelements.element.Button;
import ru.yandex.qatools.htmlelements.element.TextInput;

/**
 * Select dialog
 *
 * @author Tatiana Kalinovskaya
 */
@Component
public class AuthoritySelectDialog extends Renderable implements StandardButtons
{
    @FindBy(css = "input[id*='search-text']")
    private TextInput authoritySearchInput;
    
    /** authority search button*/
    @FindBy(css = "button[id*='search-button-button']")
    private Button authoritySearchButton;

   
    @FindBy(xpath = "//button[contains(text(),'Add')]")
    private Button addButton;

    
    /**
     * perform the search 
     */
    public AuthoritySelectDialog authoritySearch(String request)
    {
        authoritySearchInput.sendKeys(request);
        authoritySearchButton.click();
        return this.render();
    }
    /**
     * click on Add button
     */
    public void clickAddButton()
    {
        
        Utils.mouseOver(addButton);
        while(!addButton.isEnabled())
        {
           Utils.mouseOver(addButton);
        }
        addButton.click();
    }
    
}
