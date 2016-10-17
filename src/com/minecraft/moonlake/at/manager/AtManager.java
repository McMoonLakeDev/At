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
 
 
package com.minecraft.moonlake.at.manager;

import com.minecraft.moonlake.at.AtPlugin;
import com.minecraft.moonlake.at.api.event.PlayerAtAllEvent;
import com.minecraft.moonlake.at.api.event.PlayerAtPlayerEvent;
import com.minecraft.moonlake.economy.EconomyType;
import com.minecraft.moonlake.manager.PlayerManager;
import com.minecraft.moonlake.nms.packet.PacketPlayOutTitle;
import com.minecraft.moonlake.util.StringUtil;
import org.bukkit.Bukkit;
import org.bukkit.Sound;
import org.bukkit.entity.Player;

/**
 * Created by IntelliJ IDEA.
 * User: MoonLake
 * Date: 2016/10/16
 * Time: 11:53
 *
 * @author Month_Light
 * @version 1.0
 */
public class AtManager {

    private static AtPlugin MAIN;

    // 待实现功能
    // at 缓存区（为了防止同时接收到 at 消息造成丢失）
    // at 冷却时间
    // at 屏蔽

    private AtManager() {
    }

    public static void setMain(AtPlugin main) {
        if(MAIN == null) {
            MAIN = main;
        }
    }

    public static AtPlugin getMain() {
        return MAIN;
    }

    public static boolean handleAtTarget(Player source, String message) {
        if(!source.hasPermission("moonlake.at.use")) {
            source.sendMessage(getMain().getMessage("NoPermission"));
            return false;
        }
        Player target = AtManager.getAtTarget(message);

        if(target == null) {
            source.sendMessage(getMain().getMessage("ErrorNoOnline"));
            return false;
        }
        double needEconomy = 0d;    // 用于执行成功后减少玩家的经济数量

        if(getMain().isEconomy() && !source.hasPermission("moonlake.at.economy")) {
            // 启动经济功能检测并且玩家没有忽略经济权限则检测是否足够
            double has = getMain().getEconomyType() == EconomyType.MONEY ?
                    getMain().getEconomy().getMoney(source.getName()) :
                    getMain().getEconomy().getPoint(source.getName());
            // 获取配置文件需求的数量
            needEconomy = getMain().getConfig().getDouble("Economy.AtAmount", 0d);

            if(has < needEconomy) {
                // 玩家的经济不足够
                source.sendMessage(getMain().getMessage("NoHaveEconomy", needEconomy, getMain().getEconomyType().getDisplayName()));
                return false;
            }
        }
        // 触发 at 玩家事件
        PlayerAtPlayerEvent pape = new PlayerAtPlayerEvent(source, target);
        Bukkit.getServer().getPluginManager().callEvent(pape);

        if(!pape.isCancelled()) {
            // 如果没有阻止则发送
            PacketPlayOutTitle packetPlayOutTitle = new PacketPlayOutTitle("");
            packetPlayOutTitle.getTitle().set(StringUtil.toColor(StringUtil.format(getMain().getConfig().getString("At.Title"), source.getName())));
            packetPlayOutTitle.getSubTitle().set(message.substring(message.indexOf(" ") + 1));
            packetPlayOutTitle.getFadeIn().set(10);
            packetPlayOutTitle.getStay().set(50);
            packetPlayOutTitle.getFadeOut().set(30);
            packetPlayOutTitle.send(target);
            // 播放一个音效
            target.playSound(target.getLocation(), Sound.ENTITY_PLAYER_LEVELUP, 10f, 1f);
            // 减少玩家的经济数量
            if(getMain().isEconomy()) {

                if(getMain().getEconomyType() == EconomyType.MONEY)
                    getMain().getEconomy().takeMoney(source.getName(), needEconomy);
                else
                    getMain().getEconomy().takePoint(source.getName(), (int) needEconomy);
            }
        }
        return pape.isCancelled();
    }

    public static boolean handleAtAll(Player source, String message) {
        if(!source.hasPermission("moonlake.at.all")) {
            source.sendMessage(getMain().getMessage("NoPermission"));
            return false;
        }
        double needEconomy = 0d;    // 用于执行成功后减少玩家的经济数量

        if(getMain().isEconomy() && !source.hasPermission("moonlake.at.economy")) {
            // 启动经济功能检测并且玩家没有忽略经济权限则检测是否足够
            double has = getMain().getEconomyType() == EconomyType.MONEY ?
                    getMain().getEconomy().getMoney(source.getName()) :
                    getMain().getEconomy().getPoint(source.getName());
            // 获取配置文件需求的数量
            needEconomy = getMain().getConfig().getDouble("Economy.AtAllAmount", 0d);

            if(has < needEconomy) {
                // 玩家的经济不足够
                source.sendMessage(getMain().getMessage("NoHaveEconomy", needEconomy, getMain().getEconomyType().getDisplayName()));
                return false;
            }
        }
        // 触发 at 全体玩家事件
        PlayerAtAllEvent paae = new PlayerAtAllEvent(source);
        Bukkit.getServer().getPluginManager().callEvent(paae);

        if(!paae.isCancelled()) {
            // 如果没有阻止则发送
            PacketPlayOutTitle packetPlayOutTitle = new PacketPlayOutTitle("");
            packetPlayOutTitle.getTitle().set(StringUtil.toColor(StringUtil.format(getMain().getConfig().getString("At.Title"), source.getName())));
            packetPlayOutTitle.getSubTitle().set(message.substring(message.indexOf(" ") + 1));
            packetPlayOutTitle.getFadeIn().set(10);
            packetPlayOutTitle.getStay().set(60);
            packetPlayOutTitle.getFadeOut().set(40);
            packetPlayOutTitle.sendAll();
            // 遍历所有玩家并播放一个音效
            for(final Player target : PlayerManager.getOnlines()) {
                target.playSound(target.getLocation(), Sound.ENTITY_PLAYER_LEVELUP, 10f, 1f);
            }
            // 减少玩家的经济数量
            if(getMain().isEconomy()) {

                if(getMain().getEconomyType() == EconomyType.MONEY)
                    getMain().getEconomy().takeMoney(source.getName(), needEconomy);
                else
                    getMain().getEconomy().takePoint(source.getName(), (int) needEconomy);
            }
        }
        return paae.isCancelled();
    }

    private static Player getAtTarget(String message) {
        if(message == null || !message.contains("@") || !message.contains(" "))
            return null;
        return Bukkit.getServer().getPlayer(message.substring(1, message.indexOf(" ")));
    }
}
