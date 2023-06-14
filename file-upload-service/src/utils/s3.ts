import AWS from 'aws-sdk';
import { S3 } from 'aws-sdk/clients/all';
import { injectable } from 'tsyringe';
import AppError from './appError';
import environment from '../config/environment';
import { systemLogs } from './logger';

const {
  AWSAccessKeyId,
  AWSSecretAccessKey,
  AWSRegion,
  AWSS3EmulatorEndpoint,
  AWSBucketName,
  nodeEnv,
} = environment;

interface UploadedFile {
  fieldname: string;
  originalname: string;
  encoding: string;
  mimetype: string;
  buffer: Buffer;
  size: number;
}

@injectable()
class S3FileUploader {
  private s3: S3;
  private bucketName: string;

  constructor() {
    const config = {
      accessKeyId: AWSAccessKeyId,
      secretAccessKey: AWSSecretAccessKey,
      region: AWSRegion,
    };

    if (nodeEnv == 'development') {
      config['endpoint'] = new AWS.Endpoint(AWSS3EmulatorEndpoint);
      config['s3ForcePathStyle'] = true;
    }

    this.s3 = new AWS.S3(config);
    this.bucketName = AWSBucketName;
  }

  private async bucketExists(bucketName: string): Promise<boolean> {
    try {
      await this.s3.headBucket({ Bucket: bucketName }).promise();
      return true;
    } catch (error) {
      if (error.code === 'NotFound') {
        return false;
      } else {
        systemLogs.error(error);
        throw new AppError('Internal server error', 500);
      }
    }
  }

  async uploadFile(
    bucketName: string,
    buffer: Buffer,
    keyName: string
  ): Promise<AWS.S3.ManagedUpload.SendData> {
    const uploadParams = {
      Bucket: bucketName,
      Key: keyName,
      Body: buffer,
    };

    return this.s3.upload(uploadParams).promise();
  }

  async createBucketIfNotExists(bucketName: string): Promise<void> {
    const bucketExists = await this.bucketExists(bucketName);

    if (!bucketExists) {
      await this.s3.createBucket({ Bucket: bucketName }).promise();
      systemLogs.info(`Bucket "${bucketName}" created successfully`);
    }
  }

  async executeUpload(type: string, file: UploadedFile): Promise<string> {
    const keyName = `${type}/${file.originalname}`;

    try {
      await this.createBucketIfNotExists(this.bucketName);

      const response = await this.uploadFile(
        this.bucketName,
        file.buffer,
        keyName
      );

      if (nodeEnv === 'development') {
        const file_url = response.Location;
        return file_url.replace('aws-emulator', 'localhost');
      }

      return response.Location;
    } catch (error) {
      systemLogs.error('Error uploading file:', error);
      throw new AppError('Internal server error', 500);
    }
  }
}

export default S3FileUploader;
