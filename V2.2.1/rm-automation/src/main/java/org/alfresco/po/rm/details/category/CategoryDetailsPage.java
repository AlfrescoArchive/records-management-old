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
package org.alfresco.po.rm.details.category;

import org.alfresco.po.common.annotations.RenderableChild;
import org.alfresco.po.rm.disposition.edit.steps.EditDispositionSchedulePage;
import org.alfresco.po.rm.disposition.edit.general.EditGeneralDispositionInformationPage;
import org.alfresco.po.rm.site.RMSiteNavigation;
import org.alfresco.po.share.details.DetailsPage;
import org.alfresco.po.share.details.document.PropertyPanel;
import org.alfresco.po.share.details.document.SharePanel;
import org.openqa.selenium.support.FindBy;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * Category Details page
 *
 * @author Tatiana Kalinovskaya
 */
@Component
public class CategoryDetailsPage extends DetailsPage<RMSiteNavigation>
{
    /** disposition block */
    @FindBy(css = ".disposition")
    private DispositionBlock disposition;

    @Autowired
    @RenderableChild
    private CategoryActionsPanel categoryActionsPanel;

    @Autowired
    @RenderableChild
    private SharePanel sharePanel;

    @Autowired
    @RenderableChild
    private PropertyPanel propertyPanel;

    /** edit general disposition page*/
    @Autowired
    private EditGeneralDispositionInformationPage editGeneralDispositionInformationPage;

    /** edit disposition steps page */
    @Autowired
    private EditDispositionSchedulePage editDispositionSchedulePage;

    /** helper method to get disposition block */
    public DispositionBlock getDispositionBlock()
    {
        return disposition;
    }

    /** helper method to get actions panel */
    public CategoryActionsPanel getCategoryActionsPanel()
    {
        return categoryActionsPanel;
    }

    /** create disposition schedule
     *
     * @return category details page
     */
    public CategoryDetailsPage createDispositionSchedule()
    {
        getDispositionBlock().clickOnCreateDispositionSchedule();
        return this.render();
    }

    /** view description of the disposition step
     *
     * @param number - number of step
     * @return decription
     */
    public String viewStepDescription(int number)
    {
        getDispositionBlock().clickOnViewDescription(number);
        this.render();
        return getDispositionBlock().getStepDescription(number);
    }

    /** edit disposition general information
     * @return editGeneralDispositionInformationPage
     */
    public EditGeneralDispositionInformationPage editDispositionGeneral()
    {
        getDispositionBlock().clickOnEditDispositionGeneral();
        return editGeneralDispositionInformationPage.render();
    }

    /** edit disposition steps
     * @return editDispositionSchedulePage
     */
    public EditDispositionSchedulePage editDispositionSteps()
    {
        getDispositionBlock().clickOnEditDispositionSteps();
        return editDispositionSchedulePage.render();
    }
}
