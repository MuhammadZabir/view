import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { HttpResponse, HttpErrorResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import { JhiAlertService } from 'ng-jhipster';

import { IIssueDifficulty } from 'app/shared/model/issue-difficulty.model';
import { IssueDifficultyService } from './issue-difficulty.service';
import { IIssue } from 'app/shared/model/issue.model';
import { IssueService } from 'app/entities/issue';
import { IPointConfig } from 'app/shared/model/point-config.model';
import { PointConfigService } from 'app/entities/point-config';

@Component({
    selector: 'jhi-issue-difficulty-update',
    templateUrl: './issue-difficulty-update.component.html'
})
export class IssueDifficultyUpdateComponent implements OnInit {
    private _issueDifficulty: IIssueDifficulty;
    isSaving: boolean;

    issues: IIssue[];

    pointconfigs: IPointConfig[];

    constructor(
        private jhiAlertService: JhiAlertService,
        private issueDifficultyService: IssueDifficultyService,
        private issueService: IssueService,
        private pointConfigService: PointConfigService,
        private activatedRoute: ActivatedRoute
    ) {}

    ngOnInit() {
        this.isSaving = false;
        this.activatedRoute.data.subscribe(({ issueDifficulty }) => {
            this.issueDifficulty = issueDifficulty;
        });
        this.issueService.query({ filter: 'issuedifficulty-is-null' }).subscribe(
            (res: HttpResponse<IIssue[]>) => {
                if (!this.issueDifficulty.issue || !this.issueDifficulty.issue.id) {
                    this.issues = res.body;
                } else {
                    this.issueService.find(this.issueDifficulty.issue.id).subscribe(
                        (subRes: HttpResponse<IIssue>) => {
                            this.issues = [subRes.body].concat(res.body);
                        },
                        (subRes: HttpErrorResponse) => this.onError(subRes.message)
                    );
                }
            },
            (res: HttpErrorResponse) => this.onError(res.message)
        );
        this.pointConfigService.query().subscribe(
            (res: HttpResponse<IPointConfig[]>) => {
                this.pointconfigs = res.body;
            },
            (res: HttpErrorResponse) => this.onError(res.message)
        );
    }

    previousState() {
        window.history.back();
    }

    save() {
        this.isSaving = true;
        if (this.issueDifficulty.id !== undefined) {
            this.subscribeToSaveResponse(this.issueDifficultyService.update(this.issueDifficulty));
        } else {
            this.subscribeToSaveResponse(this.issueDifficultyService.create(this.issueDifficulty));
        }
    }

    private subscribeToSaveResponse(result: Observable<HttpResponse<IIssueDifficulty>>) {
        result.subscribe((res: HttpResponse<IIssueDifficulty>) => this.onSaveSuccess(), (res: HttpErrorResponse) => this.onSaveError());
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

    trackIssueById(index: number, item: IIssue) {
        return item.id;
    }

    trackPointConfigById(index: number, item: IPointConfig) {
        return item.id;
    }
    get issueDifficulty() {
        return this._issueDifficulty;
    }

    set issueDifficulty(issueDifficulty: IIssueDifficulty) {
        this._issueDifficulty = issueDifficulty;
    }
}
