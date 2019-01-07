/* tslint:disable max-line-length */
import { ComponentFixture, TestBed, inject, fakeAsync, tick } from '@angular/core/testing';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { Observable, of } from 'rxjs';
import { JhiEventManager } from 'ng-jhipster';

import { ViewTestModule } from '../../../test.module';
import { StatusCategoryDeleteDialogComponent } from 'app/entities/status-category/status-category-delete-dialog.component';
import { StatusCategoryService } from 'app/entities/status-category/status-category.service';

describe('Component Tests', () => {
    describe('StatusCategory Management Delete Component', () => {
        let comp: StatusCategoryDeleteDialogComponent;
        let fixture: ComponentFixture<StatusCategoryDeleteDialogComponent>;
        let service: StatusCategoryService;
        let mockEventManager: any;
        let mockActiveModal: any;

        beforeEach(() => {
            TestBed.configureTestingModule({
                imports: [ViewTestModule],
                declarations: [StatusCategoryDeleteDialogComponent]
            })
                .overrideTemplate(StatusCategoryDeleteDialogComponent, '')
                .compileComponents();
            fixture = TestBed.createComponent(StatusCategoryDeleteDialogComponent);
            comp = fixture.componentInstance;
            service = fixture.debugElement.injector.get(StatusCategoryService);
            mockEventManager = fixture.debugElement.injector.get(JhiEventManager);
            mockActiveModal = fixture.debugElement.injector.get(NgbActiveModal);
        });

        describe('confirmDelete', () => {
            it(
                'Should call delete service on confirmDelete',
                inject(
                    [],
                    fakeAsync(() => {
                        // GIVEN
                        spyOn(service, 'delete').and.returnValue(of({}));

                        // WHEN
                        comp.confirmDelete(123);
                        tick();

                        // THEN
                        expect(service.delete).toHaveBeenCalledWith(123);
                        expect(mockActiveModal.dismissSpy).toHaveBeenCalled();
                        expect(mockEventManager.broadcastSpy).toHaveBeenCalled();
                    })
                )
            );
        });
    });
});
