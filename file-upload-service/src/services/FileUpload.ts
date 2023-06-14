import { inject, injectable } from 'tsyringe';
import S3FileUploader from '../utils/s3';

interface UploadedFile {
  fieldname: string;
  originalname: string;
  encoding: string;
  mimetype: string;
  buffer: Buffer;
  size: number;
}

@injectable()
export default class FileUploadService {
  constructor(@inject(S3FileUploader) private _uploader: S3FileUploader) {}

  public async execute(type: string, file: UploadedFile): Promise<string> {
    const fileUrl = await this._uploader.executeUpload(type, file);
    return fileUrl;
  }
}
