package org.mrdarkimc.demonicchat.chat;

import me.clip.placeholderapi.PlaceholderAPI;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.chat.*;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.chat.hover.content.Text;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.event.player.PlayerChatEvent;
import org.mrdarkimc.demonicchat.DemonicChat;
import org.mrdarkimc.demonicchat.chatformatter.Tag;
import org.mrdarkimc.demonicchat.hooks.VaultHook;
import org.mrdarkimc.demonicchat.utils.Utils;

import javax.sql.rowset.spi.TransactionalWriter;
import java.lang.reflect.Array;
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
            getPlugin().getServer().spigot().broadcast(newText(e.getPlayer(), e.getMessage(), true));

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
                    player.spigot().sendMessage(newText(e.getPlayer(), e.getMessage(), false));
                    getPlugin().getServer().getOperators();
                    //todo iterate to OPs
                    getPlugin().getLogger().info("Sending local chat to " + playerList.size() + " players");
                }
            });
        }

    }

    public static int hexToRgb(String hex) {
        if (hex.startsWith("#")) {
            hex = hex.substring(1);
        }
        int rgb = Integer.parseInt(hex, 16);

        return rgb;
    }

    public static String rgbToHex(int rgb) {
        int red = (rgb >> 16) & 0xFF;
        int green = (rgb >> 8) & 0xFF;
        int blue = rgb & 0xFF;

        return String.format("#%02X%02X%02X", red, green, blue);
    }

    public void clearTags() {
        chatTypeIcon = null;
        ComponentStyle style = new ComponentStyle();
        style.setColor(ChatColor.BLACK);
        prefix = null;
        nick = null;
        clan = null;
        finalMessage = null;
    }

    //    public String formatMessage(Player player, String message, boolean isGlobal){
//        String chatTypeIcon = !isGlobal ? String.valueOf(globalSymbolToText) : "";
//        String prefix = VaultHook.getChat().getPlayerPrefix(player); //todo прогнать все через PAPI
//        String nick = player.getDisplayName();
//        String clan = getClanName(player);
//        String messageColour = getMessageColor();
//        message = getRidOfAnything(message);
//
//        String formattedText = chatTypeIcon +" "+ prefix + nick + " " + clan + " " + messageColour + message;
//
//        //chatTypeIcon prefix nick clanIcon message
//
//    }
    public String getMessageColor() {
        return Utils.translateHex("&7");
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

    //    public TextComponent msg(Player player, String message, boolean isGlobal){
//        String finMsg = "SUPER PENIS";
//        TextComponent textComponent = new TextComponent(finMsg);
//        textComponent.setColor(ChatColor.of("#bef574"));
//
//        TextComponent textComponent2 = new TextComponent(finMsg);
//        textComponent2.setColor(ChatColor.of("#bef574"));
//        textComponent.addExtra(textComponent2);
//
//
//        HoverEvent playerHE = new HoverEvent(HoverEvent.Action.SHOW_TEXT, new Text(textComponent));
//
//        //todo вынести в поле
//
//        ComponentStyle style = new ComponentStyle();
//        style.setColor(ChatColor.of("#565BA7"));
//        TextComponent chatTypeIcon = new TextComponent(isGlobal ? String.valueOf(globalSymbolToText) : "");
//                chatTypeIcon.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new Text(Utils.translateHex("&#565BA7Глобальный чат"))));
//        TextComponent prefix = new TextComponent(Utils.translateHex(VaultHook.getChat().getPlayerPrefix(player)));
//                prefix.setHoverEvent(playerHE);
//                prefix.setStyle(style);
//        TextComponent nick = new TextComponent(Utils.translateHex(player.getDisplayName()));
//                nick.setHoverEvent(playerHE);
//        TextComponent clan = new TextComponent(getClanName(player));
//                clan.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new Text(Utils.translateHex("&#565BA7Клан игрока"))));
//        TextComponent finalMessage = new TextComponent(getRidOfAnything(Utils.translateHex(message)));
//                finalMessage.setHoverEvent(playerHE);
//
//        String messageColour = getMessageColor();
//
//
//
//        //builing tags
//        chatTypeIcon.addExtra("");
//        chatTypeIcon.addExtra(prefix);
//        chatTypeIcon.addExtra(nick);
//        chatTypeIcon.addExtra("");
//        chatTypeIcon.addExtra(clan);
//        chatTypeIcon.addExtra("");
//        chatTypeIcon.addExtra(messageColour);
//        chatTypeIcon.addExtra(finalMessage);
//        return chatTypeIcon;
//    }
    public TextComponent newText(Player player, String message, boolean isGlobal) {

        TextComponent textComponent = new TextComponent();
        Set<String> keys = getPlugin().getConfig().getConfigurationSection("tags").getKeys(false);
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
            textComponent.addExtra(new Tag(player, text, clickEvent, hoverEvent).getComponent());
        }



        return textComponent;
    }
    public List<String> getAllTextsAsStrings(){
        List<String> list = new ArrayList<>();
        Set<String> keys = getPlugin().getConfig().getConfigurationSection("tags").getKeys(false);
        for (String key : keys) {
            String text = getPlugin().getConfig().getString("tags." + key + ".text");
            list.add(text);
        }
        return list;
    }
    public void handleTexts(List<String> list){
        for (String string : list) {
            TextComponent comp = Tag.translateComponentHex(string);
        }
    }
    //        HoverEvent hoverEvent = null;
