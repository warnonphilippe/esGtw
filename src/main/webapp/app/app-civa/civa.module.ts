import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { CivadisPrimengLayoutModule } from '@civadis/primeng-layout';
import { MenusModule } from './civa-menus/menus.module';
import { CivaMainComponent } from './civa-main/civa-main.component';
import { CivaHomeComponent } from './civa-home/civa-home.component';

@NgModule({
  imports: [CommonModule, MenusModule, CivadisPrimengLayoutModule],
  declarations: [CivaMainComponent, CivaHomeComponent],
  exports: [CivaMainComponent, CivaHomeComponent]
})
export class CivaModule {}
