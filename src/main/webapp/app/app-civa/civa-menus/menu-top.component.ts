import { Component, OnInit } from '@angular/core';
import { LayoutCivadisComponent } from '@civadis/primeng-layout';
import { LoginService } from 'app/core';
import { Router } from '@angular/router';

@Component({
  selector: 'civa-menu-top',
  templateUrl: './menu-top.component.html'
})
export class MenuTopComponent implements OnInit {
  constructor(public app: LayoutCivadisComponent, public loginService: LoginService, public router: Router) {}

  ngOnInit() {}

  logout() {
    this.loginService.logout();
    this.router.navigate(['']);
  }
}
