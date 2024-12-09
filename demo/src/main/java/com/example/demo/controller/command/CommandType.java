package com.example.demo.controller.command;

public enum CommandType {
    LOGIN(new LoginCommand()),
    LOGOUT(new LogoutCommand()),
    DEFAULT(new DefaultCommand());

    private BaseCommand command;

    CommandType(BaseCommand command) {
        this.command = command;
    }

    public BaseCommand getCommand() {
        return command;
    }

    public static BaseCommand defineCommand(String commandStr) {
        return CommandType.valueOf(commandStr.toUpperCase()).getCommand();// todo
    }
}
