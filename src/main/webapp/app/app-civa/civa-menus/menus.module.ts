import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { MenuTopComponent } from './menu-top.component';
import { MenuLeftComponent } from './menu-left.component';
import { MenuBottomComponent } from './menu-bottom.component';
import { MenuRightComponent } from './menu-right.component';
import { CivadisPrimengLibModule } from '@civadis/primeng-lib';
import { CivadisPrimengLayoutModule } from '@civadis/primeng-layout';

@NgModule({
  declarations: [MenuTopComponent, MenuLeftComponent, MenuBottomComponent, MenuRightComponent],
  imports: [CommonModule, CivadisPrimengLibModule, CivadisPrimengLayoutModule],
  exports: [MenuTopComponent, MenuLeftComponent, MenuBottomComponent, MenuRightComponent]
})
export class MenusModule {}
