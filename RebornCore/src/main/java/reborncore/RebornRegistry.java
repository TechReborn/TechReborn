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

import net.minecraft.block.Block;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;
import org.apache.commons.lang3.Validate;

import java.util.HashMap;
import java.util.function.Function;

/**
 * Created by Gigabit101 on 16/08/2016.
 */
public class RebornRegistry {
	//public static LootManager.InnerPool lp = new LootManager.InnerPool();

	//Yeah, this is horrible
	private static final HashMap<Object, Identifier> objIdentMap = new HashMap<>();

	/**
	 * Registers Block and BlockItem in vanilla registries
	 *
	 * @param block Block Block to register
	 * @param builder Item.Settings Settings builder for BlockItem
	 * @param name Identifier Registry name for block and item
	 */
	public static void registerBlock(Block block, Item.Settings builder, Identifier name) {
		Registry.register(Registry.BLOCK, name, block);
		BlockItem itemBlock = new BlockItem(block, builder);
		Registry.register(Registry.ITEM, name, itemBlock);
	}

	public static void registerBlock(Block block, Function<Block, BlockItem> blockItemFunction, Identifier name) {
		Registry.register(Registry.BLOCK, name, block);
		BlockItem itemBlock = blockItemFunction.apply(block);
		Registry.register(Registry.ITEM, name, itemBlock);
	}

	/**
	 * Registers Block and BlockItem in vanilla registries. 
	 * Block should have registered identifier in RebornRegistry via {@link #registerIdent registerIdent} method
	 *
	 * @param block Block Block to register
	 * @param itemGroup Item.Settings Settings builder for BlockItem
	 */
	public static void registerBlock(Block block, Item.Settings itemGroup) {
		Validate.isTrue(objIdentMap.containsKey(block));
		registerBlock(block, itemGroup, objIdentMap.get(block));
	}

	public static void registerBlock(Block block, Function<Block, BlockItem> blockItemFunction){
		Validate.isTrue(objIdentMap.containsKey(block));
		registerBlock(block, blockItemFunction, objIdentMap.get(block));
	}

	/**
	 * Register only Block, without BlockItem in vanilla registries
	 * Block should have registered identifier in RebornRegistry via {@link #registerIdent registerIdent} method
	 * @param block Block Block to register
	 */
	public static void registerBlockNoItem(Block block) {
		Validate.isTrue(objIdentMap.containsKey(block));
		Registry.register(Registry.BLOCK, objIdentMap.get(block), block);
	}


	/**
	 * Register Item in vanilla registries
	 *
	 * @param item Item Item to register
	 * @param name Identifier Registry name for item
	 */
	public static void registerItem(Item item, Identifier name) {
		Registry.register(Registry.ITEM, name, item);
	}

	/**
	 * Register Item in vanilla registries
	 * Item should have registered identifier in RebornRegistry via {@link #registerIdent registerIdent} method
	 *
	 * @param item Item Item to register
	 */
	public static void registerItem(Item item){
		Validate.isTrue(objIdentMap.containsKey(item));
		registerItem(item, objIdentMap.get(item));
	}

	/**
	 * Registers Identifier in internal RebornCore map
	 *
	 * @param object Object Item, Block or whatever to be put into map
	 * @param identifier Identifier Registry name for object
	 */
	public static void registerIdent(Object object, Identifier identifier){
		objIdentMap.put(object, identifier);
	}

	//eg: RebornRegistry.addLoot(Items.NETHER_STAR, 0.95, LootTableList.CHESTS_VILLAGE_BLACKSMITH);
	//eg: RebornRegistry.addLoot(Items.DIAMOND, 1.95, LootTableList.ENTITIES_COW);

	public static void addLoot(Item item, double chance, Identifier list) {
	//	lp.addItem(LootManager.createLootEntry(item, chance, list));
	}

	public static void addLoot(Item item, int minSize, int maxSize, double chance, Identifier list) {
	//	lp.addItem(LootManager.createLootEntry(item, minSize, maxSize, chance, list));
	}

}
