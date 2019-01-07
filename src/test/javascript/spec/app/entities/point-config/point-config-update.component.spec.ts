/* tslint:disable max-line-length */
import { ComponentFixture, TestBed, fakeAsync, tick } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { Observable, of } from 'rxjs';

import { ViewTestModule } from '../../../test.module';
import { PointConfigUpdateComponent } from 'app/entities/point-config/point-config-update.component';
import { PointConfigService } from 'app/entities/point-config/point-config.service';
import { PointConfig } from 'app/shared/model/point-config.model';

describe('Component Tests', () => {
    describe('PointConfig Management Update Component', () => {
        let comp: PointConfigUpdateComponent;
        let fixture: ComponentFixture<PointConfigUpdateComponent>;
        let service: PointConfigService;

        beforeEach(() => {
            TestBed.configureTestingModule({
                imports: [ViewTestModule],
                declarations: [PointConfigUpdateComponent]
            })
                .overrideTemplate(PointConfigUpdateComponent, '')
                .compileComponents();

            fixture = TestBed.createComponent(PointConfigUpdateComponent);
            comp = fixture.componentInstance;
            service = fixture.debugElement.injector.get(PointConfigService);
        });

        describe('save', () => {
            it(
                'Should call update service on save for existing entity',
                fakeAsync(() => {
                    // GIVEN
                    const entity = new PointConfig(123);
                    spyOn(service, 'update').and.returnValue(of(new HttpResponse({ body: entity })));
                    comp.pointConfig = entity;
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
                    const entity = new PointConfig();
                    spyOn(service, 'create').and.returnValue(of(new HttpResponse({ body: entity })));
                    comp.pointConfig = entity;
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
