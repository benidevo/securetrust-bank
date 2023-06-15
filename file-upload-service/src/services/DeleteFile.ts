import { inject, injectable } from 'tsyringe';
import S3FileUploader from '../utils/s3';
import environment from '../config/environment';

const { AWSBucketName } = environment;

interface IPayload {
  file_url: string;
}

@injectable()
export default class DeleteFileService {
  private awsBucketName: string;

  constructor(@inject(S3FileUploader) private _uploader: S3FileUploader) {
    this.awsBucketName = AWSBucketName;
  }

  async execute(msg: string): Promise<void> {
    const payload: IPayload = JSON.parse(msg);
    const { file_url } = payload;
    const fileKey = file_url.split(`${this.awsBucketName}/`)[1];

    await this._uploader.deleteFile(fileKey);
  }
}
