import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, RouterStateSnapshot, Routes } from '@angular/router';
import { JhiPaginationUtil, JhiResolvePagingParams } from 'ng-jhipster';
import { UserRouteAccessService } from 'app/core';
import { Observable, of } from 'rxjs';
import { filter, map } from 'rxjs/operators';
import { InvoiceProjectionGtw } from 'app/shared/model/invoice-projection-gtw.model';
import { InvoiceProjectionGtwService } from './invoice-projection-gtw.service';
import { InvoiceProjectionGtwComponent } from './invoice-projection-gtw.component';
import { InvoiceProjectionGtwDetailComponent } from './invoice-projection-gtw-detail.component';
import { InvoiceProjectionGtwUpdateComponent } from './invoice-projection-gtw-update.component';
import { InvoiceProjectionGtwDeletePopupComponent } from './invoice-projection-gtw-delete-dialog.component';
import { IInvoiceProjectionGtw } from 'app/shared/model/invoice-projection-gtw.model';

@Injectable({ providedIn: 'root' })
export class InvoiceProjectionGtwResolve implements Resolve<IInvoiceProjectionGtw> {
  constructor(private service: InvoiceProjectionGtwService) {}

  resolve(route: ActivatedRouteSnapshot, state: RouterStateSnapshot): Observable<IInvoiceProjectionGtw> {
    const id = route.params['id'] ? route.params['id'] : null;
    if (id) {
      return this.service.find(id).pipe(
        filter((response: HttpResponse<InvoiceProjectionGtw>) => response.ok),
        map((invoiceProjectionGtw: HttpResponse<InvoiceProjectionGtw>) => invoiceProjectionGtw.body)
      );
    }
    return of(new InvoiceProjectionGtw());
  }
}

export const invoiceProjectionGtwRoute: Routes = [
  {
    path: '',
    component: InvoiceProjectionGtwComponent,
    resolve: {
      pagingParams: JhiResolvePagingParams
    },
    data: {
      authorities: ['ROLE_USER'],
      defaultSort: 'id,asc',
      pageTitle: 'storeApp.invoiceProjectionGtw.home.title'
    },
    canActivate: [UserRouteAccessService]
  },
  {
    path: ':id/view',
    component: InvoiceProjectionGtwDetailComponent,
    resolve: {
      invoiceProjectionGtw: InvoiceProjectionGtwResolve
    },
    data: {
      authorities: ['ROLE_USER'],
      pageTitle: 'storeApp.invoiceProjectionGtw.home.title'
    },
    canActivate: [UserRouteAccessService]
  },
  {
    path: 'new',
    component: InvoiceProjectionGtwUpdateComponent,
    resolve: {
      invoiceProjectionGtw: InvoiceProjectionGtwResolve
    },
    data: {
      authorities: ['ROLE_USER'],
      pageTitle: 'storeApp.invoiceProjectionGtw.home.title'
    },
    canActivate: [UserRouteAccessService]
  },
  {
    path: ':id/edit',
    component: InvoiceProjectionGtwUpdateComponent,
    resolve: {
      invoiceProjectionGtw: InvoiceProjectionGtwResolve
    },
    data: {
      authorities: ['ROLE_USER'],
      pageTitle: 'storeApp.invoiceProjectionGtw.home.title'
    },
    canActivate: [UserRouteAccessService]
  }
];

export const invoiceProjectionGtwPopupRoute: Routes = [
  {
    path: ':id/delete',
    component: InvoiceProjectionGtwDeletePopupComponent,
    resolve: {
      invoiceProjectionGtw: InvoiceProjectionGtwResolve
    },
    data: {
      authorities: ['ROLE_USER'],
      pageTitle: 'storeApp.invoiceProjectionGtw.home.title'
    },
    canActivate: [UserRouteAccessService],
    outlet: 'popup'
  }
];
