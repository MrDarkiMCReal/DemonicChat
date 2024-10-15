package org.mrdarkimc.demonicchat.chat;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.scheduler.BukkitRunnable;
import org.mrdarkimc.demonicchat.DemonicChat;

public class AsyncChatSender implements Listener {
    public void awaitingForMessage(){
       new BukkitRunnable() {
            @Override
            public void run() {

            }
        }.runTaskAsynchronously(DemonicChat.getInstance());
    }
}

