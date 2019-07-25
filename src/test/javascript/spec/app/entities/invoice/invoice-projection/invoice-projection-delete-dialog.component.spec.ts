/* tslint:disable max-line-length */
import { ComponentFixture, TestBed, inject, fakeAsync, tick } from '@angular/core/testing';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { Observable, of } from 'rxjs';
import { JhiEventManager } from 'ng-jhipster';

import { StoreTestModule } from '../../../../test.module';
import { InvoiceProjectionDeleteDialogComponent } from 'app/entities/invoice/invoice-projection/invoice-projection-delete-dialog.component';
import { InvoiceProjectionService } from 'app/entities/invoice/invoice-projection/invoice-projection.service';

describe('Component Tests', () => {
  describe('InvoiceProjection Management Delete Component', () => {
    let comp: InvoiceProjectionDeleteDialogComponent;
    let fixture: ComponentFixture<InvoiceProjectionDeleteDialogComponent>;
    let service: InvoiceProjectionService;
    let mockEventManager: any;
    let mockActiveModal: any;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [StoreTestModule],
        declarations: [InvoiceProjectionDeleteDialogComponent]
      })
        .overrideTemplate(InvoiceProjectionDeleteDialogComponent, '')
        .compileComponents();
      fixture = TestBed.createComponent(InvoiceProjectionDeleteDialogComponent);
      comp = fixture.componentInstance;
      service = fixture.debugElement.injector.get(InvoiceProjectionService);
      mockEventManager = fixture.debugElement.injector.get(JhiEventManager);
      mockActiveModal = fixture.debugElement.injector.get(NgbActiveModal);
    });

    describe('confirmDelete', () => {
      it('Should call delete service on confirmDelete', inject(
        [],
        fakeAsync(() => {
          // GIVEN
          spyOn(service, 'delete').and.returnValue(of({}));

          // WHEN
          comp.confirmDelete(123);
          tick();

          // THEN
          expect(service.delete).toHaveBeenCalledWith(123);
          expect(mockActiveModal.dismissSpy).toHaveBeenCalled();
          expect(mockEventManager.broadcastSpy).toHaveBeenCalled();
        })
      ));
    });
  });
});
