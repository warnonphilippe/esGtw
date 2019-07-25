import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, RouterStateSnapshot, Routes } from '@angular/router';
import { JhiPaginationUtil, JhiResolvePagingParams } from 'ng-jhipster';
import { UserRouteAccessService } from 'app/core';
import { Observable, of } from 'rxjs';
import { filter, map } from 'rxjs/operators';
import { InvoiceProjection } from 'app/shared/model/invoice/invoice-projection.model';
import { InvoiceProjectionService } from './invoice-projection.service';
import { InvoiceProjectionComponent } from './invoice-projection.component';
import { InvoiceProjectionDetailComponent } from './invoice-projection-detail.component';
import { InvoiceProjectionUpdateComponent } from './invoice-projection-update.component';
import { InvoiceProjectionDeletePopupComponent } from './invoice-projection-delete-dialog.component';
import { IInvoiceProjection } from 'app/shared/model/invoice/invoice-projection.model';

@Injectable({ providedIn: 'root' })
export class InvoiceProjectionResolve implements Resolve<IInvoiceProjection> {
  constructor(private service: InvoiceProjectionService) {}

  resolve(route: ActivatedRouteSnapshot, state: RouterStateSnapshot): Observable<IInvoiceProjection> {
    const id = route.params['id'] ? route.params['id'] : null;
    if (id) {
      return this.service.find(id).pipe(
        filter((response: HttpResponse<InvoiceProjection>) => response.ok),
        map((invoiceProjection: HttpResponse<InvoiceProjection>) => invoiceProjection.body)
      );
    }
    return of(new InvoiceProjection());
  }
}

export const invoiceProjectionRoute: Routes = [
  {
    path: '',
    component: InvoiceProjectionComponent,
    resolve: {
      pagingParams: JhiResolvePagingParams
    },
    data: {
      authorities: ['ROLE_USER'],
      defaultSort: 'id,asc',
      pageTitle: 'storeApp.invoiceInvoiceProjection.home.title'
    },
    canActivate: [UserRouteAccessService]
  },
  {
    path: ':id/view',
    component: InvoiceProjectionDetailComponent,
    resolve: {
      invoiceProjection: InvoiceProjectionResolve
    },
    data: {
      authorities: ['ROLE_USER'],
      pageTitle: 'storeApp.invoiceInvoiceProjection.home.title'
    },
    canActivate: [UserRouteAccessService]
  },
  {
    path: 'new',
    component: InvoiceProjectionUpdateComponent,
    resolve: {
      invoiceProjection: InvoiceProjectionResolve
    },
    data: {
      authorities: ['ROLE_USER'],
      pageTitle: 'storeApp.invoiceInvoiceProjection.home.title'
    },
    canActivate: [UserRouteAccessService]
  },
  {
    path: ':id/edit',
    component: InvoiceProjectionUpdateComponent,
    resolve: {
      invoiceProjection: InvoiceProjectionResolve
    },
    data: {
      authorities: ['ROLE_USER'],
      pageTitle: 'storeApp.invoiceInvoiceProjection.home.title'
    },
    canActivate: [UserRouteAccessService]
  }
];

export const invoiceProjectionPopupRoute: Routes = [
  {
    path: ':id/delete',
    component: InvoiceProjectionDeletePopupComponent,
    resolve: {
      invoiceProjection: InvoiceProjectionResolve
    },
    data: {
      authorities: ['ROLE_USER'],
      pageTitle: 'storeApp.invoiceInvoiceProjection.home.title'
    },
    canActivate: [UserRouteAccessService],
    outlet: 'popup'
  }
];
