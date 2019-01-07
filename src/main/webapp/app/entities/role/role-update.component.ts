import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { HttpResponse, HttpErrorResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import { JhiAlertService } from 'ng-jhipster';

import { IRole } from 'app/shared/model/role.model';
import { RoleService } from './role.service';
import { IPermission } from 'app/shared/model/permission.model';
import { PermissionService } from 'app/entities/permission';

@Component({
    selector: 'jhi-role-update',
    templateUrl: './role-update.component.html'
})
export class RoleUpdateComponent implements OnInit {
    private _role: IRole;
    isSaving: boolean;

    permissions: IPermission[];

    constructor(
        private jhiAlertService: JhiAlertService,
        private roleService: RoleService,
        private permissionService: PermissionService,
        private activatedRoute: ActivatedRoute
    ) {}

    ngOnInit() {
        this.isSaving = false;
        this.activatedRoute.data.subscribe(({ role }) => {
            this.role = role;
        });
        this.permissionService.query().subscribe(
            (res: HttpResponse<IPermission[]>) => {
                this.permissions = res.body;
            },
            (res: HttpErrorResponse) => this.onError(res.message)
        );
    }

    previousState() {
        window.history.back();
    }

    save() {
        this.isSaving = true;
        if (this.role.id !== undefined) {
            this.subscribeToSaveResponse(this.roleService.update(this.role));
        } else {
            this.subscribeToSaveResponse(this.roleService.create(this.role));
        }
    }

    private subscribeToSaveResponse(result: Observable<HttpResponse<IRole>>) {
        result.subscribe((res: HttpResponse<IRole>) => this.onSaveSuccess(), (res: HttpErrorResponse) => this.onSaveError());
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

    trackPermissionById(index: number, item: IPermission) {
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
    get role() {
        return this._role;
    }

    set role(role: IRole) {
        this._role = role;
    }
}
