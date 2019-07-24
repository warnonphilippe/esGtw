import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import * as moment from 'moment';
import { DATE_FORMAT } from 'app/shared/constants/input.constants';
import { map } from 'rxjs/operators';

import { SERVER_API_URL } from 'app/app.constants';
import { createRequestOption } from 'app/shared';
import { IInvoiceProjectionGtw } from 'app/shared/model/invoice-projection-gtw.model';

type EntityResponseType = HttpResponse<IInvoiceProjectionGtw>;
type EntityArrayResponseType = HttpResponse<IInvoiceProjectionGtw[]>;

@Injectable({ providedIn: 'root' })
export class InvoiceProjectionGtwService {
  public resourceUrl = SERVER_API_URL + 'api/invoice-projection-gtws';

  constructor(protected http: HttpClient) {}

  create(invoiceProjectionGtw: IInvoiceProjectionGtw): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(invoiceProjectionGtw);
    return this.http
      .post<IInvoiceProjectionGtw>(this.resourceUrl, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  update(invoiceProjectionGtw: IInvoiceProjectionGtw): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(invoiceProjectionGtw);
    return this.http
      .put<IInvoiceProjectionGtw>(this.resourceUrl, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http
      .get<IInvoiceProjectionGtw>(`${this.resourceUrl}/${id}`, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http
      .get<IInvoiceProjectionGtw[]>(this.resourceUrl, { params: options, observe: 'response' })
      .pipe(map((res: EntityArrayResponseType) => this.convertDateArrayFromServer(res)));
  }

  delete(id: number): Observable<HttpResponse<any>> {
    return this.http.delete<any>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  protected convertDateFromClient(invoiceProjectionGtw: IInvoiceProjectionGtw): IInvoiceProjectionGtw {
    const copy: IInvoiceProjectionGtw = Object.assign({}, invoiceProjectionGtw, {
      date: invoiceProjectionGtw.date != null && invoiceProjectionGtw.date.isValid() ? invoiceProjectionGtw.date.toJSON() : null,
      paymentDate:
        invoiceProjectionGtw.paymentDate != null && invoiceProjectionGtw.paymentDate.isValid()
          ? invoiceProjectionGtw.paymentDate.toJSON()
          : null
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
      res.body.forEach((invoiceProjectionGtw: IInvoiceProjectionGtw) => {
        invoiceProjectionGtw.date = invoiceProjectionGtw.date != null ? moment(invoiceProjectionGtw.date) : null;
        invoiceProjectionGtw.paymentDate = invoiceProjectionGtw.paymentDate != null ? moment(invoiceProjectionGtw.paymentDate) : null;
      });
    }
    return res;
  }
}
