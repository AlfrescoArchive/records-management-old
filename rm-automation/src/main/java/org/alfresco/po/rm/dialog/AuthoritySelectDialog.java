package org.alfresco.po.rm.dialog;

import org.alfresco.po.common.buttonset.StandardButtons;
import org.alfresco.po.common.renderable.Renderable;
import org.alfresco.po.common.util.Utils;
import org.alfresco.po.share.page.Message;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.ui.ExpectedCondition;
import org.springframework.beans.factory.annotation.Autowired;
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
    
    @FindBy(css= "div [id*= 'authorityFinder']")
    private WebElement getResultsWebElement;

    @Autowired
    private Message waitMessage;
    
    private static final By MESSAGE_SELECTOR = By.cssSelector("div[style*='visibility: visible'] tbody[class$='message'] div");

    
       
    /**
     * perform the search 
     */
    public AuthoritySelectDialog authoritySearch(String request)
    {
        authoritySearchInput.sendKeys(request);
        authoritySearchButton.click();
        // wait for searching message to be hidden
        Utils.webDriverWait().until(searchFinished());
        return this;
    }
    

    /**
     * Helper method to return custom expected condition that returns true when
     * the search is complete.
     */
    protected ExpectedCondition<Boolean> searchFinished()
    {
        return new ExpectedCondition<Boolean>()
        {
            public Boolean apply(WebDriver arg0)
            {
                WebElement message = getResultsWebElement.findElement(MESSAGE_SELECTOR);
                return !(message.isDisplayed() && message.getText().contains("Searching"));
            }
        };
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
