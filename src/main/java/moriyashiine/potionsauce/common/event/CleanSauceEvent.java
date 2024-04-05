/*
 * All Rights Reserved (c) MoriyaShiine
 */

package moriyashiine.potionsauce.common.event;

import moriyashiine.potionsauce.common.PotionSauce;
import net.fabricmc.fabric.api.event.player.UseBlockCallback;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.LeveledCauldronBlock;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.PotionUtil;
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
		if (stack.isFood() && !PotionSauce.getEffects(stack).isEmpty()) {
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
					ItemStack food = stack;
					boolean give = false;
					if (!player.isSneaking()) {
						food = stack.split(1);
						give = true;
					}
					food.getNbt().remove(PotionUtil.CUSTOM_POTION_EFFECTS_KEY);
					food.getSubNbt(PotionSauce.MOD_ID).remove("Sauced");
					if (give && !player.giveItemStack(food)) {
						player.dropStack(food);
					}
					return ActionResult.success(world.isClient);
				}
			}
		}
		return ActionResult.PASS;
	}
}
