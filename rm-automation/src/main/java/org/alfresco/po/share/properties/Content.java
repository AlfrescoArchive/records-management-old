package org.alfresco.po.share.properties;

import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import ru.yandex.qatools.htmlelements.element.HtmlElement;
import ru.yandex.qatools.htmlelements.element.Select;
import ru.yandex.qatools.htmlelements.element.TextInput;

import static org.alfresco.po.common.util.Utils.clearAndType;

/**
 * Content properties
 *
 * @author Tatiana Kalinovskaya
 */
public class Content extends HtmlElement
{
    /** name text input */
    @FindBy(name = "prop_cm_name")
    private TextInput nameTextInput;

    /** title text input */
    @FindBy(name = "prop_cm_title")
    private TextInput titleTextInput;

    /** description text input */
    @FindBy(name = "prop_cm_description")
    private TextInput descriptionTextInput;

    /** mimetype select box */
    @FindBy(name = "prop_mimetype")
    private Select mimetypeSelectBox;

    /** author text input */
    @FindBy(name = "prop_cm_author")
    private TextInput authorTextInput;

    /** author text input */
    @FindBy(name = "prop_cm_owner")
    private TextInput ownerTextInput;

//TODO Tags


    /**
     * Set name value
     */
    public Content setNameValue(String name)
    {
        clearAndType(nameTextInput, name);
        return this;
    }

    /**
     * Get name value
     */
    public String getNameValue()
    {
        return nameTextInput.getText();
    }

    /**
     * Get description value
     */
    public String getDescription()
    {
        return descriptionTextInput.getWrappedElement().getAttribute("value");
    }

    /**
     * Set description value
     */
    public Content setDescription(String description)
    {
        clearAndType(descriptionTextInput, description);
        return this;
    }

    /**
     * Get title value
     */
    public String getTitle()
    {
        return titleTextInput.getText();
    }

    /**
     * Set title value
     */
    public Content setTitle(String title)
    {
        clearAndType(titleTextInput, title);
        return this;
    }

    /**
     * Get identifier value
     */
    public Mimetype getMimetypeSelectBox()
    {
        WebElement selectedElement =  mimetypeSelectBox.getFirstSelectedOption();
        String value = selectedElement.getAttribute("value");
        return Mimetype.fromValue(value);
    }

    /**
     * Set mimetype value
     */
    public Content setMimetype(String mimetype)
    {
        mimetypeSelectBox.selectByValue(mimetype.toString());
        return this;
    }

    /**
     * Get author value
     */
    public String getAuthor()
    {
        return authorTextInput.getText();
    }

    /**
     * Set author value
     */
    public Content setAuthor(String author)
    {
        clearAndType(authorTextInput, author);
        return this;
    }

    /**
     * Get owner value
     */
    public String getOwner()
    {
        return ownerTextInput.getText();
    }

    /**
     * Set owner value
     */
    public Content setOwner(String owner)
    {
        clearAndType(authorTextInput, owner);
        return this;
    }

    /**
     * is name enabled
     */
    public boolean isNameEnabled()
    {
        return nameTextInput.isEnabled();
    }

    /**
     * is description enabled
     */
    public boolean isDescritionEnabled()
    {
        return descriptionTextInput.isEnabled();
    }

    /**
     * is title enabled
     */
    public boolean isTitleEnabled()
    {
        return titleTextInput.isEnabled();
    }

    /**
     * is mimetype enabled
     */
    public boolean isMimetypeEnabled()
    {
        return mimetypeSelectBox.isEnabled();
    }
    /**
     * is author enabled
     */
    public boolean isAuthorEnabled()
    {
        return authorTextInput.isEnabled();
    }

    /**
     * is owner enabled
     */
    public boolean isOwnerEnabled()
    {
        return ownerTextInput.isEnabled();
    }

}
