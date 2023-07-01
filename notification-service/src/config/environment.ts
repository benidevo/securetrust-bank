import dotenv from 'dotenv';

dotenv.config();

export default {
  port: process.env.PORT,
  nodeEnv: process.env.NODE_ENV,
  amqpUrl: process.env.AMQP_URL,,
  defaultSenderEmail: process.env.DEFAULT_SENDER_EMAIL,
  mailhogServer: process.env.MAILHOG_SERVER,
  mailhogPort: parseInt(process.env.MAILHOG_PORT),
};