//        ClickEvent clickEvent = null;
//        String text = null;
//
//        for (String key : keys) {
//            switch (key){
//                case "text":
//                    String configText = getPlugin().getConfig().getString("tags." + key + ".text").replaceAll("\\{message}",message);
//                    text = Utils.translateHex(PlaceholderAPI.setPlaceholders(player,configText));
//                case "hover-event":
//                    List<String> hoverMessage = getPlugin().getConfig().getStringList("tags." + key + ".hover-event.text");
//                    hoverEvent = new HoverEvent(HoverEvent.Action.SHOW_TEXT,Tag.stringListToContentList(hoverMessage));
//                case "click-event":
//                    ConfigurationSection section = getPlugin().getConfig().getConfigurationSection("tags." + key + ".click-event");
//                    String action = section.getString("type");
//                    String output = section.getString("output");
//                    clickEvent = new ClickEvent(ClickEvent.Action.valueOf(action),output);
//                default: //skip
//            }
//            textComponent.addExtra(new Tag(player,text,clickEvent,hoverEvent).getComponent());


//        textComponent.addExtra(new TextComponent(isGlobal ? String.valueOf(globalSymbolToText) : ""));
//        textComponent.addExtra(new Tag(player,VaultHook.getChat().getPlayerPrefix(player),hover).getComponent());
//        textComponent.addExtra(new Tag(player,player.getDisplayName(),hover).getComponent());
//        textComponent.addExtra(new Tag(player,getClanName(player),hover).getComponent());
//        textComponent.addExtra(new Tag(player,message,hover).getComponent());

//        HoverEvent event = new HoverEvent(HoverEvent.Action.SHOW_TEXT,Tag.stringListToContentList(hover));
//        Tag.stringListToContentList(hover).forEach(event::addContent);
//    public void format(){
//        TextComponent message = new TextComponent("Do you want to accept or decline? ");
//
//        String[] strings = new String[]{"penis","penis2"};
//        TextComponent acceptButton = new TextComponent(Arrays.toString(strings));
//        acceptButton.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new Text("Click to accept")));
//        acceptButton.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/home acceptInvite " + players.getSender() + " " + home));
//
//
//
//        TextComponent declineButton = new TextComponent(Utils.translateHex("&#565BA7[Decline]"));
//        declineButton.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new Text("Click to decline")));
//        declineButton.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/decline"));
//        declineButton.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/home declineInvite"));
//        message.addExtra(acceptButton);
//        message.addExtra(" ");
//        message.addExtra(declineButton);
//
//        Player player = HomeTotems.getInstance().getServer().getOfflinePlayer(players.getInvitedplayer()).getPlayer();
//        player.getPlayer().spigot().sendMessage(message);
//    }
}
