import { NgModule, CUSTOM_ELEMENTS_SCHEMA } from '@angular/core';
import { RouterModule } from '@angular/router';

import { ViewSharedModule } from 'app/shared';
import {
    CriteriaComponent,
    CriteriaDetailComponent,
    CriteriaUpdateComponent,
    CriteriaDeletePopupComponent,
    CriteriaDeleteDialogComponent,
    criteriaRoute,
    criteriaPopupRoute
} from './';

const ENTITY_STATES = [...criteriaRoute, ...criteriaPopupRoute];

@NgModule({
    imports: [ViewSharedModule, RouterModule.forChild(ENTITY_STATES)],
    declarations: [
        CriteriaComponent,
        CriteriaDetailComponent,
        CriteriaUpdateComponent,
        CriteriaDeleteDialogComponent,
        CriteriaDeletePopupComponent
    ],
    entryComponents: [CriteriaComponent, CriteriaUpdateComponent, CriteriaDeleteDialogComponent, CriteriaDeletePopupComponent],
    schemas: [CUSTOM_ELEMENTS_SCHEMA]
})
export class ViewCriteriaModule {}
