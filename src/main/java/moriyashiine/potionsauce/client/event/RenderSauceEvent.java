/*
 * Copyright (c) MoriyaShiine. All Rights Reserved.
 */
package moriyashiine.potionsauce.client.event;

import com.google.common.collect.Lists;
import moriyashiine.potionsauce.common.init.ModComponentTypes;
import net.fabricmc.fabric.api.client.item.v1.ItemTooltipCallback;
import net.minecraft.component.DataComponentTypes;
import net.minecraft.component.type.PotionContentsComponent;
import net.minecraft.entity.effect.StatusEffectCategory;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.tooltip.TooltipType;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;

import java.util.ArrayList;
import java.util.List;

public class RenderSauceEvent implements ItemTooltipCallback {
	@Override
	public void getTooltip(ItemStack stack, Item.TooltipContext tooltipContext, TooltipType tooltipType, List<Text> lines) {
		List<StatusEffectInstance> sauceEffects = getSauceEffects(stack);
		if (!sauceEffects.isEmpty()) {
			sauceEffects.removeIf(instance -> instance.getEffectType().value().getCategory() == StatusEffectCategory.HARMFUL);
			if (!sauceEffects.isEmpty()) {
				lines.add(1, Text.translatable("tooltip.potionsauce.sauce").formatted(Formatting.DARK_GREEN));
				List<Text> potionText = new ArrayList<>();
				PotionContentsComponent.buildTooltip(sauceEffects, potionText::add, 1, tooltipContext.getUpdateTickRate());
				for (int i = potionText.size() - 1; i >= 0; i--) {
					lines.add(2, potionText.get(i));
				}
			}
		}
	}

	private static List<StatusEffectInstance> getSauceEffects(ItemStack stack) {
		if (stack.contains(DataComponentTypes.FOOD) && stack.contains(ModComponentTypes.SAUCED)) {
			PotionContentsComponent potionContents = stack.get(DataComponentTypes.POTION_CONTENTS);
			if (potionContents != null) {
				return Lists.newArrayList(potionContents.getEffects());
			}
		}
		return List.of();
	}
}
