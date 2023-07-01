import dotenv from 'dotenv';

dotenv.config();

export default {
  port: process.env.PORT,
  nodeEnv: process.env.NODE_ENV,
  AWSAccessKeyId: process.env.AWS_ACCESS_KEY_ID,
  AWSSecretAccessKey: process.env.AWS_SECRET_ACCESS_KEY,
  AWSRegion: process.env.AWS_REGION,
  AWSS3EmulatorEndpoint: process.env.AWS_S3_EMULATOR_ENDPOINT,
  AWSBucketName: process.env.AWS_BUCKET_NAME,
  amqpUrl: process.env.AMQP_URL,
};
