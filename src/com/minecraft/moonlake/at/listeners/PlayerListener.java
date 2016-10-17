/*
 * Copyright (C) 2016 The MoonLake Authors
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
 
 
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
