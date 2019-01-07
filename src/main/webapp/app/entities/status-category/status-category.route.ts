import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, RouterStateSnapshot, Routes } from '@angular/router';
import { JhiPaginationUtil, JhiResolvePagingParams } from 'ng-jhipster';
import { UserRouteAccessService } from 'app/core';
import { Observable } from 'rxjs';
import { StatusCategory } from 'app/shared/model/status-category.model';
import { StatusCategoryService } from './status-category.service';
import { StatusCategoryComponent } from './status-category.component';
import { StatusCategoryDetailComponent } from './status-category-detail.component';
import { StatusCategoryUpdateComponent } from './status-category-update.component';
import { StatusCategoryDeletePopupComponent } from './status-category-delete-dialog.component';
import { IStatusCategory } from 'app/shared/model/status-category.model';

@Injectable({ providedIn: 'root' })
export class StatusCategoryResolve implements Resolve<IStatusCategory> {
    constructor(private service: StatusCategoryService) {}

    resolve(route: ActivatedRouteSnapshot, state: RouterStateSnapshot) {
        const id = route.params['id'] ? route.params['id'] : null;
        if (id) {
            return this.service.find(id).map((statusCategory: HttpResponse<StatusCategory>) => statusCategory.body);
        }
        return Observable.of(new StatusCategory());
    }
}

export const statusCategoryRoute: Routes = [
    {
        path: 'status-category',
        component: StatusCategoryComponent,
        resolve: {
            pagingParams: JhiResolvePagingParams
        },
        data: {
            authorities: ['ROLE_USER'],
            defaultSort: 'id,asc',
            pageTitle: 'viewApp.statusCategory.home.title'
        },
        canActivate: [UserRouteAccessService]
    },
    {
        path: 'status-category/:id/view',
        component: StatusCategoryDetailComponent,
        resolve: {
            statusCategory: StatusCategoryResolve
        },
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'viewApp.statusCategory.home.title'
        },
        canActivate: [UserRouteAccessService]
    },
    {
        path: 'status-category/new',
        component: StatusCategoryUpdateComponent,
        resolve: {
            statusCategory: StatusCategoryResolve
        },
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'viewApp.statusCategory.home.title'
        },
        canActivate: [UserRouteAccessService]
    },
    {
        path: 'status-category/:id/edit',
        component: StatusCategoryUpdateComponent,
        resolve: {
            statusCategory: StatusCategoryResolve
        },
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'viewApp.statusCategory.home.title'
        },
        canActivate: [UserRouteAccessService]
    }
];

export const statusCategoryPopupRoute: Routes = [
    {
        path: 'status-category/:id/delete',
        component: StatusCategoryDeletePopupComponent,
        resolve: {
            statusCategory: StatusCategoryResolve
        },
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'viewApp.statusCategory.home.title'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    }
];
