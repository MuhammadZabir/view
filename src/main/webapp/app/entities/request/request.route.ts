import { RequestComponent } from './';
import { UserRouteAccessService } from '../../core';
import { Routes } from '@angular/router';

export const requestRoute: Routes = [
    {
        path: 'request',
        component: RequestComponent,
        data: {
            authorities: ['ROLE_USER'],
            defaultSort: 'id,asc',
            pageTitle: 'viewApp.pointConfig.home.title'
        },
        canActivate: [UserRouteAccessService]
    }
];