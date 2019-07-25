import { NgModule, CUSTOM_ELEMENTS_SCHEMA } from '@angular/core';
import { RouterModule } from '@angular/router';
import { JhiLanguageService } from 'ng-jhipster';
import { JhiLanguageHelper } from 'app/core';

import { StoreSharedModule } from 'app/shared';
import {
  InvoiceProjectionComponent,
  InvoiceProjectionDetailComponent,
  InvoiceProjectionUpdateComponent,
  InvoiceProjectionDeletePopupComponent,
  InvoiceProjectionDeleteDialogComponent,
  invoiceProjectionRoute,
  invoiceProjectionPopupRoute
} from './';

const ENTITY_STATES = [...invoiceProjectionRoute, ...invoiceProjectionPopupRoute];

@NgModule({
  imports: [StoreSharedModule, RouterModule.forChild(ENTITY_STATES)],
  declarations: [
    InvoiceProjectionComponent,
    InvoiceProjectionDetailComponent,
    InvoiceProjectionUpdateComponent,
    InvoiceProjectionDeleteDialogComponent,
    InvoiceProjectionDeletePopupComponent
  ],
  entryComponents: [
    InvoiceProjectionComponent,
    InvoiceProjectionUpdateComponent,
    InvoiceProjectionDeleteDialogComponent,
    InvoiceProjectionDeletePopupComponent
  ],
  providers: [{ provide: JhiLanguageService, useClass: JhiLanguageService }],
  schemas: [CUSTOM_ELEMENTS_SCHEMA]
})
export class InvoiceInvoiceProjectionModule {
  constructor(private languageService: JhiLanguageService, private languageHelper: JhiLanguageHelper) {
    this.languageHelper.language.subscribe((languageKey: string) => {
      if (languageKey !== undefined) {
        this.languageService.changeLanguage(languageKey);
      }
    });
  }
}
