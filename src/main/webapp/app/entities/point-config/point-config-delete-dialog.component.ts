import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';

import { NgbActiveModal, NgbModal, NgbModalRef } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager } from 'ng-jhipster';

import { IPointConfig } from 'app/shared/model/point-config.model';
import { PointConfigService } from './point-config.service';

@Component({
    selector: 'jhi-point-config-delete-dialog',
    templateUrl: './point-config-delete-dialog.component.html'
})
export class PointConfigDeleteDialogComponent {
    pointConfig: IPointConfig;

    constructor(
        private pointConfigService: PointConfigService,
        public activeModal: NgbActiveModal,
        private eventManager: JhiEventManager
    ) {}

    clear() {
        this.activeModal.dismiss('cancel');
    }

    confirmDelete(id: number) {
        this.pointConfigService.delete(id).subscribe(response => {
            this.eventManager.broadcast({
                name: 'pointConfigListModification',
                content: 'Deleted an pointConfig'
            });
            this.activeModal.dismiss(true);
        });
    }
}

@Component({
    selector: 'jhi-point-config-delete-popup',
    template: ''
})
export class PointConfigDeletePopupComponent implements OnInit, OnDestroy {
    private ngbModalRef: NgbModalRef;

    constructor(private activatedRoute: ActivatedRoute, private router: Router, private modalService: NgbModal) {}

    ngOnInit() {
        this.activatedRoute.data.subscribe(({ pointConfig }) => {
            setTimeout(() => {
                this.ngbModalRef = this.modalService.open(PointConfigDeleteDialogComponent as Component, {
                    size: 'lg',
                    backdrop: 'static'
                });
                this.ngbModalRef.componentInstance.pointConfig = pointConfig;
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
