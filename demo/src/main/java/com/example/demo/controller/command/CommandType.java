package com.example.demo.controller.command;

public enum CommandType {
    LOGIN(new LoginCommand()),
    LOGOUT(new LogoutCommand()),
    DEFAULT(new DefaultCommand()),
    REGISTER(new RegisterCommand());

    private BaseCommand command;

    CommandType(BaseCommand command) {
        this.command = command;
    }

    public BaseCommand getCommand() {
        return command;
    }

    public static BaseCommand defineCommand(String commandStr) {
        if (commandStr == null) {
            throw new IllegalArgumentException("commandStr cannot be null");
        }
        return CommandType.valueOf(commandStr.toUpperCase()).getCommand();
    }
}
