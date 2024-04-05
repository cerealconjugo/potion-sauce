/*
 * All Rights Reserved (c) MoriyaShiine
 */

package moriyashiine.potionsauce.mixin;

import com.llamalad7.mixinextras.injector.ModifyReturnValue;
import moriyashiine.potionsauce.common.PotionSauce;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.potion.PotionUtil;
import net.minecraft.recipe.BrewingRecipeRegistry;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.List;

@Mixin(BrewingRecipeRegistry.class)
public class BrewingRecipeRegistryMixin {
	@ModifyReturnValue(method = "isValidIngredient", at = @At("RETURN"))
	private static boolean potionsauce$allowPotion(boolean original, ItemStack stack) {
		if (!original && stack.isOf(Items.POTION) && !PotionSauce.getEffects(stack).isEmpty()) {
			return true;
		}
		return original;
	}

	@ModifyReturnValue(method = "hasRecipe", at = @At("RETURN"))
	private static boolean potionsauce$allowFood(boolean original, ItemStack input, ItemStack ingredient) {
		if (!original && input.isFood() && ingredient.isOf(Items.POTION) && PotionSauce.getEffects(input).isEmpty()) {
			return true;
		}
		return original;
	}

	@Inject(method = "craft", at = @At("HEAD"), cancellable = true)
	private static void potionsauce$applyPotion(ItemStack ingredient, ItemStack input, CallbackInfoReturnable<ItemStack> cir) {
		if (input.isFood() && ingredient.isOf(Items.POTION)) {
			List<StatusEffectInstance> effects = PotionSauce.getEffects(ingredient);
			if (!effects.isEmpty()) {
				for (int i = 0; i < effects.size(); i++) {
					StatusEffectInstance instance = effects.get(i);
					effects.set(i, new StatusEffectInstance(instance.getEffectType(),
							instance.getDuration() == StatusEffectInstance.INFINITE ? instance.getDuration() : Math.max(1, instance.getDuration() / (instance.getEffectType().isInstant() ? 1 : 3)),
							Math.max(0, instance.getAmplifier() - (instance.getEffectType().isInstant() ? 1 : 0)),
							instance.isAmbient(),
							instance.shouldShowParticles(),
							instance.shouldShowIcon()));
				}
				ItemStack saucedFood = PotionUtil.setCustomPotionEffects(input, effects);
				saucedFood.getOrCreateSubNbt(PotionSauce.MOD_ID).putBoolean("Sauced", true);
				cir.setReturnValue(saucedFood);
			}
		}
	}
}
