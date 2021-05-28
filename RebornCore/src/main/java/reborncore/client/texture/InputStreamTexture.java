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

package reborncore.client.texture;

import net.minecraft.client.texture.AbstractTexture;
import net.minecraft.client.texture.NativeImage;
import net.minecraft.client.texture.TextureUtil;
import net.minecraft.resource.Resource;
import net.minecraft.resource.ResourceManager;
import net.minecraft.resource.metadata.ResourceMetadataReader;
import net.minecraft.util.Identifier;
import org.apache.commons.io.IOUtils;

import org.jetbrains.annotations.Nullable;
import java.io.IOException;
import java.io.InputStream;

/**
 * Created by modmuss50 on 23/05/2016.
 */
public class InputStreamTexture extends AbstractTexture {
	protected final InputStream textureLocation;
	NativeImage image;
	String name;

	public InputStreamTexture(InputStream textureResourceLocation, String name) {
		this.textureLocation = textureResourceLocation;
		this.name = name;
	}

	@Override
	public void load(ResourceManager resourceManager) throws IOException {
		this.clearGlId();
		if (image == null) {
			Resource iresource = null;
			try {
				iresource = new Resource() {

					@Override
					public Identifier getId() {
						return new Identifier("reborncore:loaded/" + name);
					}

					@Override
					public InputStream getInputStream() {
						return textureLocation;
					}

					@Nullable
					@Override
					public <T> T getMetadata(ResourceMetadataReader<T> iMetadataSectionSerializer) {
						return null;
					}

					@Override
					public String getResourcePackName() {
						return "reborncore";
					}

					@Override
					public void close() {

					}
				};
				image = NativeImage.read(iresource.getInputStream());
			} finally {
				IOUtils.closeQuietly(iresource);
			}
		}
		this.bindTexture();
		TextureUtil.allocate(this.getGlId(), 0, image.getWidth(), image.getHeight());
		image.upload(0, 0, 0, true);
	}
}
