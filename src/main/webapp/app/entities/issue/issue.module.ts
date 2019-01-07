import { NgModule, CUSTOM_ELEMENTS_SCHEMA } from '@angular/core';
import { RouterModule } from '@angular/router';

import { ViewSharedModule } from 'app/shared';
import { ViewAdminModule } from 'app/admin/admin.module';
import {
    IssueComponent,
    IssueDetailComponent,
    IssueUpdateComponent,
    IssueDeletePopupComponent,
    IssueDeleteDialogComponent,
    issueRoute,
    issuePopupRoute
} from './';

const ENTITY_STATES = [...issueRoute, ...issuePopupRoute];

@NgModule({
    imports: [ViewSharedModule, ViewAdminModule, RouterModule.forChild(ENTITY_STATES)],
    declarations: [IssueComponent, IssueDetailComponent, IssueUpdateComponent, IssueDeleteDialogComponent, IssueDeletePopupComponent],
    entryComponents: [IssueComponent, IssueUpdateComponent, IssueDeleteDialogComponent, IssueDeletePopupComponent],
    schemas: [CUSTOM_ELEMENTS_SCHEMA]
})
export class ViewIssueModule {}
