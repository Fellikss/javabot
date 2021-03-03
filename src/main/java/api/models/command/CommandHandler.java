package api.models.command;

import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;


public class CommandHandler {
    private static final Logger logger = LoggerFactory.getLogger("JDA-Command");


    public static Set<Command> commands = new HashSet<>();

    public static void registerCommands(Command... commands) {
        Collections.addAll(CommandHandler.commands, commands);
    }

    public static void registerCommand(Command command) {
        CommandHandler.registerCommands(command);
    }

    public static void unregisterCommands(Command... commands) {
        CommandHandler.commands.removeAll(Arrays.asList(commands));
    }

    public static void unregisterCommand(Command command) {
        CommandHandler.unregisterCommands(command);
    }

    public static Command findCommand(String trigger) {
        return commands.stream().filter(c -> Arrays.asList(c.getCommandData().aliases()).contains(trigger) || c.getCommandData().name().equals(trigger)).findFirst().orElse(null);
    }

    public static void doCommand(Command command, MessageReceivedEvent msg_event, String args) {
        DiscordCommand cd = command.getCommandData();
        if (cd == null) return;
        String[] arguments;
        if (args.equals("")) {
            arguments = new String[0];
        }
        else {
            if (cd.arguments() == 0 || cd.arguments() == 1) {
                arguments = new String[1];
                arguments[0] = args.replace("^[ ]*", "").trim();
            } else {
                arguments = args.replace("^[ ]*", "").trim().split(" ", cd.arguments());
            }
        }
        try {
            command.doCommand(msg_event, arguments);
        } catch (Exception error) {
            logger.error("Ошибка при выполнении команды " + cd.name() + ". " + error);
            throw new CommandException(error);
        }
    }

    public static void findAndRun(String trigger, MessageReceivedEvent msg_event, String arguments) {
        Command command = CommandHandler.findCommand(trigger);
        if (command == null || command.getCommandData() == null) return;
        CommandHandler.doCommand(command, msg_event, arguments);
    }
}