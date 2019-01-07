import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { IIssueDifficulty } from 'app/shared/model/issue-difficulty.model';

@Component({
    selector: 'jhi-issue-difficulty-detail',
    templateUrl: './issue-difficulty-detail.component.html'
})
export class IssueDifficultyDetailComponent implements OnInit {
    issueDifficulty: IIssueDifficulty;

    constructor(private activatedRoute: ActivatedRoute) {}

    ngOnInit() {
        this.activatedRoute.data.subscribe(({ issueDifficulty }) => {
            this.issueDifficulty = issueDifficulty;
        });
    }

    previousState() {
        window.history.back();
    }
}
