/* tslint:disable max-line-length */
import { ComponentFixture, TestBed, fakeAsync, tick } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { Observable, of } from 'rxjs';

import { ViewTestModule } from '../../../test.module';
import { IssueDifficultyUpdateComponent } from 'app/entities/issue-difficulty/issue-difficulty-update.component';
import { IssueDifficultyService } from 'app/entities/issue-difficulty/issue-difficulty.service';
import { IssueDifficulty } from 'app/shared/model/issue-difficulty.model';

describe('Component Tests', () => {
    describe('IssueDifficulty Management Update Component', () => {
        let comp: IssueDifficultyUpdateComponent;
        let fixture: ComponentFixture<IssueDifficultyUpdateComponent>;
        let service: IssueDifficultyService;

        beforeEach(() => {
            TestBed.configureTestingModule({
                imports: [ViewTestModule],
                declarations: [IssueDifficultyUpdateComponent]
            })
                .overrideTemplate(IssueDifficultyUpdateComponent, '')
                .compileComponents();

            fixture = TestBed.createComponent(IssueDifficultyUpdateComponent);
            comp = fixture.componentInstance;
            service = fixture.debugElement.injector.get(IssueDifficultyService);
        });

        describe('save', () => {
            it(
                'Should call update service on save for existing entity',
                fakeAsync(() => {
                    // GIVEN
                    const entity = new IssueDifficulty(123);
                    spyOn(service, 'update').and.returnValue(of(new HttpResponse({ body: entity })));
                    comp.issueDifficulty = entity;
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
                    const entity = new IssueDifficulty();
                    spyOn(service, 'create').and.returnValue(of(new HttpResponse({ body: entity })));
                    comp.issueDifficulty = entity;
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
