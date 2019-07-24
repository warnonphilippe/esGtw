/* tslint:disable max-line-length */
import { ComponentFixture, TestBed, inject, fakeAsync, tick } from '@angular/core/testing';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { Observable, of } from 'rxjs';
import { JhiEventManager } from 'ng-jhipster';

import { StoreTestModule } from '../../../test.module';
import { InvoiceProjectionGtwDeleteDialogComponent } from 'app/entities/invoice-projection-gtw/invoice-projection-gtw-delete-dialog.component';
import { InvoiceProjectionGtwService } from 'app/entities/invoice-projection-gtw/invoice-projection-gtw.service';

describe('Component Tests', () => {
  describe('InvoiceProjectionGtw Management Delete Component', () => {
    let comp: InvoiceProjectionGtwDeleteDialogComponent;
    let fixture: ComponentFixture<InvoiceProjectionGtwDeleteDialogComponent>;
    let service: InvoiceProjectionGtwService;
    let mockEventManager: any;
    let mockActiveModal: any;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [StoreTestModule],
        declarations: [InvoiceProjectionGtwDeleteDialogComponent]
      })
        .overrideTemplate(InvoiceProjectionGtwDeleteDialogComponent, '')
        .compileComponents();
      fixture = TestBed.createComponent(InvoiceProjectionGtwDeleteDialogComponent);
      comp = fixture.componentInstance;
      service = fixture.debugElement.injector.get(InvoiceProjectionGtwService);
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
