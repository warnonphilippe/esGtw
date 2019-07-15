import { Component, OnInit } from '@angular/core';

@Component({
  selector: 'civa-menu-right',
  templateUrl: './menu-right.component.html'
})
export class MenuRightComponent implements OnInit {
  apps: any = [];

  constructor() {}

  ngOnInit() {
    const msg = 'gestionnaire de vos procédures d’engagement & publication de vos offres';
    this.apps = [
      { icon: 'fas fa-handshake', titre: 'RHP Recrutement', message: msg },
      { icon: 'fas fa-university', titre: 'RHP Formation', message: msg },
      { icon: 'fas fa-money-bill', titre: 'RHP Paie', message: msg },
      { icon: 'fas fa-hand-pointer', titre: 'RHP Pointage', message: msg },
      { icon: 'fas fa-shopping-cart', titre: 'CPT Bon de commande', message: msg },
      { icon: 'fas fa-archive', titre: 'CPT Facturation', message: msg }
    ];
  }
}
