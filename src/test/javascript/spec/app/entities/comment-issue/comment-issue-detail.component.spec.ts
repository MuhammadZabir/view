/* tslint:disable max-line-length */
import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { ViewTestModule } from '../../../test.module';
import { CommentIssueDetailComponent } from 'app/entities/comment-issue/comment-issue-detail.component';
import { CommentIssue } from 'app/shared/model/comment-issue.model';

describe('Component Tests', () => {
    describe('CommentIssue Management Detail Component', () => {
        let comp: CommentIssueDetailComponent;
        let fixture: ComponentFixture<CommentIssueDetailComponent>;
        const route = ({ data: of({ commentIssue: new CommentIssue(123) }) } as any) as ActivatedRoute;

        beforeEach(() => {
            TestBed.configureTestingModule({
                imports: [ViewTestModule],
                declarations: [CommentIssueDetailComponent],
                providers: [{ provide: ActivatedRoute, useValue: route }]
            })
                .overrideTemplate(CommentIssueDetailComponent, '')
                .compileComponents();
            fixture = TestBed.createComponent(CommentIssueDetailComponent);
            comp = fixture.componentInstance;
        });

        describe('OnInit', () => {
            it('Should call load all on init', () => {
                // GIVEN

                // WHEN
                comp.ngOnInit();

                // THEN
                expect(comp.commentIssue).toEqual(jasmine.objectContaining({ id: 123 }));
            });
        });
    });
});
