package net.minecraft.client.resources;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.Map;

import org.json.JSONException;
import org.json.JSONObject;

import com.google.common.collect.Maps;

import net.lax1dude.eaglercraft.v1_8.IOUtils;
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
public class SimpleResource implements IResource {
	private final Map<String, IMetadataSection> mapMetadataSections = Maps.newHashMap();
	private final String resourcePackName;
	private final ResourceLocation srResourceLocation;
	private final InputStream resourceInputStream;
	private final InputStream mcmetaInputStream;
	private final IMetadataSerializer srMetadataSerializer;
	private boolean mcmetaJsonChecked;
	private JSONObject mcmetaJson;

	public SimpleResource(String resourcePackNameIn, ResourceLocation srResourceLocationIn,
			InputStream resourceInputStreamIn, InputStream mcmetaInputStreamIn,
			IMetadataSerializer srMetadataSerializerIn) {
		this.resourcePackName = resourcePackNameIn;
		this.srResourceLocation = srResourceLocationIn;
		this.resourceInputStream = resourceInputStreamIn;
		this.mcmetaInputStream = mcmetaInputStreamIn;
		this.srMetadataSerializer = srMetadataSerializerIn;
	}

	public ResourceLocation getResourceLocation() {
		return this.srResourceLocation;
	}

	public InputStream getInputStream() {
		return this.resourceInputStream;
	}

	public boolean hasMetadata() {
		return this.mcmetaInputStream != null;
	}

	public <T extends IMetadataSection> T getMetadata(String s) {
		if (!this.hasMetadata()) {
			return (T) null;
		} else {
			if (this.mcmetaJson == null && !this.mcmetaJsonChecked) {
				this.mcmetaJsonChecked = true;

				try {
					this.mcmetaJson = new JSONObject(
							IOUtils.inputStreamToString(this.mcmetaInputStream, StandardCharsets.UTF_8));
				} catch (IOException e) {
					throw new JSONException(e);
				} finally {
					IOUtils.closeQuietly(this.mcmetaInputStream);
				}
			}

			IMetadataSection imetadatasection = (IMetadataSection) this.mapMetadataSections.get(s);
			if (imetadatasection == null) {
				imetadatasection = this.srMetadataSerializer.parseMetadataSection(s, this.mcmetaJson);
			}

			return (T) imetadatasection;
		}
	}

	public String getResourcePackName() {
		return this.resourcePackName;
	}

	public boolean equals(Object object) {
		if (this == object) {
			return true;
		} else if (!(object instanceof SimpleResource)) {
			return false;
		} else {
			SimpleResource simpleresource = (SimpleResource) object;
			if (this.srResourceLocation != null) {
				if (!this.srResourceLocation.equals(simpleresource.srResourceLocation)) {
					return false;
				}
			} else if (simpleresource.srResourceLocation != null) {
				return false;
			}

			if (this.resourcePackName != null) {
				if (!this.resourcePackName.equals(simpleresource.resourcePackName)) {
					return false;
				}
			} else if (simpleresource.resourcePackName != null) {
				return false;
			}

			return true;
		}
	}

	public int hashCode() {
		int i = this.resourcePackName != null ? this.resourcePackName.hashCode() : 0;
		i = 31 * i + (this.srResourceLocation != null ? this.srResourceLocation.hashCode() : 0);
		return i;
	}
}