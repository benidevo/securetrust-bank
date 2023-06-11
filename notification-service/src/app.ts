import express from 'express';
import { Request, Response } from 'express';
import cors from 'cors';
import helmet from 'helmet';
import 'express-async-errors';
import 'reflect-metadata';
import environment from './config/environment';
import { morganMiddleware, systemLogs } from './utils/logger';
import NotificationServiceConsumer from './consumers/notificationServiceConsumer';
import {
  EMAIL_VERIFICATION_QUEUE,
  RESET_PASSWORD_QUEUE,
  REGISTRATION_COMPLETED_QUEUE,
} from './utils/constants';
import container from './config/container';

const verifyEmailConsumer = container.resolve(NotificationServiceConsumer);
const resetPasswordConsumer = container.resolve(NotificationServiceConsumer);
const registrationCompletedConsumer = container.resolve(
  NotificationServiceConsumer
);

export default class App {
  private app: express.Application;

  constructor() {
    this.app = express();
    this.app.use(cors());
    this.app.use(helmet());
    this.app.use(express.urlencoded({ extended: false }));
    this.app.use(express.json());
    this.app.use(morganMiddleware);
    this.setRoutes();
  }

  setRoutes() {
    this.app.get('/', async (request: Request, response: Response) => {
      response.json({
        success: true,
        message: 'Welcome To SecureTrust Bank Notification Service',
        data: null,
        error: null,
      });
    });
  }

  getApp() {
    return this.app;
  }

  async listen() {
    const { port, nodeEnv } = environment;

    this.app.listen(port, () => {
      systemLogs.info(`Server running in ${nodeEnv} mode on port ${port}`);
    });

    try {
      await verifyEmailConsumer.listenOnQueue(EMAIL_VERIFICATION_QUEUE);
      await resetPasswordConsumer.listenOnQueue(RESET_PASSWORD_QUEUE);
      await registrationCompletedConsumer.listenOnQueue(
        REGISTRATION_COMPLETED_QUEUE
      );
    } catch (error) {
      const message = `Error consuming messages: ${error}`;
      systemLogs.error(message);
    }
  }
}
