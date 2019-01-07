import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { HttpResponse, HttpErrorResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import { JhiAlertService } from 'ng-jhipster';

import { ICriteria } from 'app/shared/model/criteria.model';
import { CriteriaService } from './criteria.service';

@Component({
    selector: 'jhi-criteria-update',
    templateUrl: './criteria-update.component.html'
})
export class CriteriaUpdateComponent implements OnInit {
    private _criteria: ICriteria;
    isSaving: boolean;

    criteriaCollection: ICriteria[];

    constructor(
        private jhiAlertService: JhiAlertService,
        private criteriaService: CriteriaService,
        private activatedRoute: ActivatedRoute
    ) {}

    ngOnInit() {
        this.isSaving = false;
        this.activatedRoute.data.subscribe(({ criteria }) => {
            this.criteria = criteria;
        });
        this.criteriaService.query().subscribe(
            (res: HttpResponse<ICriteria[]>) => {
                this.criteriaCollection = res.body;
            },
            (res: HttpErrorResponse) => this.onError(res.message)
        );
    }

    previousState() {
        window.history.back();
    }

    save() {
        this.isSaving = true;
        if (this.criteria.id !== undefined) {
            this.subscribeToSaveResponse(this.criteriaService.update(this.criteria));
        } else {
            this.subscribeToSaveResponse(this.criteriaService.create(this.criteria));
        }
    }

    private subscribeToSaveResponse(result: Observable<HttpResponse<ICriteria>>) {
        result.subscribe((res: HttpResponse<ICriteria>) => this.onSaveSuccess(), (res: HttpErrorResponse) => this.onSaveError());
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

    trackCriteriaById(index: number, item: ICriteria) {
        return item.id;
    }

    getSelected(selectedVals: Array<any>, option: any) {
        if (selectedVals) {
            for (let i = 0; i < selectedVals.length; i++) {
                if (option.id === selectedVals[i].id) {
                    return selectedVals[i];
                }
            }
        }
        return option;
    }
    get criteria() {
        return this._criteria;
    }

    set criteria(criteria: ICriteria) {
        this._criteria = criteria;
    }
}
