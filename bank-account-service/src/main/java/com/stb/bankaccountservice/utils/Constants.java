package com.stb.bankaccountservice.utils;

import java.math.BigDecimal;


public class Constants {
    public static final String BASIC_ACCOUNT_TYPE = "BASIC";
    public static final BigDecimal BASIC_ACCOUNT_TYPE_TRANSACTION_LIMIT = new BigDecimal("100000.0");
    public static final String CREATE_BANK_ACCOUNT_QUEUE = "create_bank_account_queue";
    public static final String NEW_BANK_ACCOUNT_NOTIFICATION_QUEUE = "new_bank_account_notification_queue";
    public static  final String ADMIN_ROLE = "ADMIN";
    public static final String USER_ROLE = "USER";
}
