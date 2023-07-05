package com.stb.bankaccountservice.rabbitMQ.consumers;

import com.stb.bankaccountservice.dtos.CreateBankAccountDTO;
import com.stb.bankaccountservice.services.BankAccountService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.rabbit.annotation.RabbitListeners;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import com.fasterxml.jackson.databind.ObjectMapper;

import static com.stb.bankaccountservice.utils.Constants.CREATE_BANK_ACCOUNT_QUEUE;

@Component
@Slf4j
@RabbitListeners({
        @RabbitListener(queuesToDeclare = @org.springframework.amqp.rabbit.annotation.Queue(
                value = CREATE_BANK_ACCOUNT_QUEUE,
                durable = "true",
                autoDelete = "false"
        ))
})
public class CreateBankAccountConsumer {
    private final BankAccountService bankAccountService;

    @Autowired
    public CreateBankAccountConsumer(BankAccountService bankAccountService) {
        this.bankAccountService = bankAccountService;
    }

    @RabbitListener(queues = CREATE_BANK_ACCOUNT_QUEUE)
    public void receiveMessage(String message) {
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            CreateBankAccountDTO myData = objectMapper.readValue(message, CreateBankAccountDTO.class);
            bankAccountService.create(myData);
            log.info("Created bank account for user: " + myData.getUserId());
        } catch (Exception e) {
            log.error("Failed to create bank account", e);
        }
    }
}
