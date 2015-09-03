/*
 * Copyright (C) 2005-2015 Alfresco Software Limited.
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
package org.alfresco.po.rm.site.tasks;

import org.alfresco.po.common.renderable.Renderable;
import org.alfresco.po.common.util.Utils;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.springframework.stereotype.Component;

import ru.yandex.qatools.htmlelements.element.Button;

/**
 * A task panel containing an invitation to join a site.
 *
 * @author tpage
 * @since 2.4.a
 */
@Component
public class SiteInvitationTaskPanel extends TaskPanel
{
    @FindBy(css = "#page_x002e_data-form_x002e_task-edit_x0023_default_prop_inwf_inviteOutcome-accept-button")
    private Button acceptButton;

    public <R extends Renderable> R acceptInvitation(R renderable)
    {
        Utils.waitFor(ExpectedConditions.elementToBeClickable(acceptButton.getWrappedElement()));
        acceptButton.click();
        // not ideal but this dialog needs a second to process what's happening
        // TODO figure out what can really be used to hold reliabily at this point
        try{Thread.sleep(1000);} catch (Exception e) {}
        return renderable.render();
    }
}
