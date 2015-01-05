package org.alfresco.po.rm.browse.holds;

import org.alfresco.po.common.Toolbar;
import org.alfresco.po.rm.dialog.create.NewHoldDialog;
import org.openqa.selenium.support.FindBy;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * Holds toolbar
 * 
 * @author Roy Wetherall
 */
@Component
public class HoldsToolbar extends Toolbar
{
    /** new hold button name */
    private static final String NEW_HOLD = "newHold";
    
    /** toolbar buttons */
    @FindBy(css="div.header-bar")
    private HoldsToolbarButtonSet toolbarButtons;
    
    /** new hold dialog */
    @Autowired
    private NewHoldDialog newHoldDialog;
    
    /**
     * is the new hold button clickable
     */
    public boolean isClickableNewHold()
    {
        return toolbarButtons.isButtonClickable(NEW_HOLD);
    }

    /**
     * click on the new hold button
     */
    public NewHoldDialog clickOnNewHold()
    {
        return toolbarButtons.click(NEW_HOLD, newHoldDialog);
    }
    
}
