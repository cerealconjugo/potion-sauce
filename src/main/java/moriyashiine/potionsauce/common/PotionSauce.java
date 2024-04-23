/*
 * All Rights Reserved (c) MoriyaShiine
 */

package moriyashiine.potionsauce.common;

import com.google.common.collect.Lists;
import moriyashiine.potionsauce.common.event.CleanSauceEvent;
import moriyashiine.potionsauce.common.init.ModDataComponentTypes;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.event.player.UseBlockCallback;
import net.minecraft.component.DataComponentTypes;
import net.minecraft.component.type.PotionContentsComponent;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Identifier;

import java.util.List;

public class PotionSauce implements ModInitializer {
	public static final String MOD_ID = "potionsauce";

	public static boolean overrideDeathMessage = false;

	@Override
	public void onInitialize() {
		ModDataComponentTypes.init();
		UseBlockCallback.EVENT.register(new CleanSauceEvent());
	}

	public static Identifier id(String value) {
		return new Identifier(MOD_ID, value);
	}

	public static List<StatusEffectInstance> getSauceEffects(ItemStack stack) {
		if (stack.contains(DataComponentTypes.FOOD) && stack.contains(ModDataComponentTypes.SAUCED)) {
			PotionContentsComponent potionContents = stack.get(DataComponentTypes.POTION_CONTENTS);
			if (potionContents != null) {
				return Lists.newArrayList(potionContents.getEffects());
			}
		}
		return List.of();
	}
}