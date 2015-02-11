package org.alfresco.po.rm.properties;

import static org.alfresco.po.common.util.Utils.clearAndType;

import org.openqa.selenium.support.FindBy;

import ru.yandex.qatools.htmlelements.element.HtmlElement;
import ru.yandex.qatools.htmlelements.element.TextInput;

/**
 * Non-electronic record properties
 *
 * @author Tatiana Kalinovskaya
 */
public class NonElectronicRecord extends HtmlElement
{

    /** Physical Size text input */
    @FindBy(name = "prop_rma_physicalSize")
    private TextInput physicalSizeTextInput;

    /** Number Of Copies text input */
    @FindBy(name = "prop_rma_numberOfCopies")
    private TextInput numberOfCopiesTextInput;

    /** Physical Size text input */
    @FindBy(name = "prop_rma_storageLocation")
    private TextInput storageLocationTextInput;

    /** Shelf text input */
    @FindBy(name = "prop_rma_shelf")
    private TextInput shelfTextInput;

    /** Box text input */
    @FindBy(name = "prop_rma_box")
    private TextInput boxTextInput;

    /** File text input */
    @FindBy(name = "prop_rma_file")
    private TextInput fileTextInput;

    /**
     * Get Physical Size value
     */
    public String getPhysicalSize()
    {
        return physicalSizeTextInput.getText();
    }

    /**
     * Set Physical Size value
     */
    public NonElectronicRecord setPhysicalSize(String physicalSize)
    {
        clearAndType(physicalSizeTextInput, physicalSize);
        return this;
    }

    /**
     * Get NumberOfCopies
     */
    public String getNumberOfCopies()
    {
        return numberOfCopiesTextInput.getText();
    }

    /**
     * Set Number Of Copies
     */
    public NonElectronicRecord setNumberOfCopies(String numberOfCopies)
    {
        clearAndType(numberOfCopiesTextInput, numberOfCopies);
        return this;
    }

    /**
     * Get Storage Location value
     */
    public String getStorageLocation()
    {
        return storageLocationTextInput.getText();
    }

    /**
     * Set Storage Location value
     */
    public NonElectronicRecord setStorageLocation(String storageLocation)
    {
        clearAndType(storageLocationTextInput, storageLocation);
        return this;
    }

    /**
     * Get Physical Size value
     */
    public String getShelf()
    {
        return shelfTextInput.getText();
    }

    /**
     * Set Shelf
     */
    public NonElectronicRecord setShelf(String shelf)
    {
        clearAndType(shelfTextInput, shelf);
        return this;
    }

    /**
     * Get NumberOfCopies
     */
    public String getBox()
    {
        return boxTextInput.getText();
    }

    /**
     * Set Box value
     */
    public NonElectronicRecord setBox(String box)
    {
        clearAndType(boxTextInput, box);
        return this;
    }

    /**
     * Get Storage Location value
     */
    public String getFile()
    {
        return fileTextInput.getText();
    }

    /**
     * Set File
     */
    public NonElectronicRecord setFile(String file)
    {
        clearAndType(fileTextInput, file);
        return this;
    }

}
