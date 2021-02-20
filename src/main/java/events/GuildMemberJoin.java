package events;

import api.Database;
import api.TemplateEngine;
import api.models.database.Guild;
import net.dv8tion.jda.api.entities.Role;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.events.guild.member.GuildMemberJoinEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;

public class GuildMemberJoin extends ListenerAdapter {

    @Override
    public void onGuildMemberJoin(GuildMemberJoinEvent joinEvent) {
        Database db = new Database();
        Guild DBGuild = db.getGuildByID(joinEvent.getGuild().getIdLong());
        if (!DBGuild.getWelcomeMessage().equals("") && DBGuild.getWelcomeChannel() != null) {
            TextChannel welcomeChannel = joinEvent.getJDA().getTextChannelById(DBGuild.getWelcomeChannel());
            HashMap<String, String> values = new HashMap<>();
            values.put("member.tag", joinEvent.getMember().getUser().getAsTag());
            values.put("member.mention", joinEvent.getMember().getAsMention());
            values.put("guild.memberCount", String.valueOf(joinEvent.getGuild().getMemberCount()));
            welcomeChannel.sendMessage(TemplateEngine.render(DBGuild.getWelcomeMessage(), values)).queue();
        }
        if (DBGuild.getWelcomeRoles() != null) {
            Collection<Role> roles = new ArrayList<>();
            for (Long roleID: DBGuild.getWelcomeRoles()) {
                roles.add(joinEvent.getGuild().getRoleById(roleID));
            }
            joinEvent.getGuild().modifyMemberRoles(joinEvent.getMember(), roles).queue();
        }
    }

}