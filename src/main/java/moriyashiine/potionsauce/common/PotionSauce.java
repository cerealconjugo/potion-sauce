/*
 * All Rights Reserved (c) MoriyaShiine
 */

package moriyashiine.potionsauce.common;

import moriyashiine.potionsauce.common.event.CleanSauceEvent;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.event.player.UseBlockCallback;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.potion.PotionUtil;

import java.util.List;

public class PotionSauce implements ModInitializer {
	public static final String MOD_ID = "potionsauce";

	@Override
	public void onInitialize() {
		UseBlockCallback.EVENT.register(new CleanSauceEvent());
	}

	public static List<StatusEffectInstance> getEffects(ItemStack stack) {
		if (stack.isFood()) {
			NbtCompound nbt = stack.getSubNbt(PotionSauce.MOD_ID);
			if (nbt == null || !nbt.getBoolean("Sauced")) {
				return List.of();
			}
		}
		List<StatusEffectInstance> effects = PotionUtil.getPotionEffects(stack);
		if (effects.isEmpty()) {
			effects.addAll(PotionUtil.getCustomPotionEffects(stack));
		}
		return effects;
	}
}