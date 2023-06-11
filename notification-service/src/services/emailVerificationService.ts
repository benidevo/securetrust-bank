import { injectable } from 'tsyringe';
import { systemLogs } from '../utils/logger';
import { EMAIL_VERIFICATION_QUEUE } from '../utils/constants';
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

      // Process the email notification
      // email template

      // Send the email to the user
      await this.sendEmail(messageData);

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
