import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { HttpResponse, HttpErrorResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import { JhiAlertService } from 'ng-jhipster';

import { IAchievement } from 'app/shared/model/achievement.model';
import { AchievementService } from './achievement.service';
import { IUser, UserService } from 'app/core';
import { IDepartment } from 'app/shared/model/department.model';
import { DepartmentService } from 'app/entities/department';
import { ICriteria } from 'app/shared/model/criteria.model';
import { CriteriaService } from 'app/entities/criteria';

@Component({
    selector: 'jhi-achievement-update',
    templateUrl: './achievement-update.component.html'
})
export class AchievementUpdateComponent implements OnInit {
    private _achievement: IAchievement;
    isSaving: boolean;

    users: IUser[];

    departments: IDepartment[];

    criteria: ICriteria[];

    constructor(
        private jhiAlertService: JhiAlertService,
        private achievementService: AchievementService,
        private userService: UserService,
        private departmentService: DepartmentService,
        private criteriaService: CriteriaService,
        private activatedRoute: ActivatedRoute
    ) {}

    ngOnInit() {
        this.isSaving = false;
        this.activatedRoute.data.subscribe(({ achievement }) => {
            this.achievement = achievement;
        });
        this.userService.query().subscribe(
            (res: HttpResponse<IUser[]>) => {
                this.users = res.body;
            },
            (res: HttpErrorResponse) => this.onError(res.message)
        );
        this.departmentService.query({ filter: 'achievement-is-null' }).subscribe(
            (res: HttpResponse<IDepartment[]>) => {
                if (!this.achievement.department || !this.achievement.department.id) {
                    this.departments = res.body;
                } else {
                    this.departmentService.find(this.achievement.department.id).subscribe(
                        (subRes: HttpResponse<IDepartment>) => {
                            this.departments = [subRes.body].concat(res.body);
                        },
                        (subRes: HttpErrorResponse) => this.onError(subRes.message)
                    );
                }
            },
            (res: HttpErrorResponse) => this.onError(res.message)
        );
        this.criteriaService.query({ filter: 'achievement-is-null' }).subscribe(
            (res: HttpResponse<ICriteria[]>) => {
                if (!this.achievement.criteria || !this.achievement.criteria.id) {
                    this.criteria = res.body;
                } else {
                    this.criteriaService.find(this.achievement.criteria.id).subscribe(
                        (subRes: HttpResponse<ICriteria>) => {
                            this.criteria = [subRes.body].concat(res.body);
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
        if (this.achievement.id !== undefined) {
            this.subscribeToSaveResponse(this.achievementService.update(this.achievement));
        } else {
            this.subscribeToSaveResponse(this.achievementService.create(this.achievement));
        }
    }

    private subscribeToSaveResponse(result: Observable<HttpResponse<IAchievement>>) {
        result.subscribe((res: HttpResponse<IAchievement>) => this.onSaveSuccess(), (res: HttpErrorResponse) => this.onSaveError());
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

    trackDepartmentById(index: number, item: IDepartment) {
        return item.id;
    }

    trackCriteriaById(index: number, item: ICriteria) {
        return item.id;
    }
    get achievement() {
        return this._achievement;
    }

    set achievement(achievement: IAchievement) {
        this._achievement = achievement;
    }
}
