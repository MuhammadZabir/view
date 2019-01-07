import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { HttpResponse, HttpErrorResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import * as moment from 'moment';
import { DATE_TIME_FORMAT } from 'app/shared/constants/input.constants';
import { JhiAlertService } from 'ng-jhipster';

import { IIssue } from 'app/shared/model/issue.model';
import { IssueService } from './issue.service';
import { IIssueDifficulty } from 'app/shared/model/issue-difficulty.model';
import { IssueDifficultyService } from 'app/entities/issue-difficulty';
import { IUser, UserService } from 'app/core';

@Component({
    selector: 'jhi-issue-update',
    templateUrl: './issue-update.component.html'
})
export class IssueUpdateComponent implements OnInit {
    private _issue: IIssue;
    isSaving: boolean;

    issuedifficulties: IIssueDifficulty[];

    users: IUser[];
    durationStart: string;
    expectedDurationEnd: string;
    durationEnd: string;

    constructor(
        private jhiAlertService: JhiAlertService,
        private issueService: IssueService,
        private issueDifficultyService: IssueDifficultyService,
        private userService: UserService,
        private activatedRoute: ActivatedRoute
    ) {}

    ngOnInit() {
        this.isSaving = false;
        this.activatedRoute.data.subscribe(({ issue }) => {
            this.issue = issue;
        });
        this.issueDifficultyService.query({ filter: 'issue-is-null' }).subscribe(
            (res: HttpResponse<IIssueDifficulty[]>) => {
                if (!this.issue.issueDifficulty || !this.issue.issueDifficulty.id) {
                    this.issuedifficulties = res.body;
                } else {
                    this.issueDifficultyService.find(this.issue.issueDifficulty.id).subscribe(
                        (subRes: HttpResponse<IIssueDifficulty>) => {
                            this.issuedifficulties = [subRes.body].concat(res.body);
                        },
                        (subRes: HttpErrorResponse) => this.onError(subRes.message)
                    );
                }
            },
            (res: HttpErrorResponse) => this.onError(res.message)
        );
        this.userService.query().subscribe(
            (res: HttpResponse<IUser[]>) => {
                this.users = res.body;
            },
            (res: HttpErrorResponse) => this.onError(res.message)
        );
    }

    previousState() {
        window.history.back();
    }

    save() {
        this.isSaving = true;
        this.issue.durationStart = moment(this.durationStart, DATE_TIME_FORMAT);
        this.issue.expectedDurationEnd = moment(this.expectedDurationEnd, DATE_TIME_FORMAT);
        this.issue.durationEnd = moment(this.durationEnd, DATE_TIME_FORMAT);
        if (this.issue.id !== undefined) {
            this.subscribeToSaveResponse(this.issueService.update(this.issue));
        } else {
            this.subscribeToSaveResponse(this.issueService.create(this.issue));
        }
    }

    private subscribeToSaveResponse(result: Observable<HttpResponse<IIssue>>) {
        result.subscribe((res: HttpResponse<IIssue>) => this.onSaveSuccess(), (res: HttpErrorResponse) => this.onSaveError());
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

    trackIssueDifficultyById(index: number, item: IIssueDifficulty) {
        return item.id;
    }

    trackUserById(index: number, item: IUser) {
        return item.id;
    }
    get issue() {
        return this._issue;
    }

    set issue(issue: IIssue) {
        this._issue = issue;
        this.durationStart = moment(issue.durationStart).format(DATE_TIME_FORMAT);
        this.expectedDurationEnd = moment(issue.expectedDurationEnd).format(DATE_TIME_FORMAT);
        this.durationEnd = moment(issue.durationEnd).format(DATE_TIME_FORMAT);
    }
}
