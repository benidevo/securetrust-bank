import 'reflect-metadata';
import express from 'express';
import cors from 'cors';
import helmet from 'helmet';
import environment from './config/environment';
import { morganMiddleware, systemLogs } from './utils/logger';
import rateLimiter from './middlewares/rateLimiter';
import errorHandler from './middlewares/errorHandler';
import routes from './routes';

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
    this.app.use('/', routes);
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
