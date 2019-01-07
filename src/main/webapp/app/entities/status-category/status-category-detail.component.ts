import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { IStatusCategory } from 'app/shared/model/status-category.model';

@Component({
    selector: 'jhi-status-category-detail',
    templateUrl: './status-category-detail.component.html'
})
export class StatusCategoryDetailComponent implements OnInit {
    statusCategory: IStatusCategory;

    constructor(private activatedRoute: ActivatedRoute) {}

    ngOnInit() {
        this.activatedRoute.data.subscribe(({ statusCategory }) => {
            this.statusCategory = statusCategory;
        });
    }

    previousState() {
        window.history.back();
    }
}
