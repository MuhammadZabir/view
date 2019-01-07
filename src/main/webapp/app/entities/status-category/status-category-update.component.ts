import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { HttpResponse, HttpErrorResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import { JhiAlertService } from 'ng-jhipster';

import { IStatusCategory } from 'app/shared/model/status-category.model';
import { StatusCategoryService } from './status-category.service';
import { IPointConfig } from 'app/shared/model/point-config.model';
import { PointConfigService } from 'app/entities/point-config';

@Component({
    selector: 'jhi-status-category-update',
    templateUrl: './status-category-update.component.html'
})
export class StatusCategoryUpdateComponent implements OnInit {
    private _statusCategory: IStatusCategory;
    isSaving: boolean;

    pointconfigs: IPointConfig[];

    constructor(
        private jhiAlertService: JhiAlertService,
        private statusCategoryService: StatusCategoryService,
        private pointConfigService: PointConfigService,
        private activatedRoute: ActivatedRoute
    ) {}

    ngOnInit() {
        this.isSaving = false;
        this.activatedRoute.data.subscribe(({ statusCategory }) => {
            this.statusCategory = statusCategory;
        });
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
        if (this.statusCategory.id !== undefined) {
            this.subscribeToSaveResponse(this.statusCategoryService.update(this.statusCategory));
        } else {
            this.subscribeToSaveResponse(this.statusCategoryService.create(this.statusCategory));
        }
    }

    private subscribeToSaveResponse(result: Observable<HttpResponse<IStatusCategory>>) {
        result.subscribe((res: HttpResponse<IStatusCategory>) => this.onSaveSuccess(), (res: HttpErrorResponse) => this.onSaveError());
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

    trackPointConfigById(index: number, item: IPointConfig) {
        return item.id;
    }
    get statusCategory() {
        return this._statusCategory;
    }

    set statusCategory(statusCategory: IStatusCategory) {
        this._statusCategory = statusCategory;
    }
}
