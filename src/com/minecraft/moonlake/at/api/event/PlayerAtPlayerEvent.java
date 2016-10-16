package com.minecraft.moonlake.at.api.event;

import org.bukkit.entity.Player;
import org.bukkit.event.HandlerList;

/**
 * Created by IntelliJ IDEA.
 * User: MoonLake
 * Date: 2016/10/16
 * Time: 12:08
 *
 * @author Month_Light
 * @version 1.0
 */
public class PlayerAtPlayerEvent extends PlayerAtEvent {

    private static final HandlerList handlers = new HandlerList();
    private final Player target;

    public PlayerAtPlayerEvent(Player who, Player target) {
        super(who);

        this.target = target;
    }

    public Player getTarget() {
        return target;
    }

    @Override
    public HandlerList getHandlers() {
        return handlers;
    }

    public static HandlerList getHandlerList() {
        return handlers;
    }
}
