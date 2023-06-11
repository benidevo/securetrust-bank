export default {
  port: process.env.PORT || '3000',
  nodeEnv: process.env.NODE_ENV || 'development',
  amqpUrl: process.env.AMQP_URL || 'amqp://secureTrust:Test4321@rabbitmq:5672',
};
