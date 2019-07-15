import { Component, AfterViewInit } from '@angular/core';
import { JhiEventManager } from 'ng-jhipster';
import { Router } from '@angular/router';
import { LoginService, StateStorageService, AccountService } from 'app/core';

/**
 *  @author: Civadis, blueprint generator, utile si pas de OAUTH2
 */
@Component({
  selector: 'civa-login',
  templateUrl: './civa-login.component.html',
  styleUrls: ['./civa-login.component.scss']
})
export class CivaLoginComponent implements AfterViewInit {
  authenticationError: boolean;
  password: string;
  rememberMe: boolean;
  username: string;
  credentials: any;

  constructor(
    private eventManager: JhiEventManager,
    private loginService: LoginService,
    private stateStorageService: StateStorageService,
    private accountService: AccountService,
    private router: Router
  ) {
    this.credentials = {};
  }

  ngAfterViewInit() {}

  cancel() {
    this.credentials = {
      username: null,
      password: null,
      rememberMe: true
    };
    this.authenticationError = false;
  }

  async login() {
    try {
      this.authenticationError = false;
      await this.loginService.login();

      this.eventManager.broadcast({
        name: 'authenticationSuccess',
        content: 'Sending Authentication Success'
      });

      // previousState was set in the authExpiredInterceptor before being redirected to login modal.
      // since login is succesful, go to stored previousState and clear previousState
      const redirect = this.stateStorageService.getUrl();
      if (redirect) {
        this.stateStorageService.storeUrl(null);
        this.router.navigate([redirect]);
      }

      const isAdmin = await this.isAdmin();
      this.router.navigate(isAdmin ? ['admin'] : ['auth']);
    } catch (err) {
      this.authenticationError = true;
    }
  }

  isAdmin() {
    return this.accountService.hasAnyAuthority(['ROLE_ADMIN']);
  }

  register() {
    this.router.navigate(['/register']);
  }

  requestResetPassword() {
    this.router.navigate(['/reset', 'request']);
  }
}
