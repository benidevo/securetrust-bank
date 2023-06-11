import { IEmailVerificationMsgDTO } from 'src/dtos/EmailVerificationMessage';
import { systemLogs } from '../utils/logger';

class EmailService {
  protected async sendEmail(payload: IEmailVerificationMsgDTO): Promise<void> {
    // Implement the email sending logic here
    throw new Error('Method not implemented.');
  }
}

export default EmailService;
