/* tslint:disable max-line-length */
import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { ViewTestModule } from '../../../test.module';
import { IssueDetailComponent } from 'app/entities/issue/issue-detail.component';
import { Issue } from 'app/shared/model/issue.model';

describe('Component Tests', () => {
    describe('Issue Management Detail Component', () => {
        let comp: IssueDetailComponent;
        let fixture: ComponentFixture<IssueDetailComponent>;
        const route = ({ data: of({ issue: new Issue(123) }) } as any) as ActivatedRoute;

        beforeEach(() => {
            TestBed.configureTestingModule({
                imports: [ViewTestModule],
                declarations: [IssueDetailComponent],
                providers: [{ provide: ActivatedRoute, useValue: route }]
            })
                .overrideTemplate(IssueDetailComponent, '')
                .compileComponents();
            fixture = TestBed.createComponent(IssueDetailComponent);
            comp = fixture.componentInstance;
        });

        describe('OnInit', () => {
            it('Should call load all on init', () => {
                // GIVEN

                // WHEN
                comp.ngOnInit();

                // THEN
                expect(comp.issue).toEqual(jasmine.objectContaining({ id: 123 }));
            });
        });
    });
});
