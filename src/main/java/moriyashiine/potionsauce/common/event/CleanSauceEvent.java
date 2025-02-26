/*
 * Copyright (c) MoriyaShiine. All Rights Reserved.
 */
package moriyashiine.potionsauce.common.event;

import moriyashiine.potionsauce.common.init.ModComponentTypes;
import net.fabricmc.fabric.api.event.player.UseBlockCallback;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.LeveledCauldronBlock;
import net.minecraft.component.DataComponentTypes;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemUsage;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.world.World;

public class CleanSauceEvent implements UseBlockCallback {
	@Override
	public ActionResult interact(PlayerEntity player, World world, Hand hand, BlockHitResult hitResult) {
		ItemStack stack = player.getStackInHand(hand);
		if (stack.contains(ModComponentTypes.SAUCED)) {
			BlockState state = world.getBlockState(hitResult.getBlockPos());
			if (state.isOf(Blocks.WATER_CAULDRON)) {
				int level = state.get(LeveledCauldronBlock.LEVEL);
				if (level > 0) {
					world.playSound(null, hitResult.getBlockPos(), SoundEvents.ENTITY_GENERIC_SPLASH, SoundCategory.BLOCKS, 1, 1);
					if (level == 1) {
						world.setBlockState(hitResult.getBlockPos(), Blocks.CAULDRON.getDefaultState());
					} else {
						world.setBlockState(hitResult.getBlockPos(), state.with(LeveledCauldronBlock.LEVEL, level - 1));
					}
					ItemStack food = player.isSneaking() ? stack : stack.copyWithCount(1);
					food.remove(DataComponentTypes.POTION_CONTENTS);
					food.remove(ModComponentTypes.SAUCED);
					if (food != stack) {
						ItemUsage.exchangeStack(stack, player, food);
					}
					return ActionResult.SUCCESS;
				}
			}
		}
		return ActionResult.PASS;
	}
}
