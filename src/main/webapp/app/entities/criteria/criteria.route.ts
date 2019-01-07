import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, RouterStateSnapshot, Routes } from '@angular/router';
import { JhiPaginationUtil, JhiResolvePagingParams } from 'ng-jhipster';
import { UserRouteAccessService } from 'app/core';
import { Observable } from 'rxjs';
import { Criteria } from 'app/shared/model/criteria.model';
import { CriteriaService } from './criteria.service';
import { CriteriaComponent } from './criteria.component';
import { CriteriaDetailComponent } from './criteria-detail.component';
import { CriteriaUpdateComponent } from './criteria-update.component';
import { CriteriaDeletePopupComponent } from './criteria-delete-dialog.component';
import { ICriteria } from 'app/shared/model/criteria.model';

@Injectable({ providedIn: 'root' })
export class CriteriaResolve implements Resolve<ICriteria> {
    constructor(private service: CriteriaService) {}

    resolve(route: ActivatedRouteSnapshot, state: RouterStateSnapshot) {
        const id = route.params['id'] ? route.params['id'] : null;
        if (id) {
            return this.service.find(id).map((criteria: HttpResponse<Criteria>) => criteria.body);
        }
        return Observable.of(new Criteria());
    }
}

export const criteriaRoute: Routes = [
    {
        path: 'criteria',
        component: CriteriaComponent,
        resolve: {
            pagingParams: JhiResolvePagingParams
        },
        data: {
            authorities: ['ROLE_USER'],
            defaultSort: 'id,asc',
            pageTitle: 'viewApp.criteria.home.title'
        },
        canActivate: [UserRouteAccessService]
    },
    {
        path: 'criteria/:id/view',
        component: CriteriaDetailComponent,
        resolve: {
            criteria: CriteriaResolve
        },
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'viewApp.criteria.home.title'
        },
        canActivate: [UserRouteAccessService]
    },
    {
        path: 'criteria/new',
        component: CriteriaUpdateComponent,
        resolve: {
            criteria: CriteriaResolve
        },
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'viewApp.criteria.home.title'
        },
        canActivate: [UserRouteAccessService]
    },
    {
        path: 'criteria/:id/edit',
        component: CriteriaUpdateComponent,
        resolve: {
            criteria: CriteriaResolve
        },
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'viewApp.criteria.home.title'
        },
        canActivate: [UserRouteAccessService]
    }
];

export const criteriaPopupRoute: Routes = [
    {
        path: 'criteria/:id/delete',
        component: CriteriaDeletePopupComponent,
        resolve: {
            criteria: CriteriaResolve
        },
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'viewApp.criteria.home.title'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    }
];
