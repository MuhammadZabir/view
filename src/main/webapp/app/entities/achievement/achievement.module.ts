import { NgModule, CUSTOM_ELEMENTS_SCHEMA } from '@angular/core';
import { RouterModule } from '@angular/router';

import { ViewSharedModule } from 'app/shared';
import { ViewAdminModule } from 'app/admin/admin.module';
import {
    AchievementComponent,
    AchievementDetailComponent,
    AchievementUpdateComponent,
    AchievementDeletePopupComponent,
    AchievementDeleteDialogComponent,
    achievementRoute,
    achievementPopupRoute
} from './';

const ENTITY_STATES = [...achievementRoute, ...achievementPopupRoute];

@NgModule({
    imports: [ViewSharedModule, ViewAdminModule, RouterModule.forChild(ENTITY_STATES)],
    declarations: [
        AchievementComponent,
        AchievementDetailComponent,
        AchievementUpdateComponent,
        AchievementDeleteDialogComponent,
        AchievementDeletePopupComponent
    ],
    entryComponents: [AchievementComponent, AchievementUpdateComponent, AchievementDeleteDialogComponent, AchievementDeletePopupComponent],
    schemas: [CUSTOM_ELEMENTS_SCHEMA]
})
export class ViewAchievementModule {}
