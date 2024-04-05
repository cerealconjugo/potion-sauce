/*
 * All Rights Reserved (c) MoriyaShiine
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

	@Inject(method = "eatFood", at = @At(value = "INVOKE", target = "Lnet/minecraft/entity/LivingEntity;applyFoodEffects(Lnet/minecraft/item/ItemStack;Lnet/minecraft/world/World;Lnet/minecraft/entity/LivingEntity;)V"))
	private void potionsauce$applyEffects(World world, ItemStack stack, CallbackInfoReturnable<ItemStack> cir) {
		List<StatusEffectInstance> effects = PotionSauce.getEffects(stack);
		if (!effects.isEmpty()) {
			PotionSauce.overrideDeathMessage = true;
			effects.forEach(this::addStatusEffect);
		}
	}

	@Inject(method = "damage", at = @At("RETURN"))
	private void potionsauce$removeDeathMessageOverride(DamageSource source, float amount, CallbackInfoReturnable<Boolean> cir) {
		PotionSauce.overrideDeathMessage = false;
	}
}
