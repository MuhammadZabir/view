import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { IIssue } from 'app/shared/model/issue.model';

@Component({
    selector: 'jhi-issue-detail',
    templateUrl: './issue-detail.component.html'
})
export class IssueDetailComponent implements OnInit {
    issue: IIssue;

    constructor(private activatedRoute: ActivatedRoute) {}

    ngOnInit() {
        this.activatedRoute.data.subscribe(({ issue }) => {
            this.issue = issue;
        });
    }

    previousState() {
        window.history.back();
    }
}
