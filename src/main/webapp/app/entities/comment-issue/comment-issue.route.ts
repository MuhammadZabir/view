import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, RouterStateSnapshot, Routes } from '@angular/router';
import { JhiPaginationUtil, JhiResolvePagingParams } from 'ng-jhipster';
import { UserRouteAccessService } from 'app/core';
import { Observable } from 'rxjs';
import { CommentIssue } from 'app/shared/model/comment-issue.model';
import { CommentIssueService } from './comment-issue.service';
import { CommentIssueComponent } from './comment-issue.component';
import { CommentIssueDetailComponent } from './comment-issue-detail.component';
import { CommentIssueUpdateComponent } from './comment-issue-update.component';
import { CommentIssueDeletePopupComponent } from './comment-issue-delete-dialog.component';
import { ICommentIssue } from 'app/shared/model/comment-issue.model';

@Injectable({ providedIn: 'root' })
export class CommentIssueResolve implements Resolve<ICommentIssue> {
    constructor(private service: CommentIssueService) {}

    resolve(route: ActivatedRouteSnapshot, state: RouterStateSnapshot) {
        const id = route.params['id'] ? route.params['id'] : null;
        if (id) {
            return this.service.find(id).map((commentIssue: HttpResponse<CommentIssue>) => commentIssue.body);
        }
        return Observable.of(new CommentIssue());
    }
}

export const commentIssueRoute: Routes = [
    {
        path: 'comment-issue',
        component: CommentIssueComponent,
        resolve: {
            pagingParams: JhiResolvePagingParams
        },
        data: {
            authorities: ['ROLE_USER'],
            defaultSort: 'id,asc',
            pageTitle: 'viewApp.commentIssue.home.title'
        },
        canActivate: [UserRouteAccessService]
    },
    {
        path: 'comment-issue/:id/view',
        component: CommentIssueDetailComponent,
        resolve: {
            commentIssue: CommentIssueResolve
        },
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'viewApp.commentIssue.home.title'
        },
        canActivate: [UserRouteAccessService]
    },
    {
        path: 'comment-issue/new',
        component: CommentIssueUpdateComponent,
        resolve: {
            commentIssue: CommentIssueResolve
        },
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'viewApp.commentIssue.home.title'
        },
        canActivate: [UserRouteAccessService]
    },
    {
        path: 'comment-issue/:id/edit',
        component: CommentIssueUpdateComponent,
        resolve: {
            commentIssue: CommentIssueResolve
        },
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'viewApp.commentIssue.home.title'
        },
        canActivate: [UserRouteAccessService]
    }
];

export const commentIssuePopupRoute: Routes = [
    {
        path: 'comment-issue/:id/delete',
        component: CommentIssueDeletePopupComponent,
        resolve: {
            commentIssue: CommentIssueResolve
        },
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'viewApp.commentIssue.home.title'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    }
];
