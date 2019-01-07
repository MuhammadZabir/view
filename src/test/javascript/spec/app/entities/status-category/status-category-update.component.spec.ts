/* tslint:disable max-line-length */
import { ComponentFixture, TestBed, fakeAsync, tick } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { Observable, of } from 'rxjs';

import { ViewTestModule } from '../../../test.module';
import { StatusCategoryUpdateComponent } from 'app/entities/status-category/status-category-update.component';
import { StatusCategoryService } from 'app/entities/status-category/status-category.service';
import { StatusCategory } from 'app/shared/model/status-category.model';

describe('Component Tests', () => {
    describe('StatusCategory Management Update Component', () => {
        let comp: StatusCategoryUpdateComponent;
        let fixture: ComponentFixture<StatusCategoryUpdateComponent>;
        let service: StatusCategoryService;

        beforeEach(() => {
            TestBed.configureTestingModule({
                imports: [ViewTestModule],
                declarations: [StatusCategoryUpdateComponent]
            })
                .overrideTemplate(StatusCategoryUpdateComponent, '')
                .compileComponents();

            fixture = TestBed.createComponent(StatusCategoryUpdateComponent);
            comp = fixture.componentInstance;
            service = fixture.debugElement.injector.get(StatusCategoryService);
        });

        describe('save', () => {
            it(
                'Should call update service on save for existing entity',
                fakeAsync(() => {
                    // GIVEN
                    const entity = new StatusCategory(123);
                    spyOn(service, 'update').and.returnValue(of(new HttpResponse({ body: entity })));
                    comp.statusCategory = entity;
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
                    const entity = new StatusCategory();
                    spyOn(service, 'create').and.returnValue(of(new HttpResponse({ body: entity })));
                    comp.statusCategory = entity;
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
