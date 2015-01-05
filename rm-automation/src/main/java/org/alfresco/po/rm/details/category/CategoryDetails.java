package org.alfresco.po.rm.details.category;

import org.alfresco.po.common.annotations.RenderableChild;
import org.alfresco.po.rm.details.record.ActionsPanel;
import org.alfresco.po.rm.disposition.edit.steps.EditDispositionSchedulePage;
import org.alfresco.po.rm.disposition.edit.general.EditGeneralDispositionInformationPage;
import org.alfresco.po.rm.site.RMSiteNavigation;
import org.alfresco.po.share.details.DetailsPage;
import org.alfresco.po.share.details.document.PropertyPanel;
import org.alfresco.po.share.details.document.SharePanel;
import org.openqa.selenium.support.FindBy;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import ru.yandex.qatools.htmlelements.element.Button;

/**
 * Category Details page
 *
 * @author Tatiana Kalinovskaya
 */
@Component
public class CategoryDetails extends DetailsPage<RMSiteNavigation>
{
    /** Create Disposition Schedule button */
    @FindBy(css = "button[id*='createschedule-button']")
    private Button createdDispositionScheduleButton;

    /** Edit disposition general information button */
    @FindBy(css = "button[id*='editproperties-button']")
    private  Button editDispositionGeneralButton;

    /** Edit disposition steps button */
    @FindBy(css = "button[id*='editschedule-button']")
    private  Button editDispositionStepsButton;

    /** disposition block */
    @FindBy(css = ".disposition")
    private DispositionBlock disposition;

    @Autowired
    @RenderableChild
    private ActionsPanel categoryActionsPanel;

    @Autowired
    @RenderableChild
    private SharePanel sharePanel;

    @Autowired
    @RenderableChild
    private PropertyPanel propertyPanel;

    @Autowired
    private EditGeneralDispositionInformationPage editGeneralDispositionInformationPage;

    @Autowired
    private EditDispositionSchedulePage editDispositionSchedulePage;

    public DispositionBlock getDispositionBlock()
    {
        return disposition;
    }

    public ActionsPanel getCategoryActionsPanel()
    {
        return categoryActionsPanel;
    }

    public CategoryDetails clickOnCreateDispositionSchedule()
    {
        createdDispositionScheduleButton.click();
        return this.render();
    }

    public EditGeneralDispositionInformationPage clickEditDispositionGeneral()
    {
        editDispositionGeneralButton.click();
        return editGeneralDispositionInformationPage.render();
    }

    public EditDispositionSchedulePage clickEditDispositionSteps()
    {
        editDispositionStepsButton.click();
        return editDispositionSchedulePage.render();
    }
}
