/*
 * Copyright (c) MoriyaShiine. All Rights Reserved.
 */
package moriyashiine.potionsauce.mixin;

import moriyashiine.potionsauce.common.event.OverrideDeathMessageEvent;
import net.minecraft.component.type.ConsumableComponent;
import net.minecraft.component.type.PotionContentsComponent;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(PotionContentsComponent.class)
public class PotionContentsComponentMixin {
	@Inject(method = "onConsume", at = @At("HEAD"))
	private void potionsauce$overrideDeathMessageHead(World world, LivingEntity user, ItemStack stack, ConsumableComponent consumable, CallbackInfo ci) {
		OverrideDeathMessageEvent.overrideDeathMessage = true;
	}
}
