import { Component, OnInit } from '@angular/core';
import { JhiMainComponent } from 'app/layouts';
import { ActivatedRoute, Router } from '@angular/router';
import { LoginService } from 'app/core';

/**
 *  @author: Civadis, blueprint generator
 */
@Component({
  selector: 'civa-main',
  template: '<router-outlet (activate)="onRouterOutletActivate($event)"></router-outlet>',
  styles: []
})
export class AppMainComponent implements OnInit {
  private storage: any = localStorage;

  constructor(private route: ActivatedRoute, public loginService: LoginService, public router: Router) {}

  ngOnInit() {
    this.initTenant();
  }

  onRouterOutletActivate(event: any) {
    this.toggleCssBootstrapOrPrimeng(event);
  }

  toggleCssBootstrapOrPrimeng(event: any) {
    if (event instanceof JhiMainComponent) {
      document.body.classList.add('bootstrap'); // activer bootstrap
    } else {
      document.body.classList.remove('bootstrap'); // activer primeng
    }
  }

  initTenant() {
    let realm: string = null;

    this.route.queryParams.subscribe(params => {
      if (params['realm']) {
        realm = params['realm'];
      }
    });

    const url_string = window.location.href;
    console.log(url_string);
    const url = new URL(url_string);
    const urlRealm = url.searchParams.get('realm');

    if (realm) {
      console.log('store realm from activated route', realm);
      this.setCurrentRealm(realm);
    } else if (urlRealm) {
      console.log('store realm from url', urlRealm);
      this.setCurrentRealm(urlRealm);
    }
  }

  setCurrentRealm(realm: String) {
    // memo l'ancien tenant
    const prevTenant = this.storage.getItem('gpdoccurrentrealm');

    // set le nouveau tenant
    this.storage.setItem('gpdoccurrentrealm', realm);

    // si différent du tenant précédent, on force un logout
    if (realm !== prevTenant) {
      this.loginService.logout();
    }
  }
}
