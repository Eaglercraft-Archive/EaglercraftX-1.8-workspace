package net.minecraft.client.renderer.block.model;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import net.lax1dude.eaglercraft.v1_8.json.JSONTypeDeserializer;

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
public class BlockFaceUV {
	public float[] uvs;
	public final int rotation;

	public BlockFaceUV(float[] uvsIn, int rotationIn) {
		this.uvs = uvsIn;
		this.rotation = rotationIn;
	}

	public float func_178348_a(int parInt1) {
		if (this.uvs == null) {
			throw new NullPointerException("uvs");
		} else {
			int i = this.func_178347_d(parInt1);
			return i != 0 && i != 1 ? this.uvs[2] : this.uvs[0];
		}
	}

	public float func_178346_b(int parInt1) {
		if (this.uvs == null) {
			throw new NullPointerException("uvs");
		} else {
			int i = this.func_178347_d(parInt1);
			return i != 0 && i != 3 ? this.uvs[3] : this.uvs[1];
		}
	}

	private int func_178347_d(int parInt1) {
		return (parInt1 + this.rotation / 90) % 4;
	}

	public int func_178345_c(int parInt1) {
		return (parInt1 + (4 - this.rotation / 90)) % 4;
	}

	public void setUvs(float[] uvsIn) {
		if (this.uvs == null) {
			this.uvs = uvsIn;
		}

	}

	public static class Deserializer implements JSONTypeDeserializer<JSONObject, BlockFaceUV> {
		public BlockFaceUV deserialize(JSONObject jsonobject) throws JSONException {
			float[] afloat = this.parseUV(jsonobject);
			int i = this.parseRotation(jsonobject);
			return new BlockFaceUV(afloat, i);
		}

		protected int parseRotation(JSONObject parJsonObject) {
			int i = parJsonObject.optInt("rotation", 0);
			if (i >= 0 && i % 90 == 0 && i / 90 <= 3) {
				return i;
			} else {
				throw new JSONException("Invalid rotation " + i + " found, only 0/90/180/270 allowed");
			}
		}

		private float[] parseUV(JSONObject parJsonObject) {
			if (!parJsonObject.has("uv")) {
				return null;
			} else {
				JSONArray jsonarray = parJsonObject.getJSONArray("uv");
				if (jsonarray.length() != 4) {
					throw new JSONException("Expected 4 uv values, found: " + jsonarray.length());
				} else {
					float[] afloat = new float[4];

					for (int i = 0; i < afloat.length; ++i) {
						afloat[i] = jsonarray.getFloat(i);
					}

					return afloat;
				}
			}
		}
	}
}