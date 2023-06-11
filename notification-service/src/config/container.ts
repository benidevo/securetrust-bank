import { container } from 'tsyringe';
import NotificationServiceConsumer from '../consumers/notificationServiceConsumer';
import EmailVerificationService from '../services/emailVerificationService';
import PasswordResetService from '../services/passwordResetService';

// Register the dependencies
container.register<NotificationServiceConsumer>(NotificationServiceConsumer, {
  useClass: NotificationServiceConsumer,
});

container.register<EmailVerificationService>(EmailVerificationService, {
  useClass: EmailVerificationService,
});

container.register<PasswordResetService>(PasswordResetService, {
  useClass: PasswordResetService,
});

export default container;
