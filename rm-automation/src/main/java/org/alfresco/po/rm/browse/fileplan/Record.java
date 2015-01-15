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
package org.alfresco.po.rm.browse.fileplan;

import static org.alfresco.po.common.util.Utils.elementExists;

import java.text.MessageFormat;

import org.alfresco.po.rm.details.record.RecordDetails;
import org.alfresco.po.rm.dialog.RequestInformationDialog;
import org.alfresco.po.rm.dialog.copymovelinkfile.CopyDialog;
import org.alfresco.po.rm.dialog.copymovelinkfile.MoveDialog;
import org.alfresco.po.rm.dialog.copymovelinkfile.RecordLinkDialog;
import org.openqa.selenium.By;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

/**
 * Record list item
 * 
 * @author Roy Wetherall
 */
@Scope("prototype")
@Component
public class Record extends DisposableItem implements RecordActions
{
    /** link selector */
    private static final String IDENTIFIER_SELECTOR_XPATH = (".//td[contains(@class,'fileName')]/div/div[{0}]/span");

    /** info banner */
    private static By infoBannerSelector = By.cssSelector("div.info-banner");

    /** linked indicator */
    private static By linkedSelector = By.cssSelector("img[alt='rm-multi-parent']");

    /** information requested indicator */
    private static By informationRequestedSelector = By.cssSelector("img[alt='rm-info-requested']");

    /** cutoff indicator */
    private static By cutoffRecordIndicatorSelector = By.cssSelector("img[alt='rm-cutoff']");

    /** record copy dialog */
    @Autowired
    private CopyDialog copyDialog;

    /** record move dialog */
    @Autowired
    private MoveDialog moveDialog;

    /** record link dialog */
    @Autowired
    private RecordLinkDialog recordLinkDialog;
    
    /** record details */
    @Autowired
    private RecordDetails recordDetails;

    /** request information dialog*/
    @Autowired
    private RequestInformationDialog requestInformationDialog;
    
    @Override
    public RecordDetails clickOnLink()
    {
        return super.clickOnLink(recordDetails);
    }
    
    /**
     * @return  true if record is incomplete, false otherwise
     */
    public boolean isIncomplete()
    {
        return elementExists(getRow(), infoBannerSelector);
    }
    
    /**
     * @return  true if record is linked, false otherwise
     */
    public boolean isLinked()
    {
        return elementExists(getRow(), linkedSelector);
    }

    /**
     * @return  true if information for record is requested, false otherwise
     */
    public boolean isInformationRequested()
    {
        return elementExists(getRow(), informationRequestedSelector);
    }

    /**
     * Click on complete record action
     */
    public FilePlan clickOnCompleteRecord()
    {
        return (FilePlan)clickOnAction(COMPLETE_RECORD);
    }
    
    /**
     * Click on reopen record action
     */
    public FilePlan clickOnReopenRecord()
    {
        return (FilePlan)clickOnAction(REOPEN_RECORD);
    }

    /**
     * Click on copy action
     */
    public CopyDialog clickOnCopyTo()
    {
        return clickOnAction(COPY, copyDialog);
    }

    /**
     * Click on move action
     */
    public MoveDialog clickOnMoveTo()
    {
        return clickOnAction(MOVE, moveDialog);
    }

    /**
     * Click on link action
     */
    public RecordLinkDialog clickOnLinkTo()
    {
        return clickOnAction(LINK, recordLinkDialog);
    }
    
    /**
     * Click on Request Information action
     */
    public RequestInformationDialog clickOnRequestInformation()
    {
        return clickOnAction(REQUEST_INFORMATION, requestInformationDialog);
    }

    /**
     * Helper method to get the action selector
     */
    private By getIdentifierSelector(int index)
    {
        String identifierXPATH = MessageFormat.format(IDENTIFIER_SELECTOR_XPATH, index);
        return By.xpath(identifierXPATH);
    }
    /**
     * @return get identifier
     */
    @Override
    public  String getIdentifier()
    {
        if (this.isIncomplete())
            return getIdentifier(2);
        else return getIdentifier(1);
    }

    /**
     * Helper method to get identifier
     * @return get identifier
     */
    public String getIdentifier(int index)
    {
        String identifierString = (getRow().findElement(getIdentifierSelector(index))).getText();
        return identifierString.substring(identifierString.lastIndexOf(':')+2, identifierString.length());
    }

    /**
     * @return  true if record is cut off, false otherwise
     */
    public boolean isCutOff()
    {
        return elementExists(getRow(), cutoffRecordIndicatorSelector);
    }

}