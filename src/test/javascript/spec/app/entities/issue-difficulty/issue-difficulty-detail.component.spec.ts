/* tslint:disable max-line-length */
import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { ViewTestModule } from '../../../test.module';
import { IssueDifficultyDetailComponent } from 'app/entities/issue-difficulty/issue-difficulty-detail.component';
import { IssueDifficulty } from 'app/shared/model/issue-difficulty.model';

describe('Component Tests', () => {
    describe('IssueDifficulty Management Detail Component', () => {
        let comp: IssueDifficultyDetailComponent;
        let fixture: ComponentFixture<IssueDifficultyDetailComponent>;
        const route = ({ data: of({ issueDifficulty: new IssueDifficulty(123) }) } as any) as ActivatedRoute;

        beforeEach(() => {
            TestBed.configureTestingModule({
                imports: [ViewTestModule],
                declarations: [IssueDifficultyDetailComponent],
                providers: [{ provide: ActivatedRoute, useValue: route }]
            })
                .overrideTemplate(IssueDifficultyDetailComponent, '')
                .compileComponents();
            fixture = TestBed.createComponent(IssueDifficultyDetailComponent);
            comp = fixture.componentInstance;
        });

        describe('OnInit', () => {
            it('Should call load all on init', () => {
                // GIVEN

                // WHEN
                comp.ngOnInit();

                // THEN
                expect(comp.issueDifficulty).toEqual(jasmine.objectContaining({ id: 123 }));
            });
        });
    });
});
