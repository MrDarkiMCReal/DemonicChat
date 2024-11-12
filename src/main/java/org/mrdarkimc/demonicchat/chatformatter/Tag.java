package org.mrdarkimc.demonicchat.chatformatter;

import me.clip.placeholderapi.PlaceholderAPI;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.chat.*;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.chat.hover.content.Content;
import net.md_5.bungee.api.chat.hover.content.Text;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Tag {
    private Player player;
    private String text;
    private ClickEvent clickEvent;
    private ClickEvent.Action action;
    private String actionValue;
    private TextComponent component;
    private HoverEvent hoverEvent;
    private List<String> hoverMessage;

    public TextComponent getComponent() {
        component = translateComponentHex(text);
        //component.addExtra(text.isEmpty() ? "" : text);
        if (clickEvent != null) {
            component.setClickEvent(clickEvent);
        }
        if (hoverEvent != null) {
            component.setHoverEvent(hoverEvent);
        }
        return component;
    }

    public Tag(Player player, String text) {
        this.player = player;
        this.text = PlaceholderAPI.setPlaceholders(player,text);
    }

    public String getText() {
        return text;
    }

    public Tag(Player player, String text, ClickEvent.Action action, String actionValue) {
        this.player = player;
        this.text = text;
        this.clickEvent = new ClickEvent(action, actionValue);
    }

    public Tag(Player player, String text, ClickEvent clickEvent, HoverEvent hoverEvent) {
        this.player = player;
        this.text = text;
        this.clickEvent = clickEvent;
        this.hoverEvent = hoverEvent;
    }

    public Tag(Player player, String text, ArrayList<String> hoverMessage) {
        this.player = player;
        this.text = text;
        hoverMessage.replaceAll(string -> PlaceholderAPI.setPlaceholders(player, string));
        hoverMessage.replaceAll(string -> string.replaceAll("\\{clan}", "ggg"));
        ArrayList<Content> texts = stringListToContentList(hoverMessage);
        this.hoverEvent = new HoverEvent(HoverEvent.Action.SHOW_TEXT, texts);
    }

//    public static ArrayList<Content> stringListToContentList(List<String> hoverMessage) {
//        //input:
//        //&#FF2733Клан игрока:#2F5737 SATANIC
//        //&#2F5733KD клана:#FF5783 1.6
//        ArrayList<Content> contentList = new ArrayList<>();
////        Iterator<String> itr = hoverMessage.iterator();
////        while (itr.hasNext()){
////            itr.next()
////        }
//
//        for (String s : hoverMessage) {
//            if (!s.equals(hoverMessage.getLast())) {
//                s = s + "\n";
//            }//todo make it better
//            TextComponent component = new TextComponent();
//            Pattern pattern = Pattern.compile("&#[0-9A-Fa-f]{6}");
//            //&#FF2733Клан игрока:#2F5737 SATANIC splitted will be &#FF2733Клан игрока:#2F5737 SATANIC
//            for (String substring : s.split("(?=_)")) {
//                Matcher matcher = pattern.matcher(substring);
//                String color = "#FF5733";
//                if (matcher.find()) {
//                    color = matcher.group().replaceAll("&", "");
//                }
//                TextComponent tempComponent = new TextComponent(substring.replaceAll(pattern.toString(), ""));
//                tempComponent.setColor(ChatColor.of(color));
//                component.addExtra(tempComponent);
//            }
//
//            contentList.add(new Text(component));
//        }
//        return contentList;
//        //output (colored)
//        //Клан игрока: SATANIC
//        //KD клана: 1.6
//    }
public static ArrayList<Content> stringListToContentList(List<String> hoverMessage) {
    ArrayList<Content> contentList = new ArrayList<>();


    for (String s : hoverMessage) {
        if (!hoverMessage.getLast().equals(s)){
            s = s + "\n";
        }
        contentList.add(new Text(translateComponentHex(s)));

    }
    return contentList;
    //output (colored)
    //Клан игрока: SATANIC
    //KD клана: 1.6
}

    public static TextComponent translateComponentHex(String text) {
        TextComponent component = new TextComponent();
        Pattern pattern = Pattern.compile("&#([A-Fa-f0-9]{6})([^&]*)");
        Matcher matcher = pattern.matcher(text);
        int lastMatchEnd = 0;

        while (matcher.find()) {
            if (matcher.start() > lastMatchEnd) {
                String unchangedText = text.substring(lastMatchEnd, matcher.start());
                component.addExtra(new TextComponent(unchangedText));
            }
            String hex = "#" + matcher.group(1);
            String textWithoutHex = matcher.group(2);
            TextComponent comp = new TextComponent(textWithoutHex);
            comp.setColor(ChatColor.of(hex));
            component.setColor(ChatColor.of(hex));//add last coloring to main text component. then calls a stackText components. thats how
            component.addExtra(comp);

            lastMatchEnd = matcher.end();
        }
        if (lastMatchEnd < text.length()) {
            String remainingText = text.substring(lastMatchEnd);
            component.addExtra(new TextComponent(remainingText));
        }

        return component;
    }


}