/* tslint:disable max-line-length */
import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { StoreTestModule } from '../../../../test.module';
import { InvoiceProjectionDetailComponent } from 'app/entities/invoice/invoice-projection/invoice-projection-detail.component';
import { InvoiceProjection } from 'app/shared/model/invoice/invoice-projection.model';

describe('Component Tests', () => {
  describe('InvoiceProjection Management Detail Component', () => {
    let comp: InvoiceProjectionDetailComponent;
    let fixture: ComponentFixture<InvoiceProjectionDetailComponent>;
    const route = ({ data: of({ invoiceProjection: new InvoiceProjection(123) }) } as any) as ActivatedRoute;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [StoreTestModule],
        declarations: [InvoiceProjectionDetailComponent],
        providers: [{ provide: ActivatedRoute, useValue: route }]
      })
        .overrideTemplate(InvoiceProjectionDetailComponent, '')
        .compileComponents();
      fixture = TestBed.createComponent(InvoiceProjectionDetailComponent);
      comp = fixture.componentInstance;
    });

    describe('OnInit', () => {
      it('Should call load all on init', () => {
        // GIVEN

        // WHEN
        comp.ngOnInit();

        // THEN
        expect(comp.invoiceProjection).toEqual(jasmine.objectContaining({ id: 123 }));
      });
    });
  });
});
