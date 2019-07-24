/* tslint:disable no-unused-expression */
import { browser, ExpectedConditions as ec, protractor, promise } from 'protractor';
import { NavBarPage, SignInPage } from '../../page-objects/jhi-page-objects';

import {
  InvoiceProjectionGtwComponentsPage,
  InvoiceProjectionGtwDeleteDialog,
  InvoiceProjectionGtwUpdatePage
} from './invoice-projection-gtw.page-object';

const expect = chai.expect;

describe('InvoiceProjectionGtw e2e test', () => {
  let navBarPage: NavBarPage;
  let signInPage: SignInPage;
  let invoiceProjectionGtwUpdatePage: InvoiceProjectionGtwUpdatePage;
  let invoiceProjectionGtwComponentsPage: InvoiceProjectionGtwComponentsPage;
  let invoiceProjectionGtwDeleteDialog: InvoiceProjectionGtwDeleteDialog;

  before(async () => {
    await browser.get('/');
    navBarPage = new NavBarPage();
    signInPage = await navBarPage.getSignInPage();
    await signInPage.loginWithOAuth('admin', 'admin');
    await browser.wait(ec.visibilityOf(navBarPage.entityMenu), 5000);
  });

  it('should load InvoiceProjectionGtws', async () => {
    await navBarPage.goToEntity('invoice-projection-gtw');
    invoiceProjectionGtwComponentsPage = new InvoiceProjectionGtwComponentsPage();
    await browser.wait(ec.visibilityOf(invoiceProjectionGtwComponentsPage.title), 5000);
    expect(await invoiceProjectionGtwComponentsPage.getTitle()).to.eq('storeApp.invoiceProjectionGtw.home.title');
  });

  it('should load create InvoiceProjectionGtw page', async () => {
    await invoiceProjectionGtwComponentsPage.clickOnCreateButton();
    invoiceProjectionGtwUpdatePage = new InvoiceProjectionGtwUpdatePage();
    expect(await invoiceProjectionGtwUpdatePage.getPageTitle()).to.eq('storeApp.invoiceProjectionGtw.home.createOrEditLabel');
    await invoiceProjectionGtwUpdatePage.cancel();
  });

  it('should create and save InvoiceProjectionGtws', async () => {
    const nbButtonsBeforeCreate = await invoiceProjectionGtwComponentsPage.countDeleteButtons();

    await invoiceProjectionGtwComponentsPage.clickOnCreateButton();
    await promise.all([
      invoiceProjectionGtwUpdatePage.setCodeInput('code'),
      invoiceProjectionGtwUpdatePage.setDateInput('01/01/2001' + protractor.Key.TAB + '02:30AM'),
      invoiceProjectionGtwUpdatePage.setDetailsInput('details'),
      invoiceProjectionGtwUpdatePage.setPaymentDateInput('01/01/2001' + protractor.Key.TAB + '02:30AM'),
      invoiceProjectionGtwUpdatePage.setPaymentAmountInput('5'),
      invoiceProjectionGtwUpdatePage.setAggregateIdInput('aggregateId')
    ]);
    expect(await invoiceProjectionGtwUpdatePage.getCodeInput()).to.eq('code', 'Expected Code value to be equals to code');
    expect(await invoiceProjectionGtwUpdatePage.getDateInput()).to.contain(
      '2001-01-01T02:30',
      'Expected date value to be equals to 2000-12-31'
    );
    expect(await invoiceProjectionGtwUpdatePage.getDetailsInput()).to.eq('details', 'Expected Details value to be equals to details');
    expect(await invoiceProjectionGtwUpdatePage.getPaymentDateInput()).to.contain(
      '2001-01-01T02:30',
      'Expected paymentDate value to be equals to 2000-12-31'
    );
    expect(await invoiceProjectionGtwUpdatePage.getPaymentAmountInput()).to.eq('5', 'Expected paymentAmount value to be equals to 5');
    expect(await invoiceProjectionGtwUpdatePage.getAggregateIdInput()).to.eq(
      'aggregateId',
      'Expected AggregateId value to be equals to aggregateId'
    );
    await invoiceProjectionGtwUpdatePage.save();
    expect(await invoiceProjectionGtwUpdatePage.getSaveButton().isPresent(), 'Expected save button disappear').to.be.false;

    expect(await invoiceProjectionGtwComponentsPage.countDeleteButtons()).to.eq(
      nbButtonsBeforeCreate + 1,
      'Expected one more entry in the table'
    );
  });

  it('should delete last InvoiceProjectionGtw', async () => {
    const nbButtonsBeforeDelete = await invoiceProjectionGtwComponentsPage.countDeleteButtons();
    await invoiceProjectionGtwComponentsPage.clickOnLastDeleteButton();

    invoiceProjectionGtwDeleteDialog = new InvoiceProjectionGtwDeleteDialog();
    expect(await invoiceProjectionGtwDeleteDialog.getDialogTitle()).to.eq('storeApp.invoiceProjectionGtw.delete.question');
    await invoiceProjectionGtwDeleteDialog.clickOnConfirmButton();

    expect(await invoiceProjectionGtwComponentsPage.countDeleteButtons()).to.eq(nbButtonsBeforeDelete - 1);
  });

  after(async () => {
    await navBarPage.autoSignOut();
  });
});
