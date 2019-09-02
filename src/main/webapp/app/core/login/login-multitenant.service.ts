import { Injectable } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

declare var localStorage: any;

/**
 *  @author: Civadis, blueprint generator
 */
@Injectable({ providedIn: 'root' })
export class LoginMultitenantService {
  private storage: any = localStorage;

  constructor(private activatedRoute: ActivatedRoute) {}

  login() {
    // recherche du realm dans le params et stockage
    this.activatedRoute.queryParams.subscribe(params => {
      if (params['realm']) {
        this.saveRealm(params['realm']);
      }
    });

    // récupère le realm du stockage, issu des params ou d'un stockage précédent
    const realm = this.retrieveRealm();
    console.log('current realm to log in', realm);

    const port = location.port ? ':' + location.port : '';
    let contextPath = location.pathname;
    if (contextPath.endsWith('accessdenied')) {
      contextPath = contextPath.substring(0, contextPath.indexOf('accessdenied'));
    }
    if (!contextPath.endsWith('/')) {
      contextPath = contextPath + '/';
    }

    // If you have configured multiple OIDC providers, then, you can update this URL to /login.
    // It will show a Spring Security generated login page with links to configured OIDC providers.
    location.href = `//${location.hostname}${port}${contextPath}oauth2/authorization/${realm}_ext`;
  }

  saveRealm(val): void {
    this.storage.setItem('gpdoccurrentrealm', val);
  }

  retrieveRealm(): string {
    return this.storage.getItem('gpdoccurrentrealm');
  }
}
