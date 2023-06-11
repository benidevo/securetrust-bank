import { injectable } from 'tsyringe';
import EmailService from './emailService';
import { IRegistrationCompletedMsgDTO } from '../dtos/RegistrationCompletedMsgDTO';
import { systemLogs } from '../utils/logger';
import {
  REGISTRATION_COMPLETED_QUEUE,
  REGISTRATION_COMPLETED__EMAIL_SUBJECT,
  REGISTRATION_COMPLETED__EMAIL_TEMPLATE,
} from '../utils/constants';

@injectable()
export default class RegistrationCompletedService extends EmailService {
  async processMessage(msg: string | null): Promise<void> {
    if (!msg) return;

    let messageData: IRegistrationCompletedMsgDTO;

    try {
      messageData = JSON.parse(msg);
    } catch (error) {
      systemLogs.error(
        `Failed to process message on ${REGISTRATION_COMPLETED_QUEUE}: ${error}`
      );
    }

    const { email } = messageData;
    try {
      await this.sendEmail(
        REGISTRATION_COMPLETED__EMAIL_SUBJECT,
        messageData,
        REGISTRATION_COMPLETED__EMAIL_TEMPLATE
      );
      systemLogs.info(
        `Registration completed mail sent successfully to ${email}`
      );
    } catch (error) {
      systemLogs.error(
        `Failed to send registration completed email to ${email}:  ${error}`
      );
    }
  }
}
