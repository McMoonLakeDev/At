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
