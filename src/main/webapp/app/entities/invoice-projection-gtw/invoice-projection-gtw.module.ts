import { NgModule, CUSTOM_ELEMENTS_SCHEMA } from '@angular/core';
import { RouterModule } from '@angular/router';
import { JhiLanguageService } from 'ng-jhipster';
import { JhiLanguageHelper } from 'app/core';

import { StoreSharedModule } from 'app/shared';
import {
  InvoiceProjectionGtwComponent,
  InvoiceProjectionGtwDetailComponent,
  InvoiceProjectionGtwUpdateComponent,
  InvoiceProjectionGtwDeletePopupComponent,
  InvoiceProjectionGtwDeleteDialogComponent,
  invoiceProjectionGtwRoute,
  invoiceProjectionGtwPopupRoute
} from './';

const ENTITY_STATES = [...invoiceProjectionGtwRoute, ...invoiceProjectionGtwPopupRoute];

@NgModule({
  imports: [StoreSharedModule, RouterModule.forChild(ENTITY_STATES)],
  declarations: [
    InvoiceProjectionGtwComponent,
    InvoiceProjectionGtwDetailComponent,
    InvoiceProjectionGtwUpdateComponent,
    InvoiceProjectionGtwDeleteDialogComponent,
    InvoiceProjectionGtwDeletePopupComponent
  ],
  entryComponents: [
    InvoiceProjectionGtwComponent,
    InvoiceProjectionGtwUpdateComponent,
    InvoiceProjectionGtwDeleteDialogComponent,
    InvoiceProjectionGtwDeletePopupComponent
  ],
  providers: [{ provide: JhiLanguageService, useClass: JhiLanguageService }],
  schemas: [CUSTOM_ELEMENTS_SCHEMA]
})
export class StoreInvoiceProjectionGtwModule {
  constructor(private languageService: JhiLanguageService, private languageHelper: JhiLanguageHelper) {
    this.languageHelper.language.subscribe((languageKey: string) => {
      if (languageKey !== undefined) {
        this.languageService.changeLanguage(languageKey);
      }
    });
  }
}
