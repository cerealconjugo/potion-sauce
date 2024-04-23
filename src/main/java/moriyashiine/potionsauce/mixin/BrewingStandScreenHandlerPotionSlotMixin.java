/*
 * All Rights Reserved (c) MoriyaShiine
 */

package moriyashiine.potionsauce.mixin;

import com.llamalad7.mixinextras.injector.ModifyReturnValue;
import moriyashiine.potionsauce.common.init.ModDataComponentTypes;
import net.minecraft.component.DataComponentTypes;
import net.minecraft.item.ItemStack;
import net.minecraft.screen.BrewingStandScreenHandler;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(BrewingStandScreenHandler.PotionSlot.class)
public class BrewingStandScreenHandlerPotionSlotMixin {
	@ModifyReturnValue(method = "matches", at = @At("RETURN"))
	private static boolean potionsauce$allowFood(boolean original, ItemStack stack) {
		if (!original && stack.contains(DataComponentTypes.FOOD) && !stack.contains(ModDataComponentTypes.SAUCED)) {
			return true;
		}
		return original;
	}
}
