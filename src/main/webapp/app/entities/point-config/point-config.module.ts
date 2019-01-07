import { NgModule, CUSTOM_ELEMENTS_SCHEMA } from '@angular/core';
import { RouterModule } from '@angular/router';

import { ViewSharedModule } from 'app/shared';
import {
    PointConfigComponent,
    PointConfigDetailComponent,
    PointConfigUpdateComponent,
    PointConfigDeletePopupComponent,
    PointConfigDeleteDialogComponent,
    pointConfigRoute,
    pointConfigPopupRoute
} from './';

const ENTITY_STATES = [...pointConfigRoute, ...pointConfigPopupRoute];

@NgModule({
    imports: [ViewSharedModule, RouterModule.forChild(ENTITY_STATES)],
    declarations: [
        PointConfigComponent,
        PointConfigDetailComponent,
        PointConfigUpdateComponent,
        PointConfigDeleteDialogComponent,
        PointConfigDeletePopupComponent
    ],
    entryComponents: [PointConfigComponent, PointConfigUpdateComponent, PointConfigDeleteDialogComponent, PointConfigDeletePopupComponent],
    schemas: [CUSTOM_ELEMENTS_SCHEMA]
})
export class ViewPointConfigModule {}
