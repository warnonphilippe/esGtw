import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { IInvoiceProjection } from 'app/shared/model/invoice/invoice-projection.model';

@Component({
  selector: 'jhi-invoice-projection-detail',
  templateUrl: './invoice-projection-detail.component.html'
})
export class InvoiceProjectionDetailComponent implements OnInit {
  invoiceProjection: IInvoiceProjection;

  constructor(protected activatedRoute: ActivatedRoute) {}

  ngOnInit() {
    this.activatedRoute.data.subscribe(({ invoiceProjection }) => {
      this.invoiceProjection = invoiceProjection;
    });
  }

  previousState() {
    window.history.back();
  }
}
