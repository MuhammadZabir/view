import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, RouterStateSnapshot, Routes } from '@angular/router';
import { JhiPaginationUtil, JhiResolvePagingParams } from 'ng-jhipster';
import { UserRouteAccessService } from 'app/core';
import { Observable } from 'rxjs';
import { PointConfig } from 'app/shared/model/point-config.model';
import { PointConfigService } from './point-config.service';
import { PointConfigComponent } from './point-config.component';
import { PointConfigDetailComponent } from './point-config-detail.component';
import { PointConfigUpdateComponent } from './point-config-update.component';
import { PointConfigDeletePopupComponent } from './point-config-delete-dialog.component';
import { IPointConfig } from 'app/shared/model/point-config.model';

@Injectable({ providedIn: 'root' })
export class PointConfigResolve implements Resolve<IPointConfig> {
    constructor(private service: PointConfigService) {}

    resolve(route: ActivatedRouteSnapshot, state: RouterStateSnapshot) {
        const id = route.params['id'] ? route.params['id'] : null;
        if (id) {
            return this.service.find(id).map((pointConfig: HttpResponse<PointConfig>) => pointConfig.body);
        }
        return Observable.of(new PointConfig());
    }
}

export const pointConfigRoute: Routes = [
    {
        path: 'point-config',
        component: PointConfigComponent,
        resolve: {
            pagingParams: JhiResolvePagingParams
        },
        data: {
            authorities: ['ROLE_USER'],
            defaultSort: 'id,asc',
            pageTitle: 'viewApp.pointConfig.home.title'
        },
        canActivate: [UserRouteAccessService]
    },
    {
        path: 'point-config/:id/view',
        component: PointConfigDetailComponent,
        resolve: {
            pointConfig: PointConfigResolve
        },
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'viewApp.pointConfig.home.title'
        },
        canActivate: [UserRouteAccessService]
    },
    {
        path: 'point-config/new',
        component: PointConfigUpdateComponent,
        resolve: {
            pointConfig: PointConfigResolve
        },
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'viewApp.pointConfig.home.title'
        },
        canActivate: [UserRouteAccessService]
    },
    {
        path: 'point-config/:id/edit',
        component: PointConfigUpdateComponent,
        resolve: {
            pointConfig: PointConfigResolve
        },
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'viewApp.pointConfig.home.title'
        },
        canActivate: [UserRouteAccessService]
    }
];

export const pointConfigPopupRoute: Routes = [
    {
        path: 'point-config/:id/delete',
        component: PointConfigDeletePopupComponent,
        resolve: {
            pointConfig: PointConfigResolve
        },
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'viewApp.pointConfig.home.title'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    }
];
