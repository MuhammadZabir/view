import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, RouterStateSnapshot, Routes } from '@angular/router';
import { JhiPaginationUtil, JhiResolvePagingParams } from 'ng-jhipster';
import { UserRouteAccessService } from 'app/core';
import { Observable } from 'rxjs';
import { IssueDifficulty } from 'app/shared/model/issue-difficulty.model';
import { IssueDifficultyService } from './issue-difficulty.service';
import { IssueDifficultyComponent } from './issue-difficulty.component';
import { IssueDifficultyDetailComponent } from './issue-difficulty-detail.component';
import { IssueDifficultyUpdateComponent } from './issue-difficulty-update.component';
import { IssueDifficultyDeletePopupComponent } from './issue-difficulty-delete-dialog.component';
import { IIssueDifficulty } from 'app/shared/model/issue-difficulty.model';

@Injectable({ providedIn: 'root' })
export class IssueDifficultyResolve implements Resolve<IIssueDifficulty> {
    constructor(private service: IssueDifficultyService) {}

    resolve(route: ActivatedRouteSnapshot, state: RouterStateSnapshot) {
        const id = route.params['id'] ? route.params['id'] : null;
        if (id) {
            return this.service.find(id).map((issueDifficulty: HttpResponse<IssueDifficulty>) => issueDifficulty.body);
        }
        return Observable.of(new IssueDifficulty());
    }
}

export const issueDifficultyRoute: Routes = [
    {
        path: 'issue-difficulty',
        component: IssueDifficultyComponent,
        resolve: {
            pagingParams: JhiResolvePagingParams
        },
        data: {
            authorities: ['ROLE_USER'],
            defaultSort: 'id,asc',
            pageTitle: 'viewApp.issueDifficulty.home.title'
        },
        canActivate: [UserRouteAccessService]
    },
    {
        path: 'issue-difficulty/:id/view',
        component: IssueDifficultyDetailComponent,
        resolve: {
            issueDifficulty: IssueDifficultyResolve
        },
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'viewApp.issueDifficulty.home.title'
        },
        canActivate: [UserRouteAccessService]
    },
    {
        path: 'issue-difficulty/new',
        component: IssueDifficultyUpdateComponent,
        resolve: {
            issueDifficulty: IssueDifficultyResolve
        },
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'viewApp.issueDifficulty.home.title'
        },
        canActivate: [UserRouteAccessService]
    },
    {
        path: 'issue-difficulty/:id/edit',
        component: IssueDifficultyUpdateComponent,
        resolve: {
            issueDifficulty: IssueDifficultyResolve
        },
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'viewApp.issueDifficulty.home.title'
        },
        canActivate: [UserRouteAccessService]
    }
];

export const issueDifficultyPopupRoute: Routes = [
    {
        path: 'issue-difficulty/:id/delete',
        component: IssueDifficultyDeletePopupComponent,
        resolve: {
            issueDifficulty: IssueDifficultyResolve
        },
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'viewApp.issueDifficulty.home.title'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    }
];
