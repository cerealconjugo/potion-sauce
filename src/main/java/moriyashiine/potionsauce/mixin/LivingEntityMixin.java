/*
 * Copyright (c) MoriyaShiine. All Rights Reserved.
 */
package moriyashiine.potionsauce.mixin;

import moriyashiine.potionsauce.common.PotionSauce;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.List;

@Mixin(LivingEntity.class)
public abstract class LivingEntityMixin {
	@Shadow
	public abstract boolean addStatusEffect(StatusEffectInstance effect);

	@Inject(method = "eatFood", at = @At(value = "INVOKE", target = "Lnet/minecraft/entity/LivingEntity;applyFoodEffects(Lnet/minecraft/component/type/FoodComponent;)V"))
	private void potionsauce$applyEffects(World world, ItemStack stack, CallbackInfoReturnable<ItemStack> cir) {
		if (!world.isClient) {
			List<StatusEffectInstance> sauceEffects = PotionSauce.getSauceEffects(stack);
			if (!sauceEffects.isEmpty()) {
				PotionSauce.overrideDeathMessage = true;
				sauceEffects.forEach(effect -> addStatusEffect(new StatusEffectInstance(effect)));
			}
		}
	}

	@Inject(method = "damage", at = @At("RETURN"))
	private void potionsauce$removeDeathMessageOverride(DamageSource source, float amount, CallbackInfoReturnable<Boolean> cir) {
		PotionSauce.overrideDeathMessage = false;
	}
}
