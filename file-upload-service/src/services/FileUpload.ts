import { injectable } from 'tsyringe';

@injectable()
export default class FileUploadService {
  public async execute(
    type: string,
    file: Express.Multer.File
  ): Promise<string> {
    return 'https://api.fileupload.com';
  }
}
