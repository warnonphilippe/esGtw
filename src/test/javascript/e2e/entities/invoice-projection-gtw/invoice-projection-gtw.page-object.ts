import { browser, ExpectedConditions, element, by, ElementFinder } from 'protractor';

export class InvoiceProjectionGtwComponentsPage {
  createButton = element(by.id('jh-create-entity'));
  deleteButtons = element.all(by.css('jhi-invoice-projection-gtw div table .btn-danger'));
  title = element.all(by.css('jhi-invoice-projection-gtw div h2#page-heading span')).first();

  async clickOnCreateButton(timeout?: number) {
    await this.createButton.click();
  }

  async clickOnLastDeleteButton(timeout?: number) {
    await this.deleteButtons.last().click();
  }

  async countDeleteButtons() {
    return this.deleteButtons.count();
  }

  async getTitle() {
    return this.title.getAttribute('jhiTranslate');
  }
}

export class InvoiceProjectionGtwUpdatePage {
  pageTitle = element(by.id('jhi-invoice-projection-gtw-heading'));
  saveButton = element(by.id('save-entity'));
  cancelButton = element(by.id('cancel-save'));
  codeInput = element(by.id('field_code'));
  dateInput = element(by.id('field_date'));
  detailsInput = element(by.id('field_details'));
  paymentDateInput = element(by.id('field_paymentDate'));
  paymentAmountInput = element(by.id('field_paymentAmount'));
  aggregateIdInput = element(by.id('field_aggregateId'));

  async getPageTitle() {
    return this.pageTitle.getAttribute('jhiTranslate');
  }

  async setCodeInput(code) {
    await this.codeInput.sendKeys(code);
  }

  async getCodeInput() {
    return await this.codeInput.getAttribute('value');
  }

  async setDateInput(date) {
    await this.dateInput.sendKeys(date);
  }

  async getDateInput() {
    return await this.dateInput.getAttribute('value');
  }

  async setDetailsInput(details) {
    await this.detailsInput.sendKeys(details);
  }

  async getDetailsInput() {
    return await this.detailsInput.getAttribute('value');
  }

  async setPaymentDateInput(paymentDate) {
    await this.paymentDateInput.sendKeys(paymentDate);
  }

  async getPaymentDateInput() {
    return await this.paymentDateInput.getAttribute('value');
  }

  async setPaymentAmountInput(paymentAmount) {
    await this.paymentAmountInput.sendKeys(paymentAmount);
  }

  async getPaymentAmountInput() {
    return await this.paymentAmountInput.getAttribute('value');
  }

  async setAggregateIdInput(aggregateId) {
    await this.aggregateIdInput.sendKeys(aggregateId);
  }

  async getAggregateIdInput() {
    return await this.aggregateIdInput.getAttribute('value');
  }

  async save(timeout?: number) {
    await this.saveButton.click();
  }

  async cancel(timeout?: number) {
    await this.cancelButton.click();
  }

  getSaveButton(): ElementFinder {
    return this.saveButton;
  }
}

export class InvoiceProjectionGtwDeleteDialog {
  private dialogTitle = element(by.id('jhi-delete-invoiceProjectionGtw-heading'));
  private confirmButton = element(by.id('jhi-confirm-delete-invoiceProjectionGtw'));

  async getDialogTitle() {
    return this.dialogTitle.getAttribute('jhiTranslate');
  }

  async clickOnConfirmButton(timeout?: number) {
    await this.confirmButton.click();
  }
}
