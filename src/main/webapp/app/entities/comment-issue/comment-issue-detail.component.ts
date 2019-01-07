import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { ICommentIssue } from 'app/shared/model/comment-issue.model';

@Component({
    selector: 'jhi-comment-issue-detail',
    templateUrl: './comment-issue-detail.component.html'
})
export class CommentIssueDetailComponent implements OnInit {
    commentIssue: ICommentIssue;

    constructor(private activatedRoute: ActivatedRoute) {}

    ngOnInit() {
        this.activatedRoute.data.subscribe(({ commentIssue }) => {
            this.commentIssue = commentIssue;
        });
    }

    previousState() {
        window.history.back();
    }
}
