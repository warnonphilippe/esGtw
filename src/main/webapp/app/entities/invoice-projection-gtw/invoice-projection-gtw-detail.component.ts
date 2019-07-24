import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { IInvoiceProjectionGtw } from 'app/shared/model/invoice-projection-gtw.model';

@Component({
  selector: 'jhi-invoice-projection-gtw-detail',
  templateUrl: './invoice-projection-gtw-detail.component.html'
})
export class InvoiceProjectionGtwDetailComponent implements OnInit {
  invoiceProjectionGtw: IInvoiceProjectionGtw;

  constructor(protected activatedRoute: ActivatedRoute) {}

  ngOnInit() {
    this.activatedRoute.data.subscribe(({ invoiceProjectionGtw }) => {
      this.invoiceProjectionGtw = invoiceProjectionGtw;
    });
  }

  previousState() {
    window.history.back();
  }
}
