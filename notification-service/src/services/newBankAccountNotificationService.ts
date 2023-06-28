import { injectable } from 'tsyringe';
import EmailService from './emailService';
import { INewBankAccountNotifMsg } from '../dtos/INewBankAccountNotifMsg';
import { systemLogs } from '../utils/logger';
import {
  NEW_BANK_ACCOUNT_NOTIFICATION_QUEUE,
  NEW_BANK_ACCOUNT_NOTIFICATION_EMAIL_SUBJECT,
  NEW_BANK_ACCOUNT_NOTIFICATION_EMAIL_TEMPLATE,
} from '../utils/constants';

@injectable()
export default class NewBankAccountNotificationService extends EmailService {
  async processMessage(msg: string | null): Promise<void> {
    if (!msg) return;

    let message: INewBankAccountNotifMsg;

    try {
      message = JSON.parse(msg);
    } catch (error) {
      systemLogs.error(
        `Failed to process message on ${NEW_BANK_ACCOUNT_NOTIFICATION_QUEUE}: ${error}`
      );
    }

    try {
      await this.sendEmail(
        NEW_BANK_ACCOUNT_NOTIFICATION_EMAIL_SUBJECT,
        message,
        NEW_BANK_ACCOUNT_NOTIFICATION_EMAIL_TEMPLATE
      );
      systemLogs.info(
        `New bank account notification mail sent successfully to ${message?.email}`
      );
    } catch (error) {
      systemLogs.error(
        `Failed to send new bank account notification email to ${message?.email}:  ${error}`
      );
    }
  }
}
