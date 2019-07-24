/* tslint:disable max-line-length */
import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { StoreTestModule } from '../../../test.module';
import { InvoiceProjectionGtwDetailComponent } from 'app/entities/invoice-projection-gtw/invoice-projection-gtw-detail.component';
import { InvoiceProjectionGtw } from 'app/shared/model/invoice-projection-gtw.model';

describe('Component Tests', () => {
  describe('InvoiceProjectionGtw Management Detail Component', () => {
    let comp: InvoiceProjectionGtwDetailComponent;
    let fixture: ComponentFixture<InvoiceProjectionGtwDetailComponent>;
    const route = ({ data: of({ invoiceProjectionGtw: new InvoiceProjectionGtw(123) }) } as any) as ActivatedRoute;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [StoreTestModule],
        declarations: [InvoiceProjectionGtwDetailComponent],
        providers: [{ provide: ActivatedRoute, useValue: route }]
      })
        .overrideTemplate(InvoiceProjectionGtwDetailComponent, '')
        .compileComponents();
      fixture = TestBed.createComponent(InvoiceProjectionGtwDetailComponent);
      comp = fixture.componentInstance;
    });

    describe('OnInit', () => {
      it('Should call load all on init', () => {
        // GIVEN

        // WHEN
        comp.ngOnInit();

        // THEN
        expect(comp.invoiceProjectionGtw).toEqual(jasmine.objectContaining({ id: 123 }));
      });
    });
  });
});
