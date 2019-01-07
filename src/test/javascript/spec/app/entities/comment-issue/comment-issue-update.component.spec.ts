/* tslint:disable max-line-length */
import { ComponentFixture, TestBed, fakeAsync, tick } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { Observable, of } from 'rxjs';

import { ViewTestModule } from '../../../test.module';
import { CommentIssueUpdateComponent } from 'app/entities/comment-issue/comment-issue-update.component';
import { CommentIssueService } from 'app/entities/comment-issue/comment-issue.service';
import { CommentIssue } from 'app/shared/model/comment-issue.model';

describe('Component Tests', () => {
    describe('CommentIssue Management Update Component', () => {
        let comp: CommentIssueUpdateComponent;
        let fixture: ComponentFixture<CommentIssueUpdateComponent>;
        let service: CommentIssueService;

        beforeEach(() => {
            TestBed.configureTestingModule({
                imports: [ViewTestModule],
                declarations: [CommentIssueUpdateComponent]
            })
                .overrideTemplate(CommentIssueUpdateComponent, '')
                .compileComponents();

            fixture = TestBed.createComponent(CommentIssueUpdateComponent);
            comp = fixture.componentInstance;
            service = fixture.debugElement.injector.get(CommentIssueService);
        });

        describe('save', () => {
            it(
                'Should call update service on save for existing entity',
                fakeAsync(() => {
                    // GIVEN
                    const entity = new CommentIssue(123);
                    spyOn(service, 'update').and.returnValue(of(new HttpResponse({ body: entity })));
                    comp.commentIssue = entity;
                    // WHEN
                    comp.save();
                    tick(); // simulate async

                    // THEN
                    expect(service.update).toHaveBeenCalledWith(entity);
                    expect(comp.isSaving).toEqual(false);
                })
            );

            it(
                'Should call create service on save for new entity',
                fakeAsync(() => {
                    // GIVEN
                    const entity = new CommentIssue();
                    spyOn(service, 'create').and.returnValue(of(new HttpResponse({ body: entity })));
                    comp.commentIssue = entity;
                    // WHEN
                    comp.save();
                    tick(); // simulate async

                    // THEN
                    expect(service.create).toHaveBeenCalledWith(entity);
                    expect(comp.isSaving).toEqual(false);
                })
            );
        });
    });
});
