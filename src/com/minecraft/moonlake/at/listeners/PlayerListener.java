package com.minecraft.moonlake.at.listeners;

import com.minecraft.moonlake.at.AtPlugin;
import com.minecraft.moonlake.at.manager.AtManager;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;

/**
 * Created by IntelliJ IDEA.
 * User: MoonLake
 * Date: 2016/10/16
 * Time: 11:13
 *
 * @author Month_Light
 * @version 1.0
 */
public class PlayerListener implements Listener {

    private final AtPlugin main;

    public PlayerListener(AtPlugin main) {
        this.main = main;
    }

    public AtPlugin getMain() {
        return main;
    }

    @EventHandler
    public void onChat(AsyncPlayerChatEvent event) {
        String message = event.getMessage();

        if(message.charAt(0) == '@' && message.contains(" ")) {
            if(message.matches("@([a-zA-Z0-9_]+)\\s(.*?)")) {
                // at 单个目标玩家
                if(!AtManager.handleAtTarget(event.getPlayer(), message)) {
                    // at 失败则阻止消息并返回
                    event.setCancelled(true);
                    return;
                }
            }
            else if(message.matches("@全体玩家\\s(.*?)")) {
                // at 全体玩家
                if(!AtManager.handleAtAll(event.getPlayer(), message)) {
                    // at 失败则阻止消息并返回
                    event.setCancelled(true);
                    return;
                }
            }
            else {
                event.getPlayer().sendMessage(getMain().getMessage("ErrorAtFormat"));
                event.setCancelled(true);
            }
        }
    }
}
