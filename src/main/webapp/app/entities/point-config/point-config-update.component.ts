import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { HttpResponse, HttpErrorResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import * as moment from 'moment';
import { DATE_TIME_FORMAT } from 'app/shared/constants/input.constants';
import { JhiAlertService } from 'ng-jhipster';

import { IPointConfig } from 'app/shared/model/point-config.model';
import { PointConfigService } from './point-config.service';
import { ICompany } from 'app/shared/model/company.model';
import { CompanyService } from 'app/entities/company';

@Component({
    selector: 'jhi-point-config-update',
    templateUrl: './point-config-update.component.html'
})
export class PointConfigUpdateComponent implements OnInit {
    private _pointConfig: IPointConfig;
    isSaving: boolean;

    companies: ICompany[];
    startDate: string;
    endDate: string;

    constructor(
        private jhiAlertService: JhiAlertService,
        private pointConfigService: PointConfigService,
        private companyService: CompanyService,
        private activatedRoute: ActivatedRoute
    ) {}

    ngOnInit() {
        this.isSaving = false;
        this.activatedRoute.data.subscribe(({ pointConfig }) => {
            this.pointConfig = pointConfig;
        });
        this.companyService.query({ filter: 'pointconfig-is-null' }).subscribe(
            (res: HttpResponse<ICompany[]>) => {
                if (!this.pointConfig.company || !this.pointConfig.company.id) {
                    this.companies = res.body;
                } else {
                    this.companyService.find(this.pointConfig.company.id).subscribe(
                        (subRes: HttpResponse<ICompany>) => {
                            this.companies = [subRes.body].concat(res.body);
                        },
                        (subRes: HttpErrorResponse) => this.onError(subRes.message)
                    );
                }
            },
            (res: HttpErrorResponse) => this.onError(res.message)
        );
    }

    previousState() {
        window.history.back();
    }

    save() {
        this.isSaving = true;
        this.pointConfig.startDate = moment(this.startDate, DATE_TIME_FORMAT);
        this.pointConfig.endDate = moment(this.endDate, DATE_TIME_FORMAT);
        if (this.pointConfig.id !== undefined) {
            this.subscribeToSaveResponse(this.pointConfigService.update(this.pointConfig));
        } else {
            this.subscribeToSaveResponse(this.pointConfigService.create(this.pointConfig));
        }
    }

    private subscribeToSaveResponse(result: Observable<HttpResponse<IPointConfig>>) {
        result.subscribe((res: HttpResponse<IPointConfig>) => this.onSaveSuccess(), (res: HttpErrorResponse) => this.onSaveError());
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

    trackCompanyById(index: number, item: ICompany) {
        return item.id;
    }
    get pointConfig() {
        return this._pointConfig;
    }

    set pointConfig(pointConfig: IPointConfig) {
        this._pointConfig = pointConfig;
        this.startDate = moment(pointConfig.startDate).format(DATE_TIME_FORMAT);
        this.endDate = moment(pointConfig.endDate).format(DATE_TIME_FORMAT);
    }
}
