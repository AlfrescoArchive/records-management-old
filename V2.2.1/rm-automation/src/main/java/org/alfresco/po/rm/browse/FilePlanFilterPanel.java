/*
 * Copyright (C) 2005-2014 Alfresco Software Limited.
 *
 * This file is part of Alfresco
 *
 * Alfresco is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * Alfresco is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with Alfresco. If not, see <http://www.gnu.org/licenses/>.
 */
package org.alfresco.po.rm.browse;

import org.alfresco.po.rm.browse.holds.Holds;
import org.alfresco.po.rm.browse.transfers.Transfers;
import org.alfresco.po.rm.browse.unfiledrecords.UnfiledRecords;
import org.alfresco.po.share.panel.Panel;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import ru.yandex.qatools.htmlelements.element.Link;

/**
 * File plan filter panel
 * 
 * @author Roy Wetherall
 */
@Component
public class FilePlanFilterPanel extends Panel
{
    @FindBy(css="div[id='alf-filters'] div[id$='rm-fileplan'] h2")
    private WebElement clickableTitle;
    
    @FindBy(css="div[id='alf-filters'] div[id$='rm-fileplan'] span[class='transfers'] a")
    private Link transfersLink;

    @FindBy(css="div[id='alf-filters'] div[id$='rm-fileplan'] span[class='holds'] a")
    private Link holdsLink;
    
    @FindBy(css="div[id='alf-filters'] div[id$='rm-fileplan'] span[class='unfiledRecords'] a")
    private Link unfiledRecordsLink;
    
    @Autowired
    private UnfiledRecords unfiledRecords;

    @Autowired
    private Transfers transfers;

    @Autowired
    private Holds holds;
    
    @Override
    protected WebElement getClickableTitle()
    {
        return clickableTitle;
    }
    
    public UnfiledRecords clickOnUnfiledRecords()
    {
        unfiledRecordsLink.click();
        return unfiledRecords.render();
    }

    public Transfers clickOnTransfers()
    {
        transfersLink.click();
        return transfers.render();
    }
    
    public Holds clickOnHolds()
    {
        holdsLink.click();
        return holds.render();
    }
}
