/*
 * Copyright (c) MoriyaShiine. All Rights Reserved.
 */
package moriyashiine.potionsauce.mixin;

import moriyashiine.potionsauce.common.PotionSauce;
import net.minecraft.entity.damage.DamageSource;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyVariable;

@Mixin(DamageSource.class)
public class DamageSourceMixin {
	@ModifyVariable(method = "getDeathMessage", at = @At("STORE"), ordinal = 0)
	private String potionsauce$overrideDeathMessage(String value) {
		if (PotionSauce.overrideDeathMessage) {
			return "death.attack.potionsauce.overeating";
		}
		return value;
	}
}
