/*
 * Copyright (c) MoriyaShiine. All Rights Reserved.
 */
package moriyashiine.potionsauce.common.event;

import net.fabricmc.fabric.api.event.lifecycle.v1.ServerTickEvents;
import net.minecraft.server.MinecraftServer;

public class OverrideDeathMessageEvent implements ServerTickEvents.EndTick {
	public static boolean overrideDeathMessage = false;

	@Override
	public void onEndTick(MinecraftServer minecraftServer) {
		overrideDeathMessage = false;
	}
}
