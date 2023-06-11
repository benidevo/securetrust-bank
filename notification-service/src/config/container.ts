import { container } from 'tsyringe';
import NotificationServiceConsumer from '../consumers/notificationServiceConsumer';
import EmailVerificationService from '../services/emailVerificationService';

// Register the dependencies
container.register<NotificationServiceConsumer>(NotificationServiceConsumer, {
  useClass: NotificationServiceConsumer,
});

container.register<EmailVerificationService>(EmailVerificationService, {
  useClass: EmailVerificationService,
});

export default container;
