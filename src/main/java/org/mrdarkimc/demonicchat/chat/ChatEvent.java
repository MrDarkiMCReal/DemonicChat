package org.mrdarkimc.demonicchat.chat;

import me.clip.placeholderapi.PlaceholderAPI;
import net.md_5.bungee.api.chat.*;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.Location;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.mrdarkimc.demonicchat.DemonicChat;
import org.mrdarkimc.demonicchat.chatformatter.Tag;

import java.util.*;
import java.util.List;
import java.util.stream.Collectors;

public class ChatEvent implements Listener {
    public ChatEvent() {
        //
    }

    char globalSymbol = '!';
    char globalSymbolToText = 'G';
    TextComponent chatTypeIcon = new TextComponent(String.valueOf(globalSymbolToText));
    TextComponent prefix = new TextComponent();
    TextComponent nick = new TextComponent();
    TextComponent clan = new TextComponent();
    TextComponent finalMessage = new TextComponent();

    public enum PlayerGroup {
        defaultGroup, fury, hellhound, leviathan, nephilim, blackfury;
    }

    private List<Tag> taglist;

    public DemonicChat getPlugin() {
        return DemonicChat.getInstance();
    }

    char[] restrictedSymbols = new char[]{'2', '1'}; //todo с помощью регекса удалять все ненужное
    int radius = 100;

    @EventHandler
    public void onChat(AsyncPlayerChatEvent e) {
        e.setCancelled(true);
        if (e.getMessage().startsWith(String.valueOf(globalSymbol))) {
            getPlugin().getServer().spigot().broadcast(deserealizeChatTag(e.getPlayer(), e.getMessage(), true));

            getPlugin().getLogger().info("Sending msg to all players");
            //todo работающий прототип
//            TextComponent component = new TextComponent("PENIS");
//            component.setColor(ChatColor.of("#755c48"));
//            TextComponent component2 = new TextComponent("penis2");
//            component2.setColor(ChatColor.of("#755c48"));
//            TextComponent component3 = new TextComponent();
//            component3.setText("penis3");
//            component3.setColor(ChatColor.of("#755c48"));
//            getPlugin().getServer().spigot().broadcast(component);
//            getPlugin().getServer().spigot().broadcast(component2);
//            getPlugin().getServer().spigot().broadcast(component3);


        } else {
            getPlugin().getServer().getScheduler().runTask(getPlugin(), () -> {


                Location loc = e.getPlayer().getLocation();
                Set<Player> playerList = loc.getWorld().getNearbyEntities(loc, radius, radius, radius).stream()
                        .filter(entity -> entity instanceof Player)
                        .map(entity -> (Player) entity)
                        .collect(Collectors.toSet());

                //local
                for (Player player : playerList) {
                    player.spigot().sendMessage(deserealizeChatTag(e.getPlayer(), e.getMessage(), false));
                    getPlugin().getServer().getOperators();
                    //todo iterate to OPs
                    getPlugin().getLogger().info("Sending local chat to " + playerList.size() + " players");
                }
            });
        }

    }
    public String getRidOfAnything(String message) {
        //todo добавить логирование что удалено.
        if (message.startsWith("!")) {
            message = message.substring(1);
        }
        return message.replaceAll("penis", "");
    }

    public String getClanName(Player player) {
        return " SATANIC ";
    }


    public TextComponent deserealizeChatTag(Player player, String message, boolean isGlobal) {

        Set<String> keys = getPlugin().getConfig().getConfigurationSection("tags").getKeys(false);
        List<TextComponent> componentList = new ArrayList<>();
        for (String key : keys) {
            HoverEvent hoverEvent = null;
            ClickEvent clickEvent = null;
            String text = null;


            Set<String> setOfTags = getPlugin().getConfig().getConfigurationSection("tags." + key).getKeys(false);
            for (String subKey : setOfTags) {
                switch (subKey) {
                    case "text":
                        String configText = getPlugin().getConfig().getString("tags." + key + ".text") //%luckperms%
                                .replaceAll("\\{message}", message)
                                .replaceAll("\\{playername}",player.getName())
                                .replaceAll("\\{clan}",getClanName(player));
                        text = PlaceholderAPI.setPlaceholders(player, configText);
                        break;
                    case "hover-event":
                        List<String> hoverMessage = getPlugin().getConfig().getStringList("tags." + key + ".hover-event.text");
                        hoverMessage.replaceAll((line) -> PlaceholderAPI.setPlaceholders(player,line));
                        hoverEvent = new HoverEvent(HoverEvent.Action.SHOW_TEXT, Tag.stringListToContentList(hoverMessage));
                        break;
                    case "click-event":
                        ConfigurationSection section = getPlugin().getConfig().getConfigurationSection("tags." + key + ".click-event");
                        String action = section.getString("type");
                        String output = section.getString("output");
                        clickEvent = new ClickEvent(ClickEvent.Action.valueOf(action), output);
                        break;
                    default: //skip
                        break;
                }

            }
            TextComponent component = new Tag(player, text, clickEvent, hoverEvent).getComponent();
            componentList.add(component);

        }


        return stackComponents(componentList);
    }
    public TextComponent stackComponents(List<TextComponent> componentList){
        TextComponent previous = null;
        for (int i = componentList.size()-1; i >= 0; i--) {
           if (i==0) {
               return previous;
           }
               TextComponent last = previous==null ? componentList.get(i) : previous;
               TextComponent stackToMe = componentList.get(i-1);
               stackToMe.addExtra(last);
               previous = stackToMe;
        }
        return previous!= null ? previous : new TextComponent("");
    }

}
