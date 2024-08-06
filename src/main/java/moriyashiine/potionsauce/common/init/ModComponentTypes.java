/*
 * Copyright (c) MoriyaShiine. All Rights Reserved.
 */
package moriyashiine.potionsauce.common.init;

import com.mojang.serialization.Codec;
import moriyashiine.potionsauce.common.PotionSauce;
import net.minecraft.component.ComponentType;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.util.Unit;

public class ModComponentTypes {
	public static final ComponentType<Unit> SAUCED = new ComponentType.Builder<Unit>().codec(Codec.unit(Unit.INSTANCE)).packetCodec(PacketCodec.unit(Unit.INSTANCE)).build();

	public static void init() {
		Registry.register(Registries.DATA_COMPONENT_TYPE, PotionSauce.id("sauced"), SAUCED);
	}
}
