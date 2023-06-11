import amqp from 'amqplib';
import RabbitMQConsumer from './rabbitMQConsumer';
import {
  EMAIL_VERIFICATION_QUEUE,
  RESET_PASSWORD_QUEUE,
} from '../utils/constants';
import environment from '../config/environment';
import { systemLogs } from '../utils/logger';
import { inject, injectable } from 'tsyringe';
import EmailVerificationService from '../services/emailVerificationService';
import PasswordResetService from '../services/passwordResetService';

const { amqpUrl } = environment;

@injectable()
export default class NotificationServiceConsumer extends RabbitMQConsumer {
  constructor(
    @inject(EmailVerificationService)
    private emailVerificationService: EmailVerificationService,
    @inject(PasswordResetService)
    private passwordResetService: PasswordResetService
  ) {
    super(amqpUrl);
  }

  async listenOnQueue(queueName: string): Promise<void> {
    try {
      await this.connect();
      await this.createQueue(queueName);
      await this.consume(queueName, this.handleMessage.bind(this, queueName));

      systemLogs.info(`Listening on queue: ${queueName}`);
    } catch (error) {
      systemLogs.error('Error consuming messages:', error);
    }
  }

  private async handleMessage(
    queueName: string,
    msg: amqp.Message
  ): Promise<void> {
    const content = msg.content.toString();
    systemLogs.info(`Received message from ${msg.fields.routingKey}`);

    switch (queueName) {
      case EMAIL_VERIFICATION_QUEUE:
        await this.emailVerificationService.processMessage(content);
        break;

      case RESET_PASSWORD_QUEUE:
        await this.passwordResetService.processMessage(content);
        break;

      default:
        systemLogs.warn(`No service found for queue: ${queueName}`);
    }

    await this.ackMessage(msg);
  }
}
