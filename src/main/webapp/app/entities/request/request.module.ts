import { NgModule, CUSTOM_ELEMENTS_SCHEMA } from "@angular/core";
import { ViewSharedModule } from 'app/shared';
import { RouterModule } from '@angular/router';
import { RequestComponent, requestRoute } from './';

const ENTITY_STATES = [...requestRoute];

@NgModule({
    imports: [ViewSharedModule, RouterModule.forChild(ENTITY_STATES)],
    declarations: [
        RequestComponent
    ],
    entryComponents: [
        RequestComponent
    ],
    schemas: [CUSTOM_ELEMENTS_SCHEMA]
})
export class ViewRequestModule {}