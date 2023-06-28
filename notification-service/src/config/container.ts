import { container } from 'tsyringe';
import NotificationServiceConsumer from '../consumers/notificationServiceConsumer';
import EmailVerificationService from '../services/emailVerificationService';
import PasswordResetService from '../services/passwordResetService';
import RegistrationCompletedService from '../services/registrationCompletedService';
import NewBankAccountNotificationService from '../services/newBankAccountNotificationService';

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

container.register<RegistrationCompletedService>(RegistrationCompletedService, {
  useClass: RegistrationCompletedService,
});

container.register<NewBankAccountNotificationService>(NewBankAccountNotificationService, {
  useClass: NewBankAccountNotificationService,
})

export default container;
