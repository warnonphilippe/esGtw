/* tslint:disable max-line-length */
import { ComponentFixture, TestBed, fakeAsync, tick } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder } from '@angular/forms';
import { Observable, of } from 'rxjs';

import { StoreTestModule } from '../../../test.module';
import { InvoiceProjectionGtwUpdateComponent } from 'app/entities/invoice-projection-gtw/invoice-projection-gtw-update.component';
import { InvoiceProjectionGtwService } from 'app/entities/invoice-projection-gtw/invoice-projection-gtw.service';
import { InvoiceProjectionGtw } from 'app/shared/model/invoice-projection-gtw.model';

describe('Component Tests', () => {
  describe('InvoiceProjectionGtw Management Update Component', () => {
    let comp: InvoiceProjectionGtwUpdateComponent;
    let fixture: ComponentFixture<InvoiceProjectionGtwUpdateComponent>;
    let service: InvoiceProjectionGtwService;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [StoreTestModule],
        declarations: [InvoiceProjectionGtwUpdateComponent],
        providers: [FormBuilder]
      })
        .overrideTemplate(InvoiceProjectionGtwUpdateComponent, '')
        .compileComponents();

      fixture = TestBed.createComponent(InvoiceProjectionGtwUpdateComponent);
      comp = fixture.componentInstance;
      service = fixture.debugElement.injector.get(InvoiceProjectionGtwService);
    });

    describe('save', () => {
      it('Should call update service on save for existing entity', fakeAsync(() => {
        // GIVEN
        const entity = new InvoiceProjectionGtw(123);
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
        const entity = new InvoiceProjectionGtw();
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
