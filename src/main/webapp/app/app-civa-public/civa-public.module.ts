import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { RouterModule } from '@angular/router';
import { FormsModule } from '@angular/forms';
import { CivaLoginComponent } from './civa-login/civa-login.component';
import { CivaPublicMainComponent } from './civa-public-main/civa-public-main.component';

@NgModule({
  imports: [CommonModule, RouterModule, FormsModule],
  declarations: [CivaLoginComponent, CivaPublicMainComponent],
  exports: [CivaLoginComponent, CivaPublicMainComponent]
})
export class CivaPublicModule {}
