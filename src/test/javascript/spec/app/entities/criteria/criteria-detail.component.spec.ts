/* tslint:disable max-line-length */
import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { ViewTestModule } from '../../../test.module';
import { CriteriaDetailComponent } from 'app/entities/criteria/criteria-detail.component';
import { Criteria } from 'app/shared/model/criteria.model';

describe('Component Tests', () => {
    describe('Criteria Management Detail Component', () => {
        let comp: CriteriaDetailComponent;
        let fixture: ComponentFixture<CriteriaDetailComponent>;
        const route = ({ data: of({ criteria: new Criteria(123) }) } as any) as ActivatedRoute;

        beforeEach(() => {
            TestBed.configureTestingModule({
                imports: [ViewTestModule],
                declarations: [CriteriaDetailComponent],
                providers: [{ provide: ActivatedRoute, useValue: route }]
            })
                .overrideTemplate(CriteriaDetailComponent, '')
                .compileComponents();
            fixture = TestBed.createComponent(CriteriaDetailComponent);
            comp = fixture.componentInstance;
        });

        describe('OnInit', () => {
            it('Should call load all on init', () => {
                // GIVEN

                // WHEN
                comp.ngOnInit();

                // THEN
                expect(comp.criteria).toEqual(jasmine.objectContaining({ id: 123 }));
            });
        });
    });
});
