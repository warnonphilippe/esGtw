import { NgModule } from '@angular/core';
import { RouterModule } from '@angular/router';
import { errorRoute, navbarRoute, JhiMainComponent } from './layouts';
import { UserRouteAccessService } from './core';
import { DEBUG_INFO_ENABLED } from 'app/app.constants';
import { AUTH_APP_ROUTES } from './app-civa/civa.routes';
import { PUBLIC_APP_ROUTES } from './app-civa-public/civa-public.routes';
import { CivaMainComponent } from './app-civa/civa-main/civa-main.component';
import { CivaPublicMainComponent } from './app-civa-public/civa-public-main/civa-public-main.component';
import { StoreHomeModule } from './home/home.module';
import { StoreEntityModule } from './entities/entity.module';
import { StoreAdminModule } from './admin/admin.module';

const LAYOUT_ROUTES = [navbarRoute, ...errorRoute];

@NgModule({
  imports: [
    RouterModule.forRoot(
      [
        {
          path: 'public',
          component: CivaPublicMainComponent,
          children: [...PUBLIC_APP_ROUTES]
        },
        {
          path: 'auth',
          component: CivaMainComponent,
          children: [...AUTH_APP_ROUTES],
          data: {
            authorities: ['ROLE_USER']
          },
          canActivate: [UserRouteAccessService]
        },
        {
          path: 'admin',
          component: JhiMainComponent,
          data: {
            authorities: ['ROLE_ADMIN']
          },
          canActivate: [UserRouteAccessService],
          children: [
            // { path: '', loadChildren: () => import('./home/home.module').then(m => m.StoreHomeModule) },
            // { path: '', loadChildren: () => import('./entities/entity.module').then(m => m.StoreEntityModule) },
            // { path: 'admin', loadChildren: () => import('./admin/admin.module').then(m => m.StoreAdminModule) },
            { path: '', loadChildren: './home/home.module#StoreHomeModule' },
            { path: '', loadChildren: './entities/entity.module#StoreEntityModule' },
            { path: 'admin', loadChildren: './admin/admin.module#StoreAdminModule' },
            ...LAYOUT_ROUTES
          ]
        },
        { path: '', redirectTo: 'auth', pathMatch: 'full' },
        ...LAYOUT_ROUTES
      ],
      { enableTracing: DEBUG_INFO_ENABLED }
    )
  ],
  exports: [RouterModule]
})
export class StoreAppRoutingModule {}
