/*
 * Copyright (c) MoriyaShiine. All Rights Reserved.
 */
package moriyashiine.potionsauce.common;

import moriyashiine.potionsauce.common.event.CleanSauceEvent;
import moriyashiine.potionsauce.common.event.OverrideDeathMessageEvent;
import moriyashiine.potionsauce.common.init.ModComponentTypes;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerTickEvents;
import net.fabricmc.fabric.api.event.player.UseBlockCallback;
import net.minecraft.util.Identifier;

public class PotionSauce implements ModInitializer {
	public static final String MOD_ID = "potionsauce";

	@Override
	public void onInitialize() {
		ModComponentTypes.init();
		UseBlockCallback.EVENT.register(new CleanSauceEvent());
		ServerTickEvents.END_SERVER_TICK.register(new OverrideDeathMessageEvent());
	}

	public static Identifier id(String value) {
		return Identifier.of(MOD_ID, value);
	}
}