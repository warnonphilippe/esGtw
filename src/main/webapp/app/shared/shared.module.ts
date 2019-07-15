import { NgModule, CUSTOM_ELEMENTS_SCHEMA } from '@angular/core';
import { StoreSharedCommonModule, HasAnyAuthorityDirective } from './';

@NgModule({
  imports: [StoreSharedCommonModule],
  declarations: [HasAnyAuthorityDirective],
  exports: [StoreSharedCommonModule, HasAnyAuthorityDirective],
  schemas: [CUSTOM_ELEMENTS_SCHEMA]
})
export class StoreSharedModule {
  static forRoot() {
    return {
      ngModule: StoreSharedModule
    };
  }
}
