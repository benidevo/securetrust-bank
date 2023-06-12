import express from 'express';
import { Request, Response } from 'express';
import cors from 'cors';
import helmet from 'helmet';
import 'reflect-metadata';
import environment from './config/environment';
import { morganMiddleware, systemLogs } from './utils/logger';
import rateLimiter from './middlewares/rateLimiter';
import errorHandler from './middlewares/errorHandler';

export default class App {
  private app: express.Application;

  constructor() {
    this.app = express();
    this.app.use(cors());
    this.app.use(helmet());
    this.app.use(express.urlencoded({ extended: false }));
    this.app.use(express.json());
    this.app.use(rateLimiter);
    this.app.use(morganMiddleware);
    this.setRoutes();
    this.app.use(errorHandler);
  }

  setRoutes() {
    this.app.get(
      '/api/v1/uploads',
      async (request: Request, response: Response) => {
        response.json({
          success: true,
          message: 'Welcome To SecureTrust Bank File Upload Service',
          data: null,
          error: null,
        });
      }
    );
  }

  getApp() {
    return this.app;
  }

  async listen() {
    const { port, nodeEnv } = environment;

    this.app.listen(port, () => {
      systemLogs.info(`Server running in ${nodeEnv} mode on port ${port}`);
    });
  }
}
