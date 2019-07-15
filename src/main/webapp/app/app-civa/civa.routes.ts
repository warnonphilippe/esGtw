import { Routes } from '@angular/router';
import { MENUS_ROUTES } from './civa-menus/menus.routes';
import { CivaHomeComponent } from './civa-home/civa-home.component';

export const AUTH_APP_ROUTES: Routes = [
  { path: 'home', component: CivaHomeComponent },
  ...MENUS_ROUTES,
  { path: '', redirectTo: 'home', pathMatch: 'full' }
];
