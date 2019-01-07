import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { HttpResponse, HttpErrorResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import { JhiAlertService } from 'ng-jhipster';

import { ICommentIssue } from 'app/shared/model/comment-issue.model';
import { CommentIssueService } from './comment-issue.service';
import { IUser, UserService } from 'app/core';
import { IIssue } from 'app/shared/model/issue.model';
import { IssueService } from 'app/entities/issue';

@Component({
    selector: 'jhi-comment-issue-update',
    templateUrl: './comment-issue-update.component.html'
})
export class CommentIssueUpdateComponent implements OnInit {
    private _commentIssue: ICommentIssue;
    isSaving: boolean;

    users: IUser[];

    issues: IIssue[];

    constructor(
        private jhiAlertService: JhiAlertService,
        private commentIssueService: CommentIssueService,
        private userService: UserService,
        private issueService: IssueService,
        private activatedRoute: ActivatedRoute
    ) {}

    ngOnInit() {
        this.isSaving = false;
        this.activatedRoute.data.subscribe(({ commentIssue }) => {
            this.commentIssue = commentIssue;
        });
        this.userService.query().subscribe(
            (res: HttpResponse<IUser[]>) => {
                this.users = res.body;
            },
            (res: HttpErrorResponse) => this.onError(res.message)
        );
        this.issueService.query().subscribe(
            (res: HttpResponse<IIssue[]>) => {
                this.issues = res.body;
            },
            (res: HttpErrorResponse) => this.onError(res.message)
        );
    }

    previousState() {
        window.history.back();
    }

    save() {
        this.isSaving = true;
        if (this.commentIssue.id !== undefined) {
            this.subscribeToSaveResponse(this.commentIssueService.update(this.commentIssue));
        } else {
            this.subscribeToSaveResponse(this.commentIssueService.create(this.commentIssue));
        }
    }

    private subscribeToSaveResponse(result: Observable<HttpResponse<ICommentIssue>>) {
        result.subscribe((res: HttpResponse<ICommentIssue>) => this.onSaveSuccess(), (res: HttpErrorResponse) => this.onSaveError());
    }

    private onSaveSuccess() {
        this.isSaving = false;
        this.previousState();
    }

    private onSaveError() {
        this.isSaving = false;
    }

    private onError(errorMessage: string) {
        this.jhiAlertService.error(errorMessage, null, null);
    }

    trackUserById(index: number, item: IUser) {
        return item.id;
    }

    trackIssueById(index: number, item: IIssue) {
        return item.id;
    }
    get commentIssue() {
        return this._commentIssue;
    }

    set commentIssue(commentIssue: ICommentIssue) {
        this._commentIssue = commentIssue;
    }
}
