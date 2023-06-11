import { IPasswordResetMsgDTO } from '../dtos/PasswordResetMsgDTO';
import EmailService from './emailService';
import {
  RESET_PASSWORD_EMAIL_SUBJECT,
  RESET_PASSWORD_EMAIL_TEMPLATE,
  RESET_PASSWORD_QUEUE,
} from '../utils/constants';
import { systemLogs } from '../utils/logger';
import { injectable } from 'tsyringe';

@injectable()
class PasswordResetService extends EmailService {
  async processMessage(msg: string | null): Promise<void> {
    if (!msg) {
      return;
    }

    let messageData: IPasswordResetMsgDTO;

    try {
      messageData = JSON.parse(msg);
    } catch (error) {
      systemLogs.error(
        `Failed to process message on ${RESET_PASSWORD_QUEUE}: ${error}`
      );
    }

    const { email } = messageData;
    try {
      await this.sendEmail(
        RESET_PASSWORD_EMAIL_SUBJECT,
        messageData,
        RESET_PASSWORD_EMAIL_TEMPLATE
      );

      systemLogs.info(`Reset password mail sent successfully to ${email}`);
    } catch (error) {
      systemLogs.error(
        `Failed to send resend password email to ${email}:  ${error}`
      );
    }

  }
}

export default PasswordResetService;
