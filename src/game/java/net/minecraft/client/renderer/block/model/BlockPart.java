package net.minecraft.client.renderer.block.model;

import java.util.EnumMap;
import java.util.Map;
import java.util.Map.Entry;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.google.common.collect.Maps;

import net.lax1dude.eaglercraft.v1_8.json.JSONTypeDeserializer;
import net.lax1dude.eaglercraft.v1_8.json.JSONTypeProvider;
import net.lax1dude.eaglercraft.v1_8.vector.Vector3f;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.MathHelper;

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
public class BlockPart {
	public final Vector3f positionFrom;
	public final Vector3f positionTo;
	public final Map<EnumFacing, BlockPartFace> mapFaces;
	public final BlockPartRotation partRotation;
	public final boolean shade;

	public BlockPart(Vector3f positionFromIn, Vector3f positionToIn, Map<EnumFacing, BlockPartFace> mapFacesIn,
			BlockPartRotation partRotationIn, boolean shadeIn) {
		this.positionFrom = positionFromIn;
		this.positionTo = positionToIn;
		this.mapFaces = mapFacesIn;
		this.partRotation = partRotationIn;
		this.shade = shadeIn;
		this.setDefaultUvs();
	}

	private void setDefaultUvs() {
		for (Entry entry : this.mapFaces.entrySet()) {
			float[] afloat = this.getFaceUvs((EnumFacing) entry.getKey());
			((BlockPartFace) entry.getValue()).blockFaceUV.setUvs(afloat);
		}

	}

	private float[] getFaceUvs(EnumFacing parEnumFacing) {
		float[] afloat;
		switch (parEnumFacing) {
		case DOWN:
		case UP:
			afloat = new float[] { this.positionFrom.x, this.positionFrom.z, this.positionTo.x, this.positionTo.z };
			break;
		case NORTH:
		case SOUTH:
			afloat = new float[] { this.positionFrom.x, 16.0F - this.positionTo.y, this.positionTo.x,
					16.0F - this.positionFrom.y };
			break;
		case WEST:
		case EAST:
			afloat = new float[] { this.positionFrom.z, 16.0F - this.positionTo.y, this.positionTo.z,
					16.0F - this.positionFrom.y };
			break;
		default:
			throw new NullPointerException();
		}

		return afloat;
	}

	public static class Deserializer implements JSONTypeDeserializer<JSONObject, BlockPart> {
		public BlockPart deserialize(JSONObject jsonobject) throws JSONException {
			Vector3f vector3f = this.parsePositionFrom(jsonobject);
			Vector3f vector3f1 = this.parsePositionTo(jsonobject);
			BlockPartRotation blockpartrotation = this.parseRotation(jsonobject);
			Map map = this.parseFacesCheck(jsonobject);
			if (jsonobject.has("shade") && !(jsonobject.get("shade") instanceof Boolean)) {
				throw new JSONException("Expected shade to be a Boolean");
			} else {
				boolean flag = jsonobject.optBoolean("shade", true);
				return new BlockPart(vector3f, vector3f1, map, blockpartrotation, flag);
			}
		}

		private BlockPartRotation parseRotation(JSONObject parJsonObject) {
			BlockPartRotation blockpartrotation = null;
			if (parJsonObject.has("rotation")) {
				JSONObject jsonobject = parJsonObject.getJSONObject("rotation");
				Vector3f vector3f = this.parsePosition(jsonobject, "origin");
				vector3f.scale(0.0625F);
				EnumFacing.Axis enumfacing$axis = this.parseAxis(jsonobject);
				float f = this.parseAngle(jsonobject);
				boolean flag = jsonobject.optBoolean("rescale", false);
				blockpartrotation = new BlockPartRotation(vector3f, enumfacing$axis, f, flag);
			}

			return blockpartrotation;
		}

		private float parseAngle(JSONObject parJsonObject) {
			float f = parJsonObject.getFloat("angle");
			if (f != 0.0F && MathHelper.abs(f) != 22.5F && MathHelper.abs(f) != 45.0F) {
				throw new JSONException("Invalid rotation " + f + " found, only -45/-22.5/0/22.5/45 allowed");
			} else {
				return f;
			}
		}

		private EnumFacing.Axis parseAxis(JSONObject parJsonObject) {
			String s = parJsonObject.getString("axis");
			EnumFacing.Axis enumfacing$axis = EnumFacing.Axis.byName(s.toLowerCase());
			if (enumfacing$axis == null) {
				throw new JSONException("Invalid rotation axis: " + s);
			} else {
				return enumfacing$axis;
			}
		}

		private Map<EnumFacing, BlockPartFace> parseFacesCheck(JSONObject parJsonObject) {
			Map map = this.parseFaces(parJsonObject);
			if (map.isEmpty()) {
				throw new JSONException("Expected between 1 and 6 unique faces, got 0");
			} else {
				return map;
			}
		}

		private Map<EnumFacing, BlockPartFace> parseFaces(JSONObject parJsonObject) {
			EnumMap enummap = Maps.newEnumMap(EnumFacing.class);
			JSONObject jsonobject = parJsonObject.getJSONObject("faces");

			for (String entry : jsonobject.keySet()) {
				EnumFacing enumfacing = this.parseEnumFacing(entry);
				enummap.put(enumfacing,
						JSONTypeProvider.deserialize(jsonobject.getJSONObject(entry), BlockPartFace.class));
			}

			return enummap;
		}

		private EnumFacing parseEnumFacing(String name) {
			EnumFacing enumfacing = EnumFacing.byName(name);
			if (enumfacing == null) {
				throw new JSONException("Unknown facing: " + name);
			} else {
				return enumfacing;
			}
		}

		private Vector3f parsePositionTo(JSONObject parJsonObject) {
			Vector3f vector3f = this.parsePosition(parJsonObject, "to");
			if (vector3f.x >= -16.0F && vector3f.y >= -16.0F && vector3f.z >= -16.0F && vector3f.x <= 32.0F
					&& vector3f.y <= 32.0F && vector3f.z <= 32.0F) {
				return vector3f;
			} else {
				throw new JSONException("\'to\' specifier exceeds the allowed boundaries: " + vector3f);
			}
		}

		private Vector3f parsePositionFrom(JSONObject parJsonObject) {
			Vector3f vector3f = this.parsePosition(parJsonObject, "from");
			if (vector3f.x >= -16.0F && vector3f.y >= -16.0F && vector3f.z >= -16.0F && vector3f.x <= 32.0F
					&& vector3f.y <= 32.0F && vector3f.z <= 32.0F) {
				return vector3f;
			} else {
				throw new JSONException("\'from\' specifier exceeds the allowed boundaries: " + vector3f);
			}
		}

		private Vector3f parsePosition(JSONObject parJsonObject, String parString1) {
			JSONArray jsonarray = parJsonObject.getJSONArray(parString1);
			if (jsonarray.length() != 3) {
				throw new JSONException("Expected 3 " + parString1 + " values, found: " + jsonarray.length());
			} else {
				float[] afloat = new float[3];

				for (int i = 0; i < afloat.length; ++i) {
					afloat[i] = jsonarray.getFloat(i);
				}

				return new Vector3f(afloat[0], afloat[1], afloat[2]);
			}
		}
	}
}