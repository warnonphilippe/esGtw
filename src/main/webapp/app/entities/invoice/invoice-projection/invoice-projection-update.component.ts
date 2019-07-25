import { Component, OnInit } from '@angular/core';
import { HttpResponse, HttpErrorResponse } from '@angular/common/http';
import { FormBuilder, Validators } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import * as moment from 'moment';
import { DATE_TIME_FORMAT } from 'app/shared/constants/input.constants';
import { IInvoiceProjection, InvoiceProjection } from 'app/shared/model/invoice/invoice-projection.model';
import { InvoiceProjectionService } from './invoice-projection.service';

@Component({
  selector: 'jhi-invoice-projection-update',
  templateUrl: './invoice-projection-update.component.html'
})
export class InvoiceProjectionUpdateComponent implements OnInit {
  isSaving: boolean;

  editForm = this.fb.group({
    id: [],
    code: [null, [Validators.required]],
    date: [],
    details: [],
    status: [],
    paymentMethod: [],
    paymentDate: [],
    paymentAmount: [],
    aggregateId: []
  });

  constructor(
    protected invoiceProjectionService: InvoiceProjectionService,
    protected activatedRoute: ActivatedRoute,
    private fb: FormBuilder
  ) {}

  ngOnInit() {
    this.isSaving = false;
    this.activatedRoute.data.subscribe(({ invoiceProjection }) => {
      this.updateForm(invoiceProjection);
    });
  }

  updateForm(invoiceProjection: IInvoiceProjection) {
    this.editForm.patchValue({
      id: invoiceProjection.id,
      code: invoiceProjection.code,
      date: invoiceProjection.date != null ? invoiceProjection.date.format(DATE_TIME_FORMAT) : null,
      details: invoiceProjection.details,
      status: invoiceProjection.status,
      paymentMethod: invoiceProjection.paymentMethod,
      paymentDate: invoiceProjection.paymentDate != null ? invoiceProjection.paymentDate.format(DATE_TIME_FORMAT) : null,
      paymentAmount: invoiceProjection.paymentAmount,
      aggregateId: invoiceProjection.aggregateId
    });
  }

  previousState() {
    window.history.back();
  }

  save() {
    this.isSaving = true;
    const invoiceProjection = this.createFromForm();
    if (invoiceProjection.id !== undefined) {
      this.subscribeToSaveResponse(this.invoiceProjectionService.update(invoiceProjection));
    } else {
      this.subscribeToSaveResponse(this.invoiceProjectionService.create(invoiceProjection));
    }
  }

  private createFromForm(): IInvoiceProjection {
    return {
      ...new InvoiceProjection(),
      id: this.editForm.get(['id']).value,
      code: this.editForm.get(['code']).value,
      date: this.editForm.get(['date']).value != null ? moment(this.editForm.get(['date']).value, DATE_TIME_FORMAT) : undefined,
      details: this.editForm.get(['details']).value,
      status: this.editForm.get(['status']).value,
      paymentMethod: this.editForm.get(['paymentMethod']).value,
      paymentDate:
        this.editForm.get(['paymentDate']).value != null ? moment(this.editForm.get(['paymentDate']).value, DATE_TIME_FORMAT) : undefined,
      paymentAmount: this.editForm.get(['paymentAmount']).value,
      aggregateId: this.editForm.get(['aggregateId']).value
    };
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IInvoiceProjection>>) {
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
