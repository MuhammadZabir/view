import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';

import { NgbActiveModal, NgbModal, NgbModalRef } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager } from 'ng-jhipster';

import { IStatusCategory } from 'app/shared/model/status-category.model';
import { StatusCategoryService } from './status-category.service';

@Component({
    selector: 'jhi-status-category-delete-dialog',
    templateUrl: './status-category-delete-dialog.component.html'
})
export class StatusCategoryDeleteDialogComponent {
    statusCategory: IStatusCategory;

    constructor(
        private statusCategoryService: StatusCategoryService,
        public activeModal: NgbActiveModal,
        private eventManager: JhiEventManager
    ) {}

    clear() {
        this.activeModal.dismiss('cancel');
    }

    confirmDelete(id: number) {
        this.statusCategoryService.delete(id).subscribe(response => {
            this.eventManager.broadcast({
                name: 'statusCategoryListModification',
                content: 'Deleted an statusCategory'
            });
            this.activeModal.dismiss(true);
        });
    }
}

@Component({
    selector: 'jhi-status-category-delete-popup',
    template: ''
})
export class StatusCategoryDeletePopupComponent implements OnInit, OnDestroy {
    private ngbModalRef: NgbModalRef;

    constructor(private activatedRoute: ActivatedRoute, private router: Router, private modalService: NgbModal) {}

    ngOnInit() {
        this.activatedRoute.data.subscribe(({ statusCategory }) => {
            setTimeout(() => {
                this.ngbModalRef = this.modalService.open(StatusCategoryDeleteDialogComponent as Component, {
                    size: 'lg',
                    backdrop: 'static'
                });
                this.ngbModalRef.componentInstance.statusCategory = statusCategory;
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
