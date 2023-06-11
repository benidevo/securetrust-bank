import amqp from 'amqplib';

export default class RabbitMQConsumer {
  protected connection: amqp.Connection;
  protected channel: amqp.Channel;

  constructor(private readonly amqpUrl: string) {}

  protected async connect(): Promise<void> {
    this.connection = await amqp.connect(this.amqpUrl);
    this.channel = await this.connection.createChannel();
  }

  protected async createQueue(queueName: string): Promise<void> {
    await this.channel.assertQueue(queueName);
  }

  protected async consume(
    queueName: string,
    handleMessage: (msg: amqp.Message) => void
  ): Promise<void> {
    await this.channel.consume(queueName, handleMessage);
  }

  protected async ackMessage(msg: amqp.Message): Promise<void> {
    this.channel.ack(msg);
  }

  protected async unackMessage(msg: amqp.Message): Promise<void> {
    this.channel.nack(msg, false, false);
  }

  protected async close(): Promise<void> {
    await this.channel.close();
    await this.connection.close();
  }
}
