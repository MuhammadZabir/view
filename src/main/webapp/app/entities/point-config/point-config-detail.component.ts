import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { IPointConfig } from 'app/shared/model/point-config.model';

@Component({
    selector: 'jhi-point-config-detail',
    templateUrl: './point-config-detail.component.html'
})
export class PointConfigDetailComponent implements OnInit {
    pointConfig: IPointConfig;

    constructor(private activatedRoute: ActivatedRoute) {}

    ngOnInit() {
        this.activatedRoute.data.subscribe(({ pointConfig }) => {
            this.pointConfig = pointConfig;
        });
    }

    previousState() {
        window.history.back();
    }
}
