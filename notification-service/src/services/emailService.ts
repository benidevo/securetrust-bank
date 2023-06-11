import 'dotenv/config';
import fs from 'fs';
import handlebars from 'handlebars';
import path from 'path';
import transporter from '../config/emailTransport';
import { IEmailVerificationMsgDTO } from '../dtos/EmailVerificationMessage';
import { systemLogs } from '../utils/logger';
import environment from '../config/environment';
import { IRegistrationCompletedMsgDTO } from '../dtos/RegistrationCompletedMsgDTO';
import { IPasswordResetMsgDTO } from '../dtos/PasswordResetMsgDTO';

class EmailService {
  protected async sendEmail(
    subject: string,
    payload:
      | IEmailVerificationMsgDTO
      | IRegistrationCompletedMsgDTO
      | IPasswordResetMsgDTO,
    template: string
  ) {
    try {
      const sourceDirectory = fs.readFileSync(
        path.join(__dirname, template),
        'utf-8'
      );

      const compiledTemplate = handlebars.compile(sourceDirectory);

      const emailOptions = {
        from: environment.defaultSenderEmail,
        to: payload.email,
        subject,
        html: compiledTemplate(payload),
      };

      await transporter.sendMail(emailOptions);
    } catch (error) {
      systemLogs.error(`Email not sent: ${error}`);
    }
  }
}

export default EmailService;
