import { Routes } from '@angular/router';
import { CivaLoginComponent } from './civa-login/civa-login.component';

export const PUBLIC_APP_ROUTES: Routes = [
  { path: 'login', component: CivaLoginComponent },
  { path: '', redirectTo: 'login', pathMatch: 'full' }
];
