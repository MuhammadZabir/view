/* tslint:disable max-line-length */
import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { ViewTestModule } from '../../../test.module';
import { PointConfigDetailComponent } from 'app/entities/point-config/point-config-detail.component';
import { PointConfig } from 'app/shared/model/point-config.model';

describe('Component Tests', () => {
    describe('PointConfig Management Detail Component', () => {
        let comp: PointConfigDetailComponent;
        let fixture: ComponentFixture<PointConfigDetailComponent>;
        const route = ({ data: of({ pointConfig: new PointConfig(123) }) } as any) as ActivatedRoute;

        beforeEach(() => {
            TestBed.configureTestingModule({
                imports: [ViewTestModule],
                declarations: [PointConfigDetailComponent],
                providers: [{ provide: ActivatedRoute, useValue: route }]
            })
                .overrideTemplate(PointConfigDetailComponent, '')
                .compileComponents();
            fixture = TestBed.createComponent(PointConfigDetailComponent);
            comp = fixture.componentInstance;
        });

        describe('OnInit', () => {
            it('Should call load all on init', () => {
                // GIVEN

                // WHEN
                comp.ngOnInit();

                // THEN
                expect(comp.pointConfig).toEqual(jasmine.objectContaining({ id: 123 }));
            });
        });
    });
});
