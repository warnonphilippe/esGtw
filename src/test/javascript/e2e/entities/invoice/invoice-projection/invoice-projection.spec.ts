/* tslint:disable no-unused-expression */
import { browser, ExpectedConditions as ec, protractor, promise } from 'protractor';
import { NavBarPage, SignInPage } from '../../../page-objects/jhi-page-objects';

import {
  InvoiceProjectionComponentsPage,
  InvoiceProjectionDeleteDialog,
  InvoiceProjectionUpdatePage
} from './invoice-projection.page-object';

const expect = chai.expect;

describe('InvoiceProjection e2e test', () => {
  let navBarPage: NavBarPage;
  let signInPage: SignInPage;
  let invoiceProjectionUpdatePage: InvoiceProjectionUpdatePage;
  let invoiceProjectionComponentsPage: InvoiceProjectionComponentsPage;
  let invoiceProjectionDeleteDialog: InvoiceProjectionDeleteDialog;

  before(async () => {
    await browser.get('/');
    navBarPage = new NavBarPage();
    signInPage = await navBarPage.getSignInPage();
    await signInPage.loginWithOAuth('admin', 'admin');
    await browser.wait(ec.visibilityOf(navBarPage.entityMenu), 5000);
  });

  it('should load InvoiceProjections', async () => {
    await navBarPage.goToEntity('invoice-projection');
    invoiceProjectionComponentsPage = new InvoiceProjectionComponentsPage();
    await browser.wait(ec.visibilityOf(invoiceProjectionComponentsPage.title), 5000);
    expect(await invoiceProjectionComponentsPage.getTitle()).to.eq('storeApp.invoiceInvoiceProjection.home.title');
  });

  it('should load create InvoiceProjection page', async () => {
    await invoiceProjectionComponentsPage.clickOnCreateButton();
    invoiceProjectionUpdatePage = new InvoiceProjectionUpdatePage();
    expect(await invoiceProjectionUpdatePage.getPageTitle()).to.eq('storeApp.invoiceInvoiceProjection.home.createOrEditLabel');
    await invoiceProjectionUpdatePage.cancel();
  });

  it('should create and save InvoiceProjections', async () => {
    const nbButtonsBeforeCreate = await invoiceProjectionComponentsPage.countDeleteButtons();

    await invoiceProjectionComponentsPage.clickOnCreateButton();
    await promise.all([
      invoiceProjectionUpdatePage.setCodeInput('code'),
      invoiceProjectionUpdatePage.setDateInput('01/01/2001' + protractor.Key.TAB + '02:30AM'),
      invoiceProjectionUpdatePage.setDetailsInput('details'),
      invoiceProjectionUpdatePage.statusSelectLastOption(),
      invoiceProjectionUpdatePage.paymentMethodSelectLastOption(),
      invoiceProjectionUpdatePage.setPaymentDateInput('01/01/2001' + protractor.Key.TAB + '02:30AM'),
      invoiceProjectionUpdatePage.setPaymentAmountInput('5'),
      invoiceProjectionUpdatePage.setAggregateIdInput('aggregateId')
    ]);
    expect(await invoiceProjectionUpdatePage.getCodeInput()).to.eq('code', 'Expected Code value to be equals to code');
    expect(await invoiceProjectionUpdatePage.getDateInput()).to.contain(
      '2001-01-01T02:30',
      'Expected date value to be equals to 2000-12-31'
    );
    expect(await invoiceProjectionUpdatePage.getDetailsInput()).to.eq('details', 'Expected Details value to be equals to details');
    expect(await invoiceProjectionUpdatePage.getPaymentDateInput()).to.contain(
      '2001-01-01T02:30',
      'Expected paymentDate value to be equals to 2000-12-31'
    );
    expect(await invoiceProjectionUpdatePage.getPaymentAmountInput()).to.eq('5', 'Expected paymentAmount value to be equals to 5');
    expect(await invoiceProjectionUpdatePage.getAggregateIdInput()).to.eq(
      'aggregateId',
      'Expected AggregateId value to be equals to aggregateId'
    );
    await invoiceProjectionUpdatePage.save();
    expect(await invoiceProjectionUpdatePage.getSaveButton().isPresent(), 'Expected save button disappear').to.be.false;

    expect(await invoiceProjectionComponentsPage.countDeleteButtons()).to.eq(
      nbButtonsBeforeCreate + 1,
      'Expected one more entry in the table'
    );
  });

  it('should delete last InvoiceProjection', async () => {
    const nbButtonsBeforeDelete = await invoiceProjectionComponentsPage.countDeleteButtons();
    await invoiceProjectionComponentsPage.clickOnLastDeleteButton();

    invoiceProjectionDeleteDialog = new InvoiceProjectionDeleteDialog();
    expect(await invoiceProjectionDeleteDialog.getDialogTitle()).to.eq('storeApp.invoiceInvoiceProjection.delete.question');
    await invoiceProjectionDeleteDialog.clickOnConfirmButton();

    expect(await invoiceProjectionComponentsPage.countDeleteButtons()).to.eq(nbButtonsBeforeDelete - 1);
  });

  after(async () => {
    await navBarPage.autoSignOut();
  });
});
