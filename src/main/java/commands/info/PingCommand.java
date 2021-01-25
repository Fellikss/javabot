package commands.info;

import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.jetbrains.annotations.NotNull;

public class PingCommand extends ListenerAdapter {

    @Override
    public void onMessageReceived(@NotNull MessageReceivedEvent msg_event) {
        if (msg_event.getMessage().getContentRaw().equals("..ping")){
            EmbedBuilder pingEmbed = new EmbedBuilder();
            pingEmbed.setTitle("Пинг бота");
            pingEmbed.setDescription(String.valueOf(msg_event.getJDA().getResponseTotal()));
            msg_event.getMessage().reply(pingEmbed.build()).queue();
        }
    }

}
