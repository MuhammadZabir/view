import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';

import { NgbActiveModal, NgbModal, NgbModalRef } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager } from 'ng-jhipster';

import { ICommentIssue } from 'app/shared/model/comment-issue.model';
import { CommentIssueService } from './comment-issue.service';

@Component({
    selector: 'jhi-comment-issue-delete-dialog',
    templateUrl: './comment-issue-delete-dialog.component.html'
})
export class CommentIssueDeleteDialogComponent {
    commentIssue: ICommentIssue;

    constructor(
        private commentIssueService: CommentIssueService,
        public activeModal: NgbActiveModal,
        private eventManager: JhiEventManager
    ) {}

    clear() {
        this.activeModal.dismiss('cancel');
    }

    confirmDelete(id: number) {
        this.commentIssueService.delete(id).subscribe(response => {
            this.eventManager.broadcast({
                name: 'commentIssueListModification',
                content: 'Deleted an commentIssue'
            });
            this.activeModal.dismiss(true);
        });
    }
}

@Component({
    selector: 'jhi-comment-issue-delete-popup',
    template: ''
})
export class CommentIssueDeletePopupComponent implements OnInit, OnDestroy {
    private ngbModalRef: NgbModalRef;

    constructor(private activatedRoute: ActivatedRoute, private router: Router, private modalService: NgbModal) {}

    ngOnInit() {
        this.activatedRoute.data.subscribe(({ commentIssue }) => {
            setTimeout(() => {
                this.ngbModalRef = this.modalService.open(CommentIssueDeleteDialogComponent as Component, {
                    size: 'lg',
                    backdrop: 'static'
                });
                this.ngbModalRef.componentInstance.commentIssue = commentIssue;
                this.ngbModalRef.result.then(
                    result => {
                        this.router.navigate([{ outlets: { popup: null } }], { replaceUrl: true, queryParamsHandling: 'merge' });
                        this.ngbModalRef = null;
                    },
                    reason => {
                        this.router.navigate([{ outlets: { popup: null } }], { replaceUrl: true, queryParamsHandling: 'merge' });
                        this.ngbModalRef = null;
                    }
                );
            }, 0);
        });
    }

    ngOnDestroy() {
        this.ngbModalRef = null;
    }
}
