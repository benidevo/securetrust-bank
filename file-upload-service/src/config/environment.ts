import dotenv from 'dotenv';

dotenv.config();

export default {
  port: process.env.PORT || '3500',
  nodeEnv: process.env.NODE_ENV || 'development',
  AWSAccessKeyId: process.env.AWS_ACCESS_KEY_ID || 'AWS_ACCESS_KEY_ID',
  AWSSecretAccessKey:
    process.env.AWS_SECRET_ACCESS_KEY || 'AWS_SECRET_ACCESS_KEY',
  AWSRegion: process.env.AWS_REGION || 'eu-west-1',
  AWSS3EmulatorEndpoint: 'http://aws-emulator:4566',
  AWSBucketName: process.env.AWS_BUCKET_NAME || 'my-bucket',
};
