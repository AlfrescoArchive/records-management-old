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

import org.alfresco.po.common.renderable.Renderable;
import org.alfresco.po.rm.browse.transfers.Transfers;
import org.alfresco.po.rm.dialog.DestroyConfirmationDialog;
import org.alfresco.po.rm.dialog.EditDispositionDateDialog;
import org.alfresco.po.rm.dialog.copymovelinkfile.FileReportDialog;
import org.alfresco.po.rm.dialog.holds.AddToHoldDialog;
import org.alfresco.po.rm.dialog.holds.RemoveFromHoldDialog;
import org.openqa.selenium.By;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * Common for record folder and record
 *
 * @author Tatiana Kalinovskaya
 */
public abstract class DisposableItem extends FilePlanListItem
{
    /** transferred indicator */
    private static By transferredIndicatorSelector = By.cssSelector("img[alt='rm-transferred']");

    /** transfer pending indicator */
    private static By transferIndicatorSelector = By.cssSelector("img[alt='rm-transfer']");

    /** accessioned indicator */
    private static By accessionedIndicatorSelector = By.cssSelector("img[alt='rm-accessioned']");

    /** accession pending indicator */
    private static By accessionIndicatorSelector = By.cssSelector("img[alt='rm-accession']");

    /** destroyed indicator */
    private static By destroyedIndicatorSelector = By.cssSelector("img[alt='rm-destroyed']");

    /** frozen indicator */
    private static By frozenIndicatorSelector = By.cssSelector("img[alt='rm-frozen']");

    /** add to hold dialog */
    @Autowired
    private AddToHoldDialog addToHoldDialog;

    /** remove from hold dialog */
    @Autowired
    private RemoveFromHoldDialog removeFromHoldDialog;

    /** edit disposition date */
    @Autowired
    private EditDispositionDateDialog editDispositionDateDialog;

    /** confirmation prompt */
    @Autowired
    private DestroyConfirmationDialog destroyConfirmationDialog;

    /** file destruction report dialog */
    @Autowired
    private FileReportDialog fileReportDialog;

    /** transfers */
    @Autowired
    private Transfers transfers;

    /**
     * @return  true if record/folder is held, false otherwise
     */
    public boolean isHeld()
    {
        return elementExists(getRow(), frozenIndicatorSelector);
    }

    /**
     * @return  true if record/folder is transferred, false otherwise
     */
    public boolean isTransferred()
    {
        return elementExists(getRow(), transferredIndicatorSelector);
    }


    /**
     * @return  true if record/folder is pending transfer, false otherwise
     */
    public boolean isTransferPending()
    {
        return elementExists(getRow(), transferIndicatorSelector);
    }

    /**
     * @return  true if record/folder is accessioned, false otherwise
     */
    public boolean isAccessioned()
    {
        return elementExists(getRow(), accessionedIndicatorSelector);
    }


    /**
     * @return  true if record/folder is pending accession, false otherwise
     */
    public boolean isAccessionPending()
    {
        return elementExists(getRow(), accessionIndicatorSelector);
    }

    /**
     * @return  true if record/folder is destroyed, false otherwise
     */
    public boolean isDestroyed()
    {
        return elementExists(getRow(), destroyedIndicatorSelector);
    }

    /**
     * Click on add to hold action
     */
    public AddToHoldDialog clickOnAddToHold()
    {
        return clickOnAction(ADD_TO_HOLD, addToHoldDialog);
    }

    /**
     * Click on remove from hold action
     */
    public RemoveFromHoldDialog clickOnRemoveFromHold()
    {
        return clickOnAction(REMOVE_FROM_HOLD, removeFromHoldDialog);
    }

    /**
     * Click on edit disposition date
     */
    public EditDispositionDateDialog clickOnEditDispositionDate()
    {
        return clickOnAction(EDIT_DISPOSITION_DATE, editDispositionDateDialog);
    }

    /**
     * Click on cut off
     */
    public Renderable clickOnCutOff()
    {
        return clickOnAction(CUTOFF);
    }

    /**
     * Click on undo cut off
     */
    public Renderable clickOnUndoCutOff()
    {
        return clickOnAction(UNDO_CUTOFF);
    }


    /**
     * Click on end retention
     */
    public Renderable clickOnEndRetention()
    {
        return clickOnAction(END_RETENTION);
    }

    /**
     * Click on transfer
     */
    public Transfers clickOnTransfer()
    {
        clickOnAction(TRANSFER);
        return transfers.render();
    }

    /**
     * Click on accession
     */
    public Transfers clickOnAccession()
    {
        clickOnAction(ACCESSION);
        return transfers.render();
    }

    /**
     * Click on destroy
     */
    public DestroyConfirmationDialog clickOnDestroy()
    {
        clickOnAction(DESTROY, destroyConfirmationDialog);
        return destroyConfirmationDialog;
    }

    /**
     * Click on Generate Destruction Report
     */
    public FileReportDialog clickOnGenerateDestructionReport()
    {
        return clickOnAction(GENERATE_DESTRUCTION_REPORT,fileReportDialog);
    }
}
