/* tslint:disable max-line-length */
import { ComponentFixture, TestBed, inject, fakeAsync, tick } from '@angular/core/testing';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { Observable, of } from 'rxjs';
import { JhiEventManager } from 'ng-jhipster';

import { ViewTestModule } from '../../../test.module';
import { CommentIssueDeleteDialogComponent } from 'app/entities/comment-issue/comment-issue-delete-dialog.component';
import { CommentIssueService } from 'app/entities/comment-issue/comment-issue.service';

describe('Component Tests', () => {
    describe('CommentIssue Management Delete Component', () => {
        let comp: CommentIssueDeleteDialogComponent;
        let fixture: ComponentFixture<CommentIssueDeleteDialogComponent>;
        let service: CommentIssueService;
        let mockEventManager: any;
        let mockActiveModal: any;

        beforeEach(() => {
            TestBed.configureTestingModule({
                imports: [ViewTestModule],
                declarations: [CommentIssueDeleteDialogComponent]
            })
                .overrideTemplate(CommentIssueDeleteDialogComponent, '')
                .compileComponents();
            fixture = TestBed.createComponent(CommentIssueDeleteDialogComponent);
            comp = fixture.componentInstance;
            service = fixture.debugElement.injector.get(CommentIssueService);
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
