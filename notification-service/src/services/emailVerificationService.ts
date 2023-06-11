import { injectable } from 'tsyringe';
import { systemLogs } from '../utils/logger';
import {
  EMAIL_VERIFICATION_QUEUE,
  VERIFY_EMAIL_SUBJECT,
  VERIFY_EMAIL_TEMPLATE,
} from '../utils/constants';
import { IEmailVerificationMsgDTO } from '../dtos/EmailVerificationMessage';
import EmailService from './emailService';

@injectable()
class EmailVerificationService extends EmailService {
  async processMessage(msg: string | null): Promise<void> {
    if (!msg) {
      return;
    }

    try {
      const messageData: IEmailVerificationMsgDTO = JSON.parse(msg);

      const { email } = messageData;

      await this.sendEmail(
        VERIFY_EMAIL_SUBJECT,
        messageData,
        VERIFY_EMAIL_TEMPLATE
      );

      try {
        systemLogs.info(`Verification mail sent successfully to ${email}`);
      } catch (error) {
        systemLogs.error(
          `Failed to send verification email to ${email}:  ${error}`
        );
      }
    } catch (error) {
      systemLogs.error(
        `Failed to process message on ${EMAIL_VERIFICATION_QUEUE}: ${error}`
      );
    }
  }
}

export default EmailVerificationService;
