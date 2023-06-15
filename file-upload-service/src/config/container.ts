import { container } from 'tsyringe';
import FileUploadService from '../services/FileUpload';
import S3FileUploader from '../utils/s3';
import DeleteFileService from '../services/DeleteFile';

// register dependencies
container.register<FileUploadService>(FileUploadService, {
  useClass: FileUploadService,
});

container.register<S3FileUploader>(S3FileUploader, {
  useClass: S3FileUploader,
});

container.register<DeleteFileService>(DeleteFileService, {
  useClass: DeleteFileService,
});
export default container;
