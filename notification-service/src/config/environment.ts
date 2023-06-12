import dotenv from 'dotenv';

dotenv.config();

export default {
  port: process.env.PORT || '3000',
  nodeEnv: process.env.NODE_ENV || 'development',
  amqpUrl: process.env.AMQP_URL || 'amqp://secureTrust:Test4321@rabbitmq:5672',
  defaultSenderEmail: process.env.DEFAULT_SENDER_EMAIL || 'info@stb.com',
  mailhogServer: process.env.MAILHOG_SERVER || 'email-server',
  mailhogPort: parseInt(process.env.MAILHOG_PORT) || 1025,
};
