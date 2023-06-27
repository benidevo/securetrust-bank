package com.stb.bankaccountservice.config;

import io.github.cdimascio.dotenv.Dotenv;

public class Environment {
    private static final Dotenv env = Dotenv.load();

    public static final String RABBITMQ_HOST = env.get("RABBITMQ_HOST");
    public static final int RABBITMQ_PORT = Integer.parseInt(env.get("RABBITMQ_PORT"));
    public static final String RABBITMQ_USERNAME = env.get("RABBITMQ_USERNAME");
    public static final String RABBITMQ_PASSWORD = env.get("RABBITMQ_PASSWORD");
    public static final String RABBITMQ_VIRTUAL_HOST = env.get("RABBITMQ_VIRTUAL_HOST");
}
