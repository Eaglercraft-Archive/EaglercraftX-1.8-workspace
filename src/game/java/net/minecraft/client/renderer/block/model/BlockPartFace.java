package net.minecraft.client.renderer.block.model;

import org.json.JSONException;
import org.json.JSONObject;

import net.lax1dude.eaglercraft.v1_8.json.JSONTypeDeserializer;
import net.lax1dude.eaglercraft.v1_8.json.JSONTypeProvider;
import net.minecraft.util.EnumFacing;

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
public class BlockPartFace {
	public static final EnumFacing FACING_DEFAULT = null;
	public final EnumFacing cullFace;
	public final int tintIndex;
	public final String texture;
	public final BlockFaceUV blockFaceUV;

	public BlockPartFace(EnumFacing cullFaceIn, int tintIndexIn, String textureIn, BlockFaceUV blockFaceUVIn) {
		this.cullFace = cullFaceIn;
		this.tintIndex = tintIndexIn;
		this.texture = textureIn;
		this.blockFaceUV = blockFaceUVIn;
	}

	public static class Deserializer implements JSONTypeDeserializer<JSONObject, BlockPartFace> {
		public BlockPartFace deserialize(JSONObject jsonobject) throws JSONException {
			EnumFacing enumfacing = this.parseCullFace(jsonobject);
			int i = this.parseTintIndex(jsonobject);
			String s = this.parseTexture(jsonobject);
			BlockFaceUV blockfaceuv = JSONTypeProvider.deserialize(jsonobject, BlockFaceUV.class);
			return new BlockPartFace(enumfacing, i, s, blockfaceuv);
		}

		protected int parseTintIndex(JSONObject parJsonObject) {
			return parJsonObject.optInt("tintindex", -1);
		}

		private String parseTexture(JSONObject parJsonObject) {
			return parJsonObject.getString("texture");
		}

		private EnumFacing parseCullFace(JSONObject parJsonObject) {
			String s = parJsonObject.optString("cullface", "");
			return EnumFacing.byName(s);
		}
	}
}