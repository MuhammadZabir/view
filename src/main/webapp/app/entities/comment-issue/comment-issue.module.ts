import { NgModule, CUSTOM_ELEMENTS_SCHEMA } from '@angular/core';
import { RouterModule } from '@angular/router';

import { ViewSharedModule } from 'app/shared';
import { ViewAdminModule } from 'app/admin/admin.module';
import {
    CommentIssueComponent,
    CommentIssueDetailComponent,
    CommentIssueUpdateComponent,
    CommentIssueDeletePopupComponent,
    CommentIssueDeleteDialogComponent,
    commentIssueRoute,
    commentIssuePopupRoute
} from './';

const ENTITY_STATES = [...commentIssueRoute, ...commentIssuePopupRoute];

@NgModule({
    imports: [ViewSharedModule, ViewAdminModule, RouterModule.forChild(ENTITY_STATES)],
    declarations: [
        CommentIssueComponent,
        CommentIssueDetailComponent,
        CommentIssueUpdateComponent,
        CommentIssueDeleteDialogComponent,
        CommentIssueDeletePopupComponent
    ],
    entryComponents: [
        CommentIssueComponent,
        CommentIssueUpdateComponent,
        CommentIssueDeleteDialogComponent,
        CommentIssueDeletePopupComponent
    ],
    schemas: [CUSTOM_ELEMENTS_SCHEMA]
})
export class ViewCommentIssueModule {}
