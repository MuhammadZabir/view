import { NgModule, CUSTOM_ELEMENTS_SCHEMA } from '@angular/core';
import { RouterModule } from '@angular/router';

import { ViewSharedModule } from 'app/shared';
import {
    IssueDifficultyComponent,
    IssueDifficultyDetailComponent,
    IssueDifficultyUpdateComponent,
    IssueDifficultyDeletePopupComponent,
    IssueDifficultyDeleteDialogComponent,
    issueDifficultyRoute,
    issueDifficultyPopupRoute
} from './';

const ENTITY_STATES = [...issueDifficultyRoute, ...issueDifficultyPopupRoute];

@NgModule({
    imports: [ViewSharedModule, RouterModule.forChild(ENTITY_STATES)],
    declarations: [
        IssueDifficultyComponent,
        IssueDifficultyDetailComponent,
        IssueDifficultyUpdateComponent,
        IssueDifficultyDeleteDialogComponent,
        IssueDifficultyDeletePopupComponent
    ],
    entryComponents: [
        IssueDifficultyComponent,
        IssueDifficultyUpdateComponent,
        IssueDifficultyDeleteDialogComponent,
        IssueDifficultyDeletePopupComponent
    ],
    schemas: [CUSTOM_ELEMENTS_SCHEMA]
})
export class ViewIssueDifficultyModule {}
