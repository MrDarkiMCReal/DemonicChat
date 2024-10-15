package org.mrdarkimc.demonicchat.chatformatter;

import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.chat.*;
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

    public TextComponent getComponent() { //todo text заменить на парсинг с PAPI, для того, что бы можно было применять плейсхолдеры
        component = new TextComponent();
        component.addExtra(text);
        if (clickEvent!=null){
            component.setClickEvent(clickEvent);
        }
        if (hoverEvent!=null){
            component.setHoverEvent(hoverEvent);
        }
        return component;
    }

    public Tag(Player player, String text) {
        this.player = player;
        this.text = text;
    }

    public Tag(Player player, String text, ClickEvent.Action action, String actionValue) {
        this.player = player;
        this.text = text;
        this.clickEvent = new ClickEvent(action, actionValue);
    }

    public Tag(Player player, String text, ArrayList<String> hoverMessage) {
        this.player = player;
        this.text = text;
        ArrayList<Content> texts = stringListToContentList(hoverMessage);
        this.hoverEvent = new HoverEvent(HoverEvent.Action.SHOW_TEXT,texts);
    }

    public static ArrayList<Content> stringListToContentList(ArrayList<String> hoverMessage) {
        //input:
        //&#FF2733Клан игрока:#2F5737 SATANIC
        //&#2F5733KD клана:#FF5783 1.6
        ArrayList<Content> contentList = new ArrayList<>();

        for (String s : hoverMessage) {
            s = s + "\n";
            TextComponent component = new TextComponent();
            Pattern pattern = Pattern.compile("&#[0-9A-Fa-f]{6}");
            //&#FF2733Клан игрока:#2F5737 SATANIC splitted will be &#FF2733Клан игрока:#2F5737 SATANIC
            for (String substring : s.split("(?=_)")) {
                Matcher matcher = pattern.matcher(substring);
                String color = "#FF5733";
                if (matcher.find()) {
                    color = matcher.group().replaceAll("&", "");
                }
                TextComponent tempComponent = new TextComponent(substring.replaceAll(pattern.toString(), ""));
                tempComponent.setColor(ChatColor.of(color));
                component.addExtra(tempComponent);
            }

            contentList.add(new Text(component));
        }
        return contentList;
        //output (colored)
        //Клан игрока: SATANIC
        //KD клана: 1.6
    }
}
