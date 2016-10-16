package com.minecraft.moonlake.at.api.event;

import org.bukkit.entity.Player;
import org.bukkit.event.HandlerList;

/**
 * Created by IntelliJ IDEA.
 * User: MoonLake
 * Date: 2016/10/16
 * Time: 12:12
 *
 * @author Month_Light
 * @version 1.0
 */
public class PlayerAtAllEvent extends PlayerAtEvent {

    private static final HandlerList handlers = new HandlerList();

    public PlayerAtAllEvent(Player who) {
        super(who);
    }

    @Override
    public HandlerList getHandlers() {
        return handlers;
    }

    public static HandlerList getHandlerList() {
        return handlers;
    }
}
