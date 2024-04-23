package com.github.kramerev3axr.cmdchanger;

import net.minecraft.component.DataComponentTypes;
import net.minecraft.component.type.CustomModelDataComponent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.mojang.brigadier.arguments.IntegerArgumentType;

import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.command.v2.CommandRegistrationCallback;
import net.minecraft.item.ItemStack;
import net.minecraft.server.command.CommandManager;
import net.minecraft.text.Text;

public class Cmdchanger implements ModInitializer {
	
	public static final String MOD_ID = "cmdchanger";
    public static final Logger LOGGER = LoggerFactory.getLogger("cmdchanger");
    
    private static final int CYAN = 5636095;
    private static final int GOLD = 16755200;
    private static final int DARK_RED = 11141120;

	@Override
	public void onInitialize() {
		 CommandRegistrationCallback.EVENT.register((dispatcher, registryAccess, environment) -> {
			dispatcher.register(CommandManager.literal("custommodeldata")
				.then(CommandManager.literal("set")
             				.then(CommandManager.argument("cmd", IntegerArgumentType.integer())
             					.executes(ctx -> {
             						ItemStack selectedItem = ctx.getSource().getPlayer().getInventory().getMainHandStack();
             						Integer cmdNbt = IntegerArgumentType.getInteger(ctx, "cmd");

             						selectedItem.apply(DataComponentTypes.CUSTOM_MODEL_DATA, selectedItem.get(DataComponentTypes.CUSTOM_MODEL_DATA), cmd -> new CustomModelDataComponent(cmdNbt));
             						ctx.getSource().sendFeedback(() -> Text.literal("Set CustomModelData of ")
             										.append(Text.literal(selectedItem.getName().getString()).withColor(CYAN))
             										.append(Text.literal(" to "))
             										.append(Text.literal(String.valueOf(cmdNbt)).withColor(GOLD)), false);
             						return 1;
             					})
             				)
				)
				.then(CommandManager.literal("get")
						.executes(ctx -> {
							ItemStack selectedItem = ctx.getSource().getPlayer().getInventory().getMainHandStack();
							
							if (selectedItem.get(DataComponentTypes.CUSTOM_MODEL_DATA) != null) {
								int cmdNbt = selectedItem.get(DataComponentTypes.CUSTOM_MODEL_DATA).value();
								ctx.getSource().sendFeedback(() ->  Text.literal("")
												.append(Text.literal(selectedItem.getName().getString()).withColor(CYAN))
												.append((" has a CustomModelData of "))
												.append(Text.literal(String.valueOf(cmdNbt)).withColor(GOLD)), false);
							}
							else {
								ctx.getSource().sendFeedback(() -> Text.literal("Error! Please make sure you have a CustomModelData set!").withColor(DARK_RED), false);
							}
							return 1;
						})
					)
				);
		 });
	}
}
