import { Moment } from 'moment';

export interface IInvoiceProjectionGtw {
  id?: number;
  code?: string;
  date?: Moment;
  details?: string;
  paymentDate?: Moment;
  paymentAmount?: number;
  aggregateId?: string;
}

export class InvoiceProjectionGtw implements IInvoiceProjectionGtw {
  constructor(
    public id?: number,
    public code?: string,
    public date?: Moment,
    public details?: string,
    public paymentDate?: Moment,
    public paymentAmount?: number,
    public aggregateId?: string
  ) {}
}
