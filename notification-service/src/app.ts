import express from 'express';
import { Request, Response } from 'express';
import cors from 'cors';
import helmet from 'helmet';
import 'express-async-errors';
import environment from './config/environment';
import logger from 'morgan';
import { morganMiddleware, systemLogs } from './utils/logger';

export default class App {
  app: express.Application;
  constructor() {
    this.app = express();
    this.app.use(
      logger('dev', {
        skip: (request: Request, response: Response) =>
          environment.nodeEnv === 'test',
      })
    );
    this.app.use(cors());
    this.app.use(helmet());
    this.app.use(express.json());
    this.app.use(morganMiddleware);
    this.setRoutes();
  }

  setRoutes() {
    this.app.get('/', async (request: Request, response: Response) => {
      response.json({
        status: true,
        message: 'Welcome To SecureTrust Bank Notification Service',
      });
    });
  }

  getApp() {
    return this.app;
  }

  listen() {
    const { port, nodeEnv } = environment;
    this.app.listen(port, () => {
        console.log(`Listening at port ${parseInt(port)}`);
        systemLogs.info(`Server running in ${nodeEnv} mode on port ${port}`);
    });
  }
}
