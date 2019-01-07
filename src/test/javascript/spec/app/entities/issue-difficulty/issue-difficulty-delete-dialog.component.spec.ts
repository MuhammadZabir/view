/* tslint:disable max-line-length */
import { ComponentFixture, TestBed, inject, fakeAsync, tick } from '@angular/core/testing';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { Observable, of } from 'rxjs';
import { JhiEventManager } from 'ng-jhipster';

import { ViewTestModule } from '../../../test.module';
import { IssueDifficultyDeleteDialogComponent } from 'app/entities/issue-difficulty/issue-difficulty-delete-dialog.component';
import { IssueDifficultyService } from 'app/entities/issue-difficulty/issue-difficulty.service';

describe('Component Tests', () => {
    describe('IssueDifficulty Management Delete Component', () => {
        let comp: IssueDifficultyDeleteDialogComponent;
        let fixture: ComponentFixture<IssueDifficultyDeleteDialogComponent>;
        let service: IssueDifficultyService;
        let mockEventManager: any;
        let mockActiveModal: any;

        beforeEach(() => {
            TestBed.configureTestingModule({
                imports: [ViewTestModule],
                declarations: [IssueDifficultyDeleteDialogComponent]
            })
                .overrideTemplate(IssueDifficultyDeleteDialogComponent, '')
                .compileComponents();
            fixture = TestBed.createComponent(IssueDifficultyDeleteDialogComponent);
            comp = fixture.componentInstance;
            service = fixture.debugElement.injector.get(IssueDifficultyService);
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
