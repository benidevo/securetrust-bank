import { systemLogs } from './utils/logger';

(async () => {
  try {
    const App = require('./app').default;
    const app = new App();
    await app.listen();
  } catch (err: any) {
    systemLogs.error(`Error initializing server ${err.stack}`);
  }
})();
