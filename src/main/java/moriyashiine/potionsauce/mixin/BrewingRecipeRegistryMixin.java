/*
 * Copyright (c) MoriyaShiine. All Rights Reserved.
 */
package moriyashiine.potionsauce.mixin;

import com.llamalad7.mixinextras.injector.ModifyReturnValue;
import moriyashiine.potionsauce.common.init.ModDataComponentTypes;
import net.minecraft.component.DataComponentTypes;
import net.minecraft.component.type.PotionContentsComponent;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.recipe.BrewingRecipeRegistry;
import net.minecraft.util.Unit;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(BrewingRecipeRegistry.class)
public class BrewingRecipeRegistryMixin {
	@ModifyReturnValue(method = "isValidIngredient", at = @At("RETURN"))
	private boolean potionsauce$allowPotion(boolean original, ItemStack stack) {
		if (!original && stack.isOf(Items.POTION) && stack.getOrDefault(DataComponentTypes.POTION_CONTENTS, PotionContentsComponent.DEFAULT).hasEffects()) {
			return true;
		}
		return original;
	}

	@ModifyReturnValue(method = "hasRecipe", at = @At("RETURN"))
	private boolean potionsauce$allowFood(boolean original, ItemStack input, ItemStack ingredient) {
		if (!original && input.contains(DataComponentTypes.FOOD) && ingredient.isOf(Items.POTION) && !input.contains(ModDataComponentTypes.SAUCED)) {
			return true;
		}
		return original;
	}

	@Inject(method = "craft", at = @At("HEAD"))
	private void potionsauce$applyPotion(ItemStack ingredient, ItemStack input, CallbackInfoReturnable<ItemStack> cir) {
		if (input.contains(DataComponentTypes.FOOD) && ingredient.isOf(Items.POTION) && !input.contains(ModDataComponentTypes.SAUCED)) {
			PotionContentsComponent potionPotionContents = ingredient.getOrDefault(DataComponentTypes.POTION_CONTENTS, PotionContentsComponent.DEFAULT);
			if (potionPotionContents.hasEffects()) {
				PotionContentsComponent foodPotionContents = input.getOrDefault(DataComponentTypes.POTION_CONTENTS, PotionContentsComponent.DEFAULT);
				for (StatusEffectInstance instance : potionPotionContents.getEffects()) {
					foodPotionContents = foodPotionContents.with(new StatusEffectInstance(instance.getEffectType(),
							instance.getDuration() == StatusEffectInstance.INFINITE ? instance.getDuration() : Math.max(1, instance.getDuration() / (instance.getEffectType().value().isInstant() ? 1 : 3)),
							Math.max(0, instance.getAmplifier() - (instance.getEffectType().value().isInstant() ? 1 : 0)),
							instance.isAmbient(),
							instance.shouldShowParticles(),
							instance.shouldShowIcon()));
				}
				input.set(DataComponentTypes.POTION_CONTENTS, foodPotionContents);
				input.set(ModDataComponentTypes.SAUCED, Unit.INSTANCE);
			}
		}
	}
}
