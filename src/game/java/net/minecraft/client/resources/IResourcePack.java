package net.minecraft.client.resources;

import java.io.IOException;
import java.io.InputStream;
import java.util.Set;

import net.lax1dude.eaglercraft.v1_8.minecraft.ResourceIndex;
import net.lax1dude.eaglercraft.v1_8.opengl.ImageData;
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
public interface IResourcePack {
	InputStream getInputStream(ResourceLocation var1) throws IOException;

	boolean resourceExists(ResourceLocation var1);

	Set<String> getResourceDomains();

	<T extends IMetadataSection> T getPackMetadata(IMetadataSerializer var1, String var2) throws IOException;

	ImageData getPackImage() throws IOException;

	String getPackName();

	ResourceIndex getEaglerFileIndex();
}