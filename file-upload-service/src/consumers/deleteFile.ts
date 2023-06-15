import { inject, injectable } from 'tsyringe';
import amqp from 'amqplib';
import RabbitMQConsumer from './rabbitMQConsumer';
import DeleteFileService from '../services/DeleteFile';
import environment from '../config/environment';
import { systemLogs } from '../utils/logger';
import { DELETE_UPLOADED_FILE_QUEUE } from '../utils/constants';

const { amqpUrl } = environment;

@injectable()
export default class DeleteFileConsumer extends RabbitMQConsumer {
  constructor(
    @inject(DeleteFileService)
    private readonly deleteFileService: DeleteFileService
  ) {
    super(amqpUrl);
  }

  public async listenOnQueue(queueName: string): Promise<void> {
    try {
      await this.connect();
      await this.createQueue(queueName);
      await this.consume(queueName, this.handleMessage.bind(this, queueName));

      systemLogs.info(`Listening on queue: ${queueName}`);
    } catch (err) {
      systemLogs.error(`Error consuming message on ${queueName}: `, err);
    }
  }

  private async handleMessage(
    queueName: string,
    msg: amqp.Message
  ): Promise<void> {
    const content = msg.content.toString();
    systemLogs.info(`Received message from ${msg.fields.routingKey}`);

    switch (queueName) {
      case DELETE_UPLOADED_FILE_QUEUE:
        await this.deleteFileService.execute(content);
      default:
        systemLogs.warn(`No service found for queue: ${queueName}`);
    }

    await this.ackMessage(msg);
  }
}
