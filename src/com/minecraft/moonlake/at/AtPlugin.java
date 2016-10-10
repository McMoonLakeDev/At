package com.minecraft.moonlake.at;

import com.minecraft.moonlake.MoonLakePlugin;
import com.minecraft.moonlake.logger.MLogger;
import com.minecraft.moonlake.logger.MLoggerWrapped;
import com.minecraft.moonlake.nms.packet.PacketPlayOutTitle;
import com.minecraft.moonlake.util.StringUtil;
import org.bukkit.Bukkit;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;

/**
 * Created by MoonLake on 2016/10/10.
 */
public class AtPlugin extends JavaPlugin implements Listener {

    private final MLogger mLogger;

    public AtPlugin() {

        this.mLogger = new MLoggerWrapped("MoonLakeAt");
    }

    @Override
    public void onEnable() {

        this.getServer().getPluginManager().registerEvents(this, this);
        this.getMLogger().log("月色之湖 At 插件 v" + getDescription().getVersion() + " 成功加载.");
    }

    @Override
    public void onDisable() {

    }

    private boolean setupMoonLake() {

        Plugin plugin = getServer().getPluginManager().getPlugin("MoonLake");
        return plugin != null && plugin instanceof MoonLakePlugin;
    }

    public MLogger getMLogger() {

        return mLogger;
    }

    @EventHandler
    public void onChat(AsyncPlayerChatEvent event) {

        String message = event.getMessage();

        if(message.charAt(0) == '@' && message.contains(" ")) {

            String player = event.getPlayer().getName();

            if(message.matches("@([a-zA-Z0-9_]+)\\s(.*?)")) {
                // at target
                Player target = getAtTarget(message);

                if(target == null) {
                    event.setCancelled(true);
                    event.getPlayer().sendMessage("错误, 你 At 的目标玩家未在线.");
                    return;
                }
                new PacketPlayOutTitle(StringUtil.toColor("&b玩家 &a" + player + "&b @ 了你!"), 10, 50, 30).send(target);
                target.playSound(target.getLocation(), Sound.ENTITY_PLAYER_LEVELUP, 10f, 1f);
                target.sendMessage(String.format(StringUtil.toColor("&f[&6At&f] &a%1$s&7: &f%2$s"), player, message.substring(message.indexOf(" ") + 1)));
            }
            else if(message.matches("@全体玩家\\s(.*?)")) {
                // at all target
            }
            else {

                event.getPlayer().sendMessage("错误的 At 格式, 应使用: @(玩家名|全体玩家)空格消息. 例如: @Notch 你好!");
            }
            event.setCancelled(true);
        }
    }

    private Player getAtTarget(String message) {

        if(message == null || !message.contains("@") || !message.contains(" "))
            return null;
        return Bukkit.getServer().getPlayer(message.substring(1, message.indexOf(" ")));
    }
}
