import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';

import { NgbActiveModal, NgbModal, NgbModalRef } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager } from 'ng-jhipster';

import { IIssueDifficulty } from 'app/shared/model/issue-difficulty.model';
import { IssueDifficultyService } from './issue-difficulty.service';

@Component({
    selector: 'jhi-issue-difficulty-delete-dialog',
    templateUrl: './issue-difficulty-delete-dialog.component.html'
})
export class IssueDifficultyDeleteDialogComponent {
    issueDifficulty: IIssueDifficulty;

    constructor(
        private issueDifficultyService: IssueDifficultyService,
        public activeModal: NgbActiveModal,
        private eventManager: JhiEventManager
    ) {}

    clear() {
        this.activeModal.dismiss('cancel');
    }

    confirmDelete(id: number) {
        this.issueDifficultyService.delete(id).subscribe(response => {
            this.eventManager.broadcast({
                name: 'issueDifficultyListModification',
                content: 'Deleted an issueDifficulty'
            });
            this.activeModal.dismiss(true);
        });
    }
}

@Component({
    selector: 'jhi-issue-difficulty-delete-popup',
    template: ''
})
export class IssueDifficultyDeletePopupComponent implements OnInit, OnDestroy {
    private ngbModalRef: NgbModalRef;

    constructor(private activatedRoute: ActivatedRoute, private router: Router, private modalService: NgbModal) {}

    ngOnInit() {
        this.activatedRoute.data.subscribe(({ issueDifficulty }) => {
            setTimeout(() => {
                this.ngbModalRef = this.modalService.open(IssueDifficultyDeleteDialogComponent as Component, {
                    size: 'lg',
                    backdrop: 'static'
                });
                this.ngbModalRef.componentInstance.issueDifficulty = issueDifficulty;
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
