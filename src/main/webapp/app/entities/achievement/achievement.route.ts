import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, RouterStateSnapshot, Routes } from '@angular/router';
import { JhiPaginationUtil, JhiResolvePagingParams } from 'ng-jhipster';
import { UserRouteAccessService } from 'app/core';
import { Observable } from 'rxjs';
import { Achievement } from 'app/shared/model/achievement.model';
import { AchievementService } from './achievement.service';
import { AchievementComponent } from './achievement.component';
import { AchievementDetailComponent } from './achievement-detail.component';
import { AchievementUpdateComponent } from './achievement-update.component';
import { AchievementDeletePopupComponent } from './achievement-delete-dialog.component';
import { IAchievement } from 'app/shared/model/achievement.model';

@Injectable({ providedIn: 'root' })
export class AchievementResolve implements Resolve<IAchievement> {
    constructor(private service: AchievementService) {}

    resolve(route: ActivatedRouteSnapshot, state: RouterStateSnapshot) {
        const id = route.params['id'] ? route.params['id'] : null;
        if (id) {
            return this.service.find(id).map((achievement: HttpResponse<Achievement>) => achievement.body);
        }
        return Observable.of(new Achievement());
    }
}

export const achievementRoute: Routes = [
    {
        path: 'achievement',
        component: AchievementComponent,
        resolve: {
            pagingParams: JhiResolvePagingParams
        },
        data: {
            authorities: ['ROLE_USER'],
            defaultSort: 'id,asc',
            pageTitle: 'viewApp.achievement.home.title'
        },
        canActivate: [UserRouteAccessService]
    },
    {
        path: 'achievement/:id/view',
        component: AchievementDetailComponent,
        resolve: {
            achievement: AchievementResolve
        },
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'viewApp.achievement.home.title'
        },
        canActivate: [UserRouteAccessService]
    },
    {
        path: 'achievement/new',
        component: AchievementUpdateComponent,
        resolve: {
            achievement: AchievementResolve
        },
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'viewApp.achievement.home.title'
        },
        canActivate: [UserRouteAccessService]
    },
    {
        path: 'achievement/:id/edit',
        component: AchievementUpdateComponent,
        resolve: {
            achievement: AchievementResolve
        },
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'viewApp.achievement.home.title'
        },
        canActivate: [UserRouteAccessService]
    }
];

export const achievementPopupRoute: Routes = [
    {
        path: 'achievement/:id/delete',
        component: AchievementDeletePopupComponent,
        resolve: {
            achievement: AchievementResolve
        },
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'viewApp.achievement.home.title'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    }
];
