/* tslint:disable max-line-length */
import { ComponentFixture, TestBed, inject, fakeAsync, tick } from '@angular/core/testing';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { Observable, of } from 'rxjs';
import { JhiEventManager } from 'ng-jhipster';

import { ViewTestModule } from '../../../test.module';
import { AchievementDeleteDialogComponent } from 'app/entities/achievement/achievement-delete-dialog.component';
import { AchievementService } from 'app/entities/achievement/achievement.service';

describe('Component Tests', () => {
    describe('Achievement Management Delete Component', () => {
        let comp: AchievementDeleteDialogComponent;
        let fixture: ComponentFixture<AchievementDeleteDialogComponent>;
        let service: AchievementService;
        let mockEventManager: any;
        let mockActiveModal: any;

        beforeEach(() => {
            TestBed.configureTestingModule({
                imports: [ViewTestModule],
                declarations: [AchievementDeleteDialogComponent]
            })
                .overrideTemplate(AchievementDeleteDialogComponent, '')
                .compileComponents();
            fixture = TestBed.createComponent(AchievementDeleteDialogComponent);
            comp = fixture.componentInstance;
            service = fixture.debugElement.injector.get(AchievementService);
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
