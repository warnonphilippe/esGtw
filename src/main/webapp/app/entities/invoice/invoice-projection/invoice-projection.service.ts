import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import * as moment from 'moment';
import { DATE_FORMAT } from 'app/shared/constants/input.constants';
import { map } from 'rxjs/operators';

import { SERVER_API_URL } from 'app/app.constants';
import { createRequestOption } from 'app/shared';
import { IInvoiceProjection } from 'app/shared/model/invoice/invoice-projection.model';

type EntityResponseType = HttpResponse<IInvoiceProjection>;
type EntityArrayResponseType = HttpResponse<IInvoiceProjection[]>;

@Injectable({ providedIn: 'root' })
export class InvoiceProjectionService {
  public resourceUrl = SERVER_API_URL + 'services/invoice/api/invoice-projections';

  constructor(protected http: HttpClient) {}

  create(invoiceProjection: IInvoiceProjection): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(invoiceProjection);
    return this.http
      .post<IInvoiceProjection>(this.resourceUrl, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  update(invoiceProjection: IInvoiceProjection): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(invoiceProjection);
    return this.http
      .put<IInvoiceProjection>(this.resourceUrl, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http
      .get<IInvoiceProjection>(`${this.resourceUrl}/${id}`, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http
      .get<IInvoiceProjection[]>(this.resourceUrl, { params: options, observe: 'response' })
      .pipe(map((res: EntityArrayResponseType) => this.convertDateArrayFromServer(res)));
  }

  delete(id: number): Observable<HttpResponse<any>> {
    return this.http.delete<any>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  protected convertDateFromClient(invoiceProjection: IInvoiceProjection): IInvoiceProjection {
    const copy: IInvoiceProjection = Object.assign({}, invoiceProjection, {
      date: invoiceProjection.date != null && invoiceProjection.date.isValid() ? invoiceProjection.date.toJSON() : null,
      paymentDate:
        invoiceProjection.paymentDate != null && invoiceProjection.paymentDate.isValid() ? invoiceProjection.paymentDate.toJSON() : null
    });
    return copy;
  }

  protected convertDateFromServer(res: EntityResponseType): EntityResponseType {
    if (res.body) {
      res.body.date = res.body.date != null ? moment(res.body.date) : null;
      res.body.paymentDate = res.body.paymentDate != null ? moment(res.body.paymentDate) : null;
    }
    return res;
  }

  protected convertDateArrayFromServer(res: EntityArrayResponseType): EntityArrayResponseType {
    if (res.body) {
      res.body.forEach((invoiceProjection: IInvoiceProjection) => {
        invoiceProjection.date = invoiceProjection.date != null ? moment(invoiceProjection.date) : null;
        invoiceProjection.paymentDate = invoiceProjection.paymentDate != null ? moment(invoiceProjection.paymentDate) : null;
      });
    }
    return res;
  }
}
