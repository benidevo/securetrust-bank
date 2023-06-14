import { container } from 'tsyringe';
import FileUploadService from '../services/FileUpload';
import S3FileUploader from '../utils/s3';

// register dependencies
container.register<FileUploadService>(FileUploadService, {
  useClass: FileUploadService,
});

container.register<S3FileUploader>(S3FileUploader, {
  useClass: S3FileUploader,
});

export default container;
