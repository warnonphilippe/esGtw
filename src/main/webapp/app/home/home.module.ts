import { NgModule, CUSTOM_ELEMENTS_SCHEMA } from '@angular/core';
import { RouterModule } from '@angular/router';

import { StoreSharedModule } from 'app/shared';
import { HOME_ROUTE, HomeComponent } from './';

import { JhiLanguageService } from 'ng-jhipster';
import { JhiLanguageHelper } from 'app/core';
@NgModule({
  imports: [StoreSharedModule, RouterModule.forChild([HOME_ROUTE])],
  declarations: [HomeComponent],
  providers: [{ provide: JhiLanguageService, useClass: JhiLanguageService }],
schemas: [CUSTOM_ELEMENTS_SCHEMA]
})
export class StoreHomeModule {

                constructor(private languageService: JhiLanguageService, private languageHelper: JhiLanguageHelper) {
                    this.languageHelper.language.subscribe((languageKey: string) => {
                        if (languageKey !== undefined) {
                            this.languageService.changeLanguage(languageKey);
                        }
                    });
                }
            }
