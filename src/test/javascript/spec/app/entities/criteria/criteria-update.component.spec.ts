/* tslint:disable max-line-length */
import { ComponentFixture, TestBed, fakeAsync, tick } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { Observable, of } from 'rxjs';

import { ViewTestModule } from '../../../test.module';
import { CriteriaUpdateComponent } from 'app/entities/criteria/criteria-update.component';
import { CriteriaService } from 'app/entities/criteria/criteria.service';
import { Criteria } from 'app/shared/model/criteria.model';

describe('Component Tests', () => {
    describe('Criteria Management Update Component', () => {
        let comp: CriteriaUpdateComponent;
        let fixture: ComponentFixture<CriteriaUpdateComponent>;
        let service: CriteriaService;

        beforeEach(() => {
            TestBed.configureTestingModule({
                imports: [ViewTestModule],
                declarations: [CriteriaUpdateComponent]
            })
                .overrideTemplate(CriteriaUpdateComponent, '')
                .compileComponents();

            fixture = TestBed.createComponent(CriteriaUpdateComponent);
            comp = fixture.componentInstance;
            service = fixture.debugElement.injector.get(CriteriaService);
        });

        describe('save', () => {
            it(
                'Should call update service on save for existing entity',
                fakeAsync(() => {
                    // GIVEN
                    const entity = new Criteria(123);
                    spyOn(service, 'update').and.returnValue(of(new HttpResponse({ body: entity })));
                    comp.criteria = entity;
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
                    const entity = new Criteria();
                    spyOn(service, 'create').and.returnValue(of(new HttpResponse({ body: entity })));
                    comp.criteria = entity;
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
