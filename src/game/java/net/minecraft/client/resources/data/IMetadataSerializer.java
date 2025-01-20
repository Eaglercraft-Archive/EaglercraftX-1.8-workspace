package net.minecraft.client.resources.data;

import org.json.JSONObject;

import net.lax1dude.eaglercraft.v1_8.json.JSONTypeProvider;
import net.minecraft.util.IRegistry;
import net.minecraft.util.RegistrySimple;

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
public class IMetadataSerializer {
	private final IRegistry<String, IMetadataSerializer.Registration<? extends IMetadataSection>> metadataSectionSerializerRegistry = new RegistrySimple();

	public <T extends IMetadataSection> void registerMetadataSectionType(
			IMetadataSectionSerializer<T> parIMetadataSectionSerializer, Class<T> parClass1) {
		this.metadataSectionSerializerRegistry.putObject(parIMetadataSectionSerializer.getSectionName(),
				new IMetadataSerializer.Registration(parIMetadataSectionSerializer, parClass1));
	}

	public <T extends IMetadataSection> T parseMetadataSection(String parString1, JSONObject parJsonObject) {
		if (parString1 == null) {
			throw new IllegalArgumentException("Metadata section name cannot be null");
		} else if (!parJsonObject.has(parString1)) {
			return (T) null;
		} else if (parJsonObject.optJSONObject(parString1) == null) {
			throw new IllegalArgumentException("Invalid metadata for \'" + parString1 + "\' - expected object, found "
					+ parJsonObject.get(parString1));
		} else {
			IMetadataSerializer.Registration imetadataserializer$registration = (IMetadataSerializer.Registration) this.metadataSectionSerializerRegistry
					.getObject(parString1);
			if (imetadataserializer$registration == null) {
				throw new IllegalArgumentException("Don\'t know how to handle metadata section \'" + parString1 + "\'");
			} else {
				return (T) ((IMetadataSection) JSONTypeProvider.deserialize(parJsonObject.getJSONObject(parString1),
						imetadataserializer$registration.field_110500_b));
			}
		}
	}

	class Registration<T extends IMetadataSection> {
		final IMetadataSectionSerializer<T> field_110502_a;
		final Class<T> field_110500_b;

		private Registration(IMetadataSectionSerializer<T> parIMetadataSectionSerializer, Class<T> parClass1) {
			this.field_110502_a = parIMetadataSectionSerializer;
			this.field_110500_b = parClass1;
		}
	}
}