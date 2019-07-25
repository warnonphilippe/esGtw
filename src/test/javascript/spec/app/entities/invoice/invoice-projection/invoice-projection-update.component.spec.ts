/* tslint:disable max-line-length */
import { ComponentFixture, TestBed, fakeAsync, tick } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder } from '@angular/forms';
import { Observable, of } from 'rxjs';

import { StoreTestModule } from '../../../../test.module';
import { InvoiceProjectionUpdateComponent } from 'app/entities/invoice/invoice-projection/invoice-projection-update.component';
import { InvoiceProjectionService } from 'app/entities/invoice/invoice-projection/invoice-projection.service';
import { InvoiceProjection } from 'app/shared/model/invoice/invoice-projection.model';

describe('Component Tests', () => {
  describe('InvoiceProjection Management Update Component', () => {
    let comp: InvoiceProjectionUpdateComponent;
    let fixture: ComponentFixture<InvoiceProjectionUpdateComponent>;
    let service: InvoiceProjectionService;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [StoreTestModule],
        declarations: [InvoiceProjectionUpdateComponent],
        providers: [FormBuilder]
      })
        .overrideTemplate(InvoiceProjectionUpdateComponent, '')
        .compileComponents();

      fixture = TestBed.createComponent(InvoiceProjectionUpdateComponent);
      comp = fixture.componentInstance;
      service = fixture.debugElement.injector.get(InvoiceProjectionService);
    });

    describe('save', () => {
      it('Should call update service on save for existing entity', fakeAsync(() => {
        // GIVEN
        const entity = new InvoiceProjection(123);
        spyOn(service, 'update').and.returnValue(of(new HttpResponse({ body: entity })));
        comp.updateForm(entity);
        // WHEN
        comp.save();
        tick(); // simulate async

        // THEN
        expect(service.update).toHaveBeenCalledWith(entity);
        expect(comp.isSaving).toEqual(false);
      }));

      it('Should call create service on save for new entity', fakeAsync(() => {
        // GIVEN
        const entity = new InvoiceProjection();
        spyOn(service, 'create').and.returnValue(of(new HttpResponse({ body: entity })));
        comp.updateForm(entity);
        // WHEN
        comp.save();
        tick(); // simulate async

        // THEN
        expect(service.create).toHaveBeenCalledWith(entity);
        expect(comp.isSaving).toEqual(false);
      }));
    });
  });
});
