/*
 * Copyright (c) MoriyaShiine. All Rights Reserved.
 */
package moriyashiine.potionsauce.mixin;

import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import com.llamalad7.mixinextras.injector.ModifyReturnValue;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import moriyashiine.potionsauce.common.init.ModComponentTypes;
import net.minecraft.block.entity.BrewingStandBlockEntity;
import net.minecraft.component.DataComponentTypes;
import net.minecraft.component.type.PotionContentsComponent;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.util.collection.DefaultedList;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyVariable;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(value = BrewingStandBlockEntity.class, priority = 1001)
public class BrewingStandBlockEntityMixin {
	@Unique
	private static boolean hasPotion = false;

	@ModifyReturnValue(method = "isValid", at = @At(value = "RETURN", ordinal = 0))
	private boolean potionsauce$allowPotion(boolean original, int slot, ItemStack stack) {
		if (!original && stack.isOf(Items.POTION) && stack.getOrDefault(DataComponentTypes.POTION_CONTENTS, PotionContentsComponent.DEFAULT).hasEffects()) {
			return true;
		}
		return original;
	}

	@ModifyReturnValue(method = "isValid", at = @At(value = "RETURN", ordinal = 2))
	private boolean potionsauce$allowFood(boolean original, int slot, ItemStack stack) {
		if (!original && stack.contains(DataComponentTypes.FOOD) && !stack.contains(ModComponentTypes.SAUCED)) {
			return true;
		}
		return original;
	}

	@Inject(method = "craft", at = @At("HEAD"))
	private static void potionsauce$checkPotion(World world, BlockPos pos, DefaultedList<ItemStack> slots, CallbackInfo ci) {
		hasPotion = slots.get(3).isOf(Items.POTION);
	}

	@ModifyExpressionValue(method = "craft", at = @At(value = "INVOKE", target = "Lnet/minecraft/item/Item;hasRecipeRemainder()Z"))
	private static boolean potionsauce$allowBottle(boolean original) {
		return original || hasPotion;
	}

	@WrapOperation(method = "craft", at = @At(value = "INVOKE", target = "Lnet/minecraft/item/Item;getRecipeRemainder()Lnet/minecraft/item/Item;"))
	private static Item potionsauce$dummyBottle(Item instance, Operation<Item> original) {
		if (hasPotion) {
			return Items.GLASS_BOTTLE;
		}
		return original.call(instance);
	}

	@ModifyVariable(method = "craft", at = @At("STORE"), ordinal = 1)
	private static ItemStack potionsauce$giveBottle(ItemStack value) {
		if (hasPotion) {
			return Items.GLASS_BOTTLE.getDefaultStack();
		}
		return value;
	}
}
