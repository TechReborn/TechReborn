/*
 * This file is part of RebornCore, licensed under the MIT License (MIT).
 *
 * Copyright (c) 2016-2017 TeamReborn
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

package reborncore.common.config2;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.Map;

import com.mojang.serialization.Codec;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import net.minecraft.resources.Identifier;

import reborncore.common.config2.impl.ConfigGroupImpl;
import reborncore.common.config2.impl.FabricConfigApiImpl;
import reborncore.common.config2.impl.serialization.ConfigParser;
import reborncore.common.config2.impl.serialization.ConfigWriter;

class RebornCoreConfigApiTest {
	@Test
	void writeToSnbt() {
		Config config = RebornCoreConfigApi.config(Identifier.parse("reborncore:write_to_snbt"));
		config.comment("Root configs can have comments too");

		config.stringValue("example_string", "default")
				.comment("This is an example string config value");

		config.intValue("example_int", 123)
				.comment("This is an example int config value");

		ConfigGroup group = config.group("group")
				.comment("Groups can have comments as well");

		group.stringListValue("list", List.of("a", "b", "c"));
		group.codec("map", Codec.unboundedMap(Codec.STRING, Codec.STRING), Map.of("key", "value"))
				.comment("This is an example map config value");

		String snbt = ConfigWriter.writeToSNBT((ConfigGroupImpl) config);

		assertEquals("""
				{
					// This is an example string config value
					example_string: "default",

					// This is an example int config value
					example_int: 123,

					// Groups can have comments as well
					group: {
						list: [
							"a",
							"b",
							"c"
						],

						// This is an example map config value
						map: {
							key: "value"
						}
					}
				}
				""".trim(), snbt);
	}

	@Test
	void readUpdatedValue() {
		Config config = RebornCoreConfigApi.config(Identifier.parse("reborncore:read_updated_value"));
		ConfigValue<String> value = config.stringValue("value", "hello");

		assertEquals("hello", value.get());

		ConfigParser.loadInto("""
				{
					value: "world"
				}
				""", (ConfigGroupImpl) config);

		assertEquals("world", value.get());
	}

	@Test
	void readPartialConfigKeepsDefaultsAndIgnoresUnknownKeys() {
		Config config = RebornCoreConfigApi.config(Identifier.parse("reborncore:partial_config"));
		ConfigValue<String> a = config.stringValue("a", "hello");
		ConfigValue<String> b = config.stringValue("b", "hello");
		ConfigValue<String> c = config.stringValue("c", "hello");

		ConfigParser.loadInto("""
				{
					a: "world",
					c: "world",
					unknown: "ignored"
				}
				""", (ConfigGroupImpl) config);

		assertEquals("world", a.get());
		assertEquals("hello", b.get());
		assertEquals("world", c.get());
	}

	@Test
	void registerFinalizesConfigSpec(@TempDir Path tempDir) {
		Config config = RebornCoreConfigApi.config(Identifier.parse("reborncore:register_finalizes_spec"));
		ConfigValue<String> value = config.stringValue("value", "hello");

		FabricConfigApiImpl.register(config, tempDir);

		assertThrows(IllegalStateException.class, () -> config.stringValue("other", "world"));
		assertThrows(IllegalStateException.class, () -> config.group("group"));
		assertThrows(IllegalStateException.class, () -> value.comment("late comment"));
	}

	@Test
	void registerLoadsFromConfigDir(@TempDir Path tempDir) throws Exception {
		Path configFile = tempDir.resolve("reborncore").resolve("load_from_dir.cfg");
		Files.createDirectories(configFile.getParent());
		Files.writeString(configFile, """
				{
					value: "loaded"
				}
				""");

		Config config = RebornCoreConfigApi.config(Identifier.parse("reborncore:load_from_dir"));
		ConfigValue<String> value = config.stringValue("value", "default");

		FabricConfigApiImpl.register(config, tempDir);

		assertEquals("loaded", value.get());
	}

	@Test
	void registerWritesDefaultsToConfigDir(@TempDir Path tempDir) throws Exception {
		Config config = RebornCoreConfigApi.config(Identifier.parse("reborncore:write_defaults"));
		config.stringValue("value", "default").comment("Example value");

		FabricConfigApiImpl.register(config, tempDir);

		assertEquals("""
				{
					// Example value
					value: "default"
				}
				""".trim(), Files.readString(tempDir.resolve("reborncore").resolve("write_defaults.cfg")).trim());
	}

	@Test
	void rejectInvalidKeys() {
		Config config = RebornCoreConfigApi.config(Identifier.parse("reborncore:reject_invalid_keys"));

		assertThrows(IllegalStateException.class, () -> config.stringValue("has space", "value"));
		assertThrows(IllegalStateException.class, () -> config.group("group name"));
		assertThrows(IllegalStateException.class, () -> config.stringValue("has:colon", "value"));
		assertThrows(IllegalStateException.class, () -> config.stringValue("has\"quote", "value"));
	}

	@Test
	void allowSimpleUnquotedKeys() {
		Config config = RebornCoreConfigApi.config(Identifier.parse("reborncore:allow_simple_unquoted_keys"));

		config.stringValue("simple", "value");
		config.stringValue("with_underscore", "value");
		config.stringValue("with-dash", "value");
		config.stringValue("with.dot", "value");
		config.stringValue("with+plus", "value");
	}
}
