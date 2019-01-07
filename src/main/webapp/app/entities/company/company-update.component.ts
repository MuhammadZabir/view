import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { HttpResponse, HttpErrorResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import { JhiAlertService } from 'ng-jhipster';

import { ICompany } from 'app/shared/model/company.model';
import { CompanyService } from './company.service';
import { IPointConfig } from 'app/shared/model/point-config.model';
import { PointConfigService } from 'app/entities/point-config';

@Component({
    selector: 'jhi-company-update',
    templateUrl: './company-update.component.html'
})
export class CompanyUpdateComponent implements OnInit {
    private _company: ICompany;
    isSaving: boolean;

    pointconfigs: IPointConfig[];

    constructor(
        private jhiAlertService: JhiAlertService,
        private companyService: CompanyService,
        private pointConfigService: PointConfigService,
        private activatedRoute: ActivatedRoute
    ) {}

    ngOnInit() {
        this.isSaving = false;
        this.activatedRoute.data.subscribe(({ company }) => {
            this.company = company;
        });
        this.pointConfigService.query({ filter: 'company-is-null' }).subscribe(
            (res: HttpResponse<IPointConfig[]>) => {
                if (!this.company.pointConfig || !this.company.pointConfig.id) {
                    this.pointconfigs = res.body;
                } else {
                    this.pointConfigService.find(this.company.pointConfig.id).subscribe(
                        (subRes: HttpResponse<IPointConfig>) => {
                            this.pointconfigs = [subRes.body].concat(res.body);
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
        if (this.company.id !== undefined) {
            this.subscribeToSaveResponse(this.companyService.update(this.company));
        } else {
            this.subscribeToSaveResponse(this.companyService.create(this.company));
        }
    }

    private subscribeToSaveResponse(result: Observable<HttpResponse<ICompany>>) {
        result.subscribe((res: HttpResponse<ICompany>) => this.onSaveSuccess(), (res: HttpErrorResponse) => this.onSaveError());
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
    get company() {
        return this._company;
    }

    set company(company: ICompany) {
        this._company = company;
    }
}
