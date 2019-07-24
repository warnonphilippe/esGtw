import { Component, OnInit } from '@angular/core';
import { HttpResponse, HttpErrorResponse } from '@angular/common/http';
import { FormBuilder, Validators } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import * as moment from 'moment';
import { DATE_TIME_FORMAT } from 'app/shared/constants/input.constants';
import { IInvoiceProjectionGtw, InvoiceProjectionGtw } from 'app/shared/model/invoice-projection-gtw.model';
import { InvoiceProjectionGtwService } from './invoice-projection-gtw.service';

@Component({
  selector: 'jhi-invoice-projection-gtw-update',
  templateUrl: './invoice-projection-gtw-update.component.html'
})
export class InvoiceProjectionGtwUpdateComponent implements OnInit {
  isSaving: boolean;

  editForm = this.fb.group({
    id: [],
    code: [null, [Validators.required]],
    date: [],
    details: [],
    paymentDate: [],
    paymentAmount: [],
    aggregateId: []
  });

  constructor(
    protected invoiceProjectionGtwService: InvoiceProjectionGtwService,
    protected activatedRoute: ActivatedRoute,
    private fb: FormBuilder
  ) {}

  ngOnInit() {
    this.isSaving = false;
    this.activatedRoute.data.subscribe(({ invoiceProjectionGtw }) => {
      this.updateForm(invoiceProjectionGtw);
    });
  }

  updateForm(invoiceProjectionGtw: IInvoiceProjectionGtw) {
    this.editForm.patchValue({
      id: invoiceProjectionGtw.id,
      code: invoiceProjectionGtw.code,
      date: invoiceProjectionGtw.date != null ? invoiceProjectionGtw.date.format(DATE_TIME_FORMAT) : null,
      details: invoiceProjectionGtw.details,
      paymentDate: invoiceProjectionGtw.paymentDate != null ? invoiceProjectionGtw.paymentDate.format(DATE_TIME_FORMAT) : null,
      paymentAmount: invoiceProjectionGtw.paymentAmount,
      aggregateId: invoiceProjectionGtw.aggregateId
    });
  }

  previousState() {
    window.history.back();
  }

  save() {
    this.isSaving = true;
    const invoiceProjectionGtw = this.createFromForm();
    if (invoiceProjectionGtw.id !== undefined) {
      this.subscribeToSaveResponse(this.invoiceProjectionGtwService.update(invoiceProjectionGtw));
    } else {
      this.subscribeToSaveResponse(this.invoiceProjectionGtwService.create(invoiceProjectionGtw));
    }
  }

  private createFromForm(): IInvoiceProjectionGtw {
    return {
      ...new InvoiceProjectionGtw(),
      id: this.editForm.get(['id']).value,
      code: this.editForm.get(['code']).value,
      date: this.editForm.get(['date']).value != null ? moment(this.editForm.get(['date']).value, DATE_TIME_FORMAT) : undefined,
      details: this.editForm.get(['details']).value,
      paymentDate:
        this.editForm.get(['paymentDate']).value != null ? moment(this.editForm.get(['paymentDate']).value, DATE_TIME_FORMAT) : undefined,
      paymentAmount: this.editForm.get(['paymentAmount']).value,
      aggregateId: this.editForm.get(['aggregateId']).value
    };
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IInvoiceProjectionGtw>>) {
    result.subscribe(() => this.onSaveSuccess(), () => this.onSaveError());
  }

  protected onSaveSuccess() {
    this.isSaving = false;
    this.previousState();
  }

  protected onSaveError() {
    this.isSaving = false;
  }
}
