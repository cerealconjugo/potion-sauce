/*
 * All Rights Reserved (c) MoriyaShiine
 */

package moriyashiine.potionsauce.client;

import moriyashiine.potionsauce.client.event.RenderSauceEvent;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.item.v1.ItemTooltipCallback;

public class PotionSauceClient implements ClientModInitializer {
	@Override
	public void onInitializeClient() {
		ItemTooltipCallback.EVENT.register(new RenderSauceEvent());
	}
}
