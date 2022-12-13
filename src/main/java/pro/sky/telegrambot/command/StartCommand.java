package pro.sky.telegrambot.command;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class StartCommand implements Command {

    private final String username;

    @Override
    public String getMessage() {
        return "Hello, %s%nTo create notification enter message in the following format: dd.MM.yyyy HH:mm <Your notification>"
                .formatted(username);
    }
}
