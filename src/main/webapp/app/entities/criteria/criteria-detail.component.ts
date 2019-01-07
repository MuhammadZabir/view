import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { ICriteria } from 'app/shared/model/criteria.model';

@Component({
    selector: 'jhi-criteria-detail',
    templateUrl: './criteria-detail.component.html'
})
export class CriteriaDetailComponent implements OnInit {
    criteria: ICriteria;

    constructor(private activatedRoute: ActivatedRoute) {}

    ngOnInit() {
        this.activatedRoute.data.subscribe(({ criteria }) => {
            this.criteria = criteria;
        });
    }

    previousState() {
        window.history.back();
    }
}
