package net.minecraft.client.resources;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;

import org.json.JSONException;
import org.json.JSONObject;

import net.lax1dude.eaglercraft.v1_8.IOUtils;
import net.lax1dude.eaglercraft.v1_8.HString;
import net.lax1dude.eaglercraft.v1_8.log4j.LogManager;
import net.lax1dude.eaglercraft.v1_8.log4j.Logger;
import net.lax1dude.eaglercraft.v1_8.minecraft.EaglerFolderResourcePack;
import net.lax1dude.eaglercraft.v1_8.minecraft.ResourceIndex;
import net.lax1dude.eaglercraft.v1_8.opengl.ImageData;
import net.minecraft.client.renderer.texture.TextureUtil;
import net.minecraft.client.resources.data.IMetadataSection;
import net.minecraft.client.resources.data.IMetadataSerializer;
import net.minecraft.util.ResourceLocation;

/**+
 * This portion of EaglercraftX contains deobfuscated Minecraft 1.8 source code.
 * 
 * Minecraft 1.8.8 bytecode is (c) 2015 Mojang AB. "Do not distribute!"
 * Mod Coder Pack v9.18 deobfuscation configs are (c) Copyright by the MCP Team
 * 
 * EaglercraftX 1.8 patch files (c) 2022-2025 lax1dude, ayunami2000. All Rights Reserved.
 * 
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" AND
 * ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED
 * WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE DISCLAIMED.
 * IN NO EVENT SHALL THE COPYRIGHT HOLDER OR CONTRIBUTORS BE LIABLE FOR ANY DIRECT,
 * INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT
 * NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR
 * PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY,
 * WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE)
 * ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE
 * POSSIBILITY OF SUCH DAMAGE.
 * 
 */
public abstract class AbstractResourcePack implements IResourcePack {
	private static final Logger resourceLog = LogManager.getLogger();
	protected final String resourcePackFile;
	protected ResourceIndex resourceIndex;

	public AbstractResourcePack(String resourcePackFileIn) {
		this.resourcePackFile = resourcePackFileIn;
	}

	private static String locationToName(ResourceLocation location) {
		return HString.format("%s/%s/%s",
				new Object[] { "assets", location.getResourceDomain(), location.getResourcePath() });
	}

	public InputStream getInputStream(ResourceLocation location) throws IOException {
		return this.getInputStreamByName(locationToName(location));
	}

	public boolean resourceExists(ResourceLocation location) {
		return this.hasResourceName(locationToName(location));
	}

	protected abstract InputStream getInputStreamByName(String var1) throws IOException;

	protected abstract boolean hasResourceName(String var1);

	protected void logNameNotLowercase(String parString1) {
		resourceLog.warn("ResourcePack: ignored non-lowercase namespace: %s in %s",
				new Object[] { parString1, this.resourcePackFile });
	}

	public <T extends IMetadataSection> T getPackMetadata(IMetadataSerializer parIMetadataSerializer, String parString1)
			throws IOException {
		try {
			return readMetadata(parIMetadataSerializer, this.getInputStreamByName("pack.mcmeta"), parString1);
		} catch (JSONException e) {
			if (this instanceof EaglerFolderResourcePack) {
				EaglerFolderResourcePack.deleteResourcePack((EaglerFolderResourcePack) this);
			}
			throw e;
		}
	}

	static <T extends IMetadataSection> T readMetadata(IMetadataSerializer parIMetadataSerializer,
			InputStream parInputStream, String parString1) {
		JSONObject jsonobject = null;
		try {
			jsonobject = new JSONObject(IOUtils.inputStreamToString(parInputStream, StandardCharsets.UTF_8));
		} catch (RuntimeException | IOException runtimeexception) {
			throw new JSONException(runtimeexception);
		} finally {
			IOUtils.closeQuietly(parInputStream);
		}

		return parIMetadataSerializer.parseMetadataSection(parString1, jsonobject);
	}

	public ImageData getPackImage() throws IOException {
		return TextureUtil.readBufferedImage(this.getInputStreamByName("pack.png"));
	}

	public String getPackName() {
		return this.resourcePackFile;
	}

	public ResourceIndex getEaglerFileIndex() {
		return this.resourceIndex;
	}
}