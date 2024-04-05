/*
 * All Rights Reserved (c) MoriyaShiine
 */

package moriyashiine.potionsauce.client.event;

import moriyashiine.potionsauce.common.PotionSauce;
import net.fabricmc.fabric.api.client.item.v1.ItemTooltipCallback;
import net.minecraft.client.item.TooltipContext;
import net.minecraft.entity.effect.StatusEffectCategory;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.potion.PotionUtil;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;

import java.util.List;

public class RenderSauceEvent implements ItemTooltipCallback {
	@Override
	public void getTooltip(ItemStack stack, TooltipContext context, List<Text> lines) {
		if (stack.isFood()) {
			NbtCompound nbt = stack.getSubNbt(PotionSauce.MOD_ID);
			if (nbt != null && nbt.getBoolean("Sauced")) {
				List<StatusEffectInstance> effects = PotionSauce.getEffects(stack);
				effects.removeIf(instance -> instance.getEffectType().getCategory() == StatusEffectCategory.HARMFUL);
				if (!effects.isEmpty()) {
					lines.add(1, Text.translatable("tooltip.potionsauce.sauce").formatted(Formatting.DARK_GREEN));
					PotionUtil.buildTooltip(effects, lines, 1F);
				}
			}
		}
	}
}
