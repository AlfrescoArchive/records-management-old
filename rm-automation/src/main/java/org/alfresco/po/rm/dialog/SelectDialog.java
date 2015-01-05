package org.alfresco.po.rm.dialog;

import org.alfresco.po.common.buttonset.OkCancelButtonSet;
import org.alfresco.po.common.buttonset.StandardButtons;
import org.alfresco.po.common.renderable.Renderable;
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
public class SelectDialog extends Renderable implements StandardButtons
{
    /** search input */
    @FindBy(xpath = "//input[@class='search-input']")
    private TextInput searchInput;

    /** search button */
    @FindBy(xpath="//button[text()='Search']")
    private Button searchButton;

    /** add icon */
    @FindBy(xpath="//*[@class='addIcon']")
    private Button addIcon;

    /** remove icon */
    @FindBy(xpath="//*[@class='removeIcon']")
    private Button removeIcon;

    /** button set */
    @FindBy(css=".bdft")
    private OkCancelButtonSet buttonset;

    /**
     * Click on ok
     */
    public Renderable clickOnOk()
    {
        return buttonset.click(OK);
    }

    /**
     * Click on cancel
     */
    public Renderable clickOnCancel()
    {
        return buttonset.click(CANCEL);
    }

    /**
     * click on add icon
     */
    public SelectDialog clickAddIcon()
    {
        addIcon.click();
        return this.render();
    }

    /**
     * click on remove icon
     */
    public SelectDialog clickRemoveIcon()
    {
        removeIcon.click();
        return this.render();
    }

    /**
     * perform the search
     */
    public SelectDialog search(String request)
    {
        searchInput.sendKeys(request);
        searchButton.click();
        return this.render();
    }

}
