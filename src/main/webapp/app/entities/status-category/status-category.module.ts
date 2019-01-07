import { NgModule, CUSTOM_ELEMENTS_SCHEMA } from '@angular/core';
import { RouterModule } from '@angular/router';

import { ViewSharedModule } from 'app/shared';
import {
    StatusCategoryComponent,
    StatusCategoryDetailComponent,
    StatusCategoryUpdateComponent,
    StatusCategoryDeletePopupComponent,
    StatusCategoryDeleteDialogComponent,
    statusCategoryRoute,
    statusCategoryPopupRoute
} from './';

const ENTITY_STATES = [...statusCategoryRoute, ...statusCategoryPopupRoute];

@NgModule({
    imports: [ViewSharedModule, RouterModule.forChild(ENTITY_STATES)],
    declarations: [
        StatusCategoryComponent,
        StatusCategoryDetailComponent,
        StatusCategoryUpdateComponent,
        StatusCategoryDeleteDialogComponent,
        StatusCategoryDeletePopupComponent
    ],
    entryComponents: [
        StatusCategoryComponent,
        StatusCategoryUpdateComponent,
        StatusCategoryDeleteDialogComponent,
        StatusCategoryDeletePopupComponent
    ],
    schemas: [CUSTOM_ELEMENTS_SCHEMA]
})
export class ViewStatusCategoryModule {}
