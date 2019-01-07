import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { IPermission } from 'app/shared/model/permission.model';

@Component({
    selector: 'jhi-permission-detail',
    templateUrl: './permission-detail.component.html'
})
export class PermissionDetailComponent implements OnInit {
    permission: IPermission;

    constructor(private activatedRoute: ActivatedRoute) {}

    ngOnInit() {
        this.activatedRoute.data.subscribe(({ permission }) => {
            this.permission = permission;
        });
    }

    previousState() {
        window.history.back();
    }
}
