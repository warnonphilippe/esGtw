import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';

import { NgbActiveModal, NgbModal, NgbModalRef } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager } from 'ng-jhipster';

import { IInvoiceProjectionGtw } from 'app/shared/model/invoice-projection-gtw.model';
import { InvoiceProjectionGtwService } from './invoice-projection-gtw.service';

@Component({
  selector: 'jhi-invoice-projection-gtw-delete-dialog',
  templateUrl: './invoice-projection-gtw-delete-dialog.component.html'
})
export class InvoiceProjectionGtwDeleteDialogComponent {
  invoiceProjectionGtw: IInvoiceProjectionGtw;

  constructor(
    protected invoiceProjectionGtwService: InvoiceProjectionGtwService,
    public activeModal: NgbActiveModal,
    protected eventManager: JhiEventManager
  ) {}

  clear() {
    this.activeModal.dismiss('cancel');
  }

  confirmDelete(id: number) {
    this.invoiceProjectionGtwService.delete(id).subscribe(response => {
      this.eventManager.broadcast({
        name: 'invoiceProjectionGtwListModification',
        content: 'Deleted an invoiceProjectionGtw'
      });
      this.activeModal.dismiss(true);
    });
  }
}

@Component({
  selector: 'jhi-invoice-projection-gtw-delete-popup',
  template: ''
})
export class InvoiceProjectionGtwDeletePopupComponent implements OnInit, OnDestroy {
  protected ngbModalRef: NgbModalRef;

  constructor(protected activatedRoute: ActivatedRoute, protected router: Router, protected modalService: NgbModal) {}

  ngOnInit() {
    this.activatedRoute.data.subscribe(({ invoiceProjectionGtw }) => {
      setTimeout(() => {
        this.ngbModalRef = this.modalService.open(InvoiceProjectionGtwDeleteDialogComponent as Component, {
          size: 'lg',
          backdrop: 'static'
        });
        this.ngbModalRef.componentInstance.invoiceProjectionGtw = invoiceProjectionGtw;
        this.ngbModalRef.result.then(
          result => {
            this.router.navigate(['/invoice-projection-gtw', { outlets: { popup: null } }]);
            this.ngbModalRef = null;
          },
          reason => {
            this.router.navigate(['/invoice-projection-gtw', { outlets: { popup: null } }]);
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
