package pro.sky.telegrambot.command;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class UnknownCommand implements Command {

    private final String message;

    @Override
    public String getMessage() {
        return message;
    }
}
