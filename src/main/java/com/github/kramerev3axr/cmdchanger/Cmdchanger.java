package com.github.kramerev3axr.cmdchanger;

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
	    
    	private static int CYAN = 5636095;
    	private static int GOLD = 16755200;
    	private static int DARK_RED = 11141120;

	@Override
	public void onInitialize() {
		 CommandRegistrationCallback.EVENT.register((dispatcher, registryAccess, environment) -> {
			 dispatcher.register(CommandManager.literal("custommodeldata")
					.then(CommandManager.literal("set")
             					.then(CommandManager.argument("cmd", IntegerArgumentType.integer())
             						.executes(ctx -> {
	             						ItemStack selectedItem = ctx.getSource().getPlayer().getInventory().getMainHandStack();
             							int cmdNbt = IntegerArgumentType.getInteger(ctx, "cmd");
             						
             							selectedItem.getOrCreateNbt().putInt("CustomModelData", cmdNbt);
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
								
								if (selectedItem.hasNbt()) {
									int cmdNbt = selectedItem.getNbt().getInt("CustomModelData");
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
