import { Component, Input, OnInit } from '@angular/core';
import { LayoutCivadisComponent, LayoutMenuComponent } from '@civadis/primeng-layout';
import { TranslateService } from '@ngx-translate/core';
import { AccountService } from 'app/core';

@Component({
  selector: 'civa-menu-left',
  templateUrl: './menu-left.component.html'
})
export class MenuLeftComponent implements OnInit {
  @Input()
  reset: boolean;

  model: any[];

  constructor(
    public app: LayoutCivadisComponent,
    public layoutMenu: LayoutMenuComponent,
    private accountService: AccountService,
    public translateService: TranslateService
  ) {
    this.translateService.setTranslation(
      'en',
      {
        APP: { MENU: { HOME: 'Home', ADMIN: 'Admin' } }
      },
      true
    );
    this.translateService.setTranslation(
      'fr',
      {
        APP: { MENU: { HOME: 'Accueil', ADMIN: 'Admin' } }
      },
      true
    );
    this.translateService.setTranslation(
      'nl',
      {
        APP: { MENU: { HOME: 'Home', ADMIN: 'Admin' } }
      },
      true
    );
    this.translateService.setTranslation(
      'de',
      {
        APP: { MENU: { HOME: 'Home', ADMIN: 'Admin' } }
      },
      true
    );
  }

  async ngOnInit() {
    this.model = [{ label: 'APP.MENU.HOME', icon: 'fas fa-fw fa-home', routerLink: ['/home'] }];

    if (await this.isAdmin()) {
      this.model = [...this.model, { label: 'APP.MENU.ADMIN', icon: 'fas fa-fw fa-shield-alt', routerLink: ['/admin/'] }];
    }
  }

  isAdmin(): boolean {
    return this.accountService.hasAnyAuthority(['ROLE_ADMIN']);
  }
}
