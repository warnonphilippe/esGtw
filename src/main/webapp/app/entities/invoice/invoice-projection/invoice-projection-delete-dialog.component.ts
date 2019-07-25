import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';

import { NgbActiveModal, NgbModal, NgbModalRef } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager } from 'ng-jhipster';

import { IInvoiceProjection } from 'app/shared/model/invoice/invoice-projection.model';
import { InvoiceProjectionService } from './invoice-projection.service';

@Component({
  selector: 'jhi-invoice-projection-delete-dialog',
  templateUrl: './invoice-projection-delete-dialog.component.html'
})
export class InvoiceProjectionDeleteDialogComponent {
  invoiceProjection: IInvoiceProjection;

  constructor(
    protected invoiceProjectionService: InvoiceProjectionService,
    public activeModal: NgbActiveModal,
    protected eventManager: JhiEventManager
  ) {}

  clear() {
    this.activeModal.dismiss('cancel');
  }

  confirmDelete(id: number) {
    this.invoiceProjectionService.delete(id).subscribe(response => {
      this.eventManager.broadcast({
        name: 'invoiceProjectionListModification',
        content: 'Deleted an invoiceProjection'
      });
      this.activeModal.dismiss(true);
    });
  }
}

@Component({
  selector: 'jhi-invoice-projection-delete-popup',
  template: ''
})
export class InvoiceProjectionDeletePopupComponent implements OnInit, OnDestroy {
  protected ngbModalRef: NgbModalRef;

  constructor(protected activatedRoute: ActivatedRoute, protected router: Router, protected modalService: NgbModal) {}

  ngOnInit() {
    this.activatedRoute.data.subscribe(({ invoiceProjection }) => {
      setTimeout(() => {
        this.ngbModalRef = this.modalService.open(InvoiceProjectionDeleteDialogComponent as Component, { size: 'lg', backdrop: 'static' });
        this.ngbModalRef.componentInstance.invoiceProjection = invoiceProjection;
        this.ngbModalRef.result.then(
          result => {
            this.router.navigate(['/invoice-projection', { outlets: { popup: null } }]);
            this.ngbModalRef = null;
          },
          reason => {
            this.router.navigate(['/invoice-projection', { outlets: { popup: null } }]);
            this.ngbModalRef = null;
          }
        );
      }, 0);
    });
  }

  ngOnDestroy() {
    this.ngbModalRef = null;
  }
}
