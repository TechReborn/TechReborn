/*
 * This file is part of RebornCore, licensed under the MIT License (MIT).
 *
 * Copyright (c) 2021 TeamReborn
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */

package reborncore;

import org.apache.commons.lang3.Validate;

import java.util.HashMap;
import java.util.function.Function;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;

/**
 * Created by Gigabit101 on 16/08/2016.
 */
public class RebornRegistry {
	//Yeah, this is horrible
	private static final HashMap<Object, ResourceLocation> objIdentMap = new HashMap<>();

	/**
	 * Registers {@link Block} and {@link BlockItem} in vanilla registries
	 *
	 * @param block   {@link Block} Block to register
	 * @param settings {@link Item.Properties} Settings settings for {@link BlockItem}
	 * @param name    {@link ResourceLocation} Registry name for block and item
	 */
	public static void registerBlock(Block block, Item.Properties settings, ResourceLocation name) {
		Registry.register(BuiltInRegistries.BLOCK, name, block);
		BlockItem itemBlock = new BlockItem(block, settings);
		Registry.register(BuiltInRegistries.ITEM, name, itemBlock);
	}

	public static void registerBlock(Block block, Function<Block, BlockItem> blockItemFunction, ResourceLocation name) {
		Registry.register(BuiltInRegistries.BLOCK, name, block);
		BlockItem itemBlock = blockItemFunction.apply(block);
		Registry.register(BuiltInRegistries.ITEM, name, itemBlock);
	}

	/**
	 * Registers Block and BlockItem in vanilla registries.
	 * Block should have registered identifier in RebornRegistry via {@link #registerIdent registerIdent} method
	 *
	 * @param block     {@link Block} Block to register
	 * @param itemGroup {@link Item.Properties} Settings settings for {@link BlockItem}
	 */
	public static void registerBlock(Block block, Item.Properties itemGroup) {
		Validate.isTrue(objIdentMap.containsKey(block));
		registerBlock(block, itemGroup, objIdentMap.get(block));
	}

	public static void registerBlock(Block block, Function<Block, BlockItem> blockItemFunction){
		Validate.isTrue(objIdentMap.containsKey(block));
		registerBlock(block, blockItemFunction, objIdentMap.get(block));
	}

	/**
	 * Register only {@link Block}, without {@link BlockItem} in vanilla registries
	 * Block should have registered identifier in {@link RebornRegistry} via
	 * {@link #registerIdent registerIdent} method
	 *
	 * @param block {@link Block} Block to register
	 */
	public static void registerBlockNoItem(Block block) {
		Validate.isTrue(objIdentMap.containsKey(block));
		Registry.register(BuiltInRegistries.BLOCK, objIdentMap.get(block), block);
	}


	/**
	 * Register {@link Item} in vanilla registries
	 *
	 * @param item {@link Item} Item to register
	 * @param name {@link ResourceLocation} Registry name for item
	 */
	public static void registerItem(Item item, ResourceLocation name) {
		Registry.register(BuiltInRegistries.ITEM, name, item);
	}

	/**
	 * <p>Register {@link Item} in vanilla registries</p>
	 * <p>
	 *  {@link Item} should have registered identifier in {@link RebornRegistry}
	 *  via {@link #registerIdent registerIdent} method
	 * </p>
	 *
	 * @param item {@link Item} Item to register
	 */
	public static void registerItem(Item item){
		Validate.isTrue(objIdentMap.containsKey(item));
		registerItem(item, objIdentMap.get(item));
	}

	/**
	 * Registers {@link ResourceLocation} in internal RebornCore map
	 *
	 * @param object     {@link Object}, {@link Item}, {@link Block} or whatever to be put into map
	 * @param identifier {@link ResourceLocation} Registry name for object
	 */
	public static void registerIdent(Object object, ResourceLocation identifier){
		objIdentMap.put(object, identifier);
	}
}
