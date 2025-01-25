package net.minecraft.client.renderer.block.model;

import net.lax1dude.eaglercraft.v1_8.minecraft.EaglerTextureAtlasSprite;
import net.lax1dude.eaglercraft.v1_8.opengl.ext.deferred.VertexMarkerState;
import net.lax1dude.eaglercraft.v1_8.vector.Matrix4f;
import net.lax1dude.eaglercraft.v1_8.vector.Vector3f;
import net.lax1dude.eaglercraft.v1_8.vector.Vector4f;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.EnumFaceDirection;
import net.minecraft.client.resources.model.ModelRotation;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.MathHelper;
import net.minecraft.util.Vec3i;

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
public class FaceBakery {
	private static final float field_178418_a = 1.0F / (float) Math.cos(0.39269909262657166D) - 1.0F;
	private static final float field_178417_b = 1.0F / (float) Math.cos(0.7853981852531433D) - 1.0F;

	private int stride = 7;

	public BakedQuad makeBakedQuad(Vector3f posFrom, Vector3f posTo, BlockPartFace face,
			EaglerTextureAtlasSprite sprite, EnumFacing facing, ModelRotation modelRotationIn,
			BlockPartRotation partRotation, boolean uvLocked, boolean shade) {
		stride = 7;
		int[] aint = this.makeQuadVertexData(face, sprite, facing, this.getPositionsDiv16(posFrom, posTo),
				modelRotationIn, partRotation, uvLocked, shade, null);
		Vector3f calcNormal = getNormalFromVertexData(aint);
		EnumFacing enumfacing = getFacingFromVertexData(calcNormal);
		if (uvLocked) {
			this.func_178409_a(aint, enumfacing, face.blockFaceUV, sprite);
		}

		if (partRotation == null) {
			this.func_178408_a(aint, enumfacing);
		}

		stride = 8;
		int[] aint2 = this.makeQuadVertexData(face, sprite, facing, this.getPositionsDiv16(posFrom, posTo),
				modelRotationIn, partRotation, uvLocked, shade, calcNormal);
		if (uvLocked) {
			this.func_178409_a(aint2, enumfacing, face.blockFaceUV, sprite);
		}

		if (partRotation == null) {
			this.func_178408_a(aint2, enumfacing);
		}
		stride = 7;

		return new BakedQuad(aint, aint2, face.tintIndex, enumfacing, sprite);
	}

	private int[] makeQuadVertexData(BlockPartFace partFace, EaglerTextureAtlasSprite sprite, EnumFacing facing,
			float[] modelRotationIn, ModelRotation partRotation, BlockPartRotation uvLocked, boolean shade,
			boolean parFlag2, Vector3f calcNormal) {
		int[] aint = new int[stride * 4];

		for (int i = 0; i < 4; ++i) {
			this.fillVertexData(aint, i, facing, partFace, modelRotationIn, sprite, partRotation, uvLocked, shade,
					parFlag2, calcNormal);
		}

		return aint;
	}

	private int getFaceShadeColor(EnumFacing facing) {
		float f = this.getFaceBrightness(facing);
		int i = MathHelper.clamp_int((int) (f * 255.0F), 0, 255);
		return -16777216 | i << 16 | i << 8 | i;
	}

	private float getFaceBrightness(EnumFacing facing) {
		switch (facing) {
		case DOWN:
			return 0.5F;
		case UP:
			return 1.0F;
		case NORTH:
		case SOUTH:
			return 0.8F;
		case WEST:
		case EAST:
			return 0.6F;
		default:
			return 1.0F;
		}
	}

	private float[] getPositionsDiv16(Vector3f pos1, Vector3f pos2) {
		float[] afloat = new float[EnumFacing._VALUES.length];
		afloat[EnumFaceDirection.Constants.WEST_INDEX] = pos1.x / 16.0F;
		afloat[EnumFaceDirection.Constants.DOWN_INDEX] = pos1.y / 16.0F;
		afloat[EnumFaceDirection.Constants.NORTH_INDEX] = pos1.z / 16.0F;
		afloat[EnumFaceDirection.Constants.EAST_INDEX] = pos2.x / 16.0F;
		afloat[EnumFaceDirection.Constants.UP_INDEX] = pos2.y / 16.0F;
		afloat[EnumFaceDirection.Constants.SOUTH_INDEX] = pos2.z / 16.0F;
		return afloat;
	}

	private void fillVertexData(int[] faceData, int vertexIndex, EnumFacing facing, BlockPartFace partFace,
			float[] sprite, EaglerTextureAtlasSprite modelRotationIn, ModelRotation partRotation,
			BlockPartRotation uvLocked, boolean shade, boolean parFlag2, Vector3f calcNormal) {
		EnumFacing enumfacing = partRotation.rotateFace(facing);
		int i = (parFlag2 && (stride != 8 || !Minecraft.getMinecraft().gameSettings.shaders))
				? this.getFaceShadeColor(enumfacing)
				: -1;
		EnumFaceDirection.VertexInformation enumfacedirection$vertexinformation = EnumFaceDirection.getFacing(facing)
				.func_179025_a(vertexIndex);
		Vector3f vector3f = new Vector3f(sprite[enumfacedirection$vertexinformation.field_179184_a],
				sprite[enumfacedirection$vertexinformation.field_179182_b],
				sprite[enumfacedirection$vertexinformation.field_179183_c]);
		this.func_178407_a(vector3f, uvLocked);
		int j = this.rotateVertex(vector3f, facing, vertexIndex, partRotation, shade);
		this.storeVertexData(faceData, j, vertexIndex, vector3f, i, modelRotationIn, partFace.blockFaceUV, enumfacing,
				calcNormal);
	}

	private void storeVertexData(int[] faceData, int storeIndex, int vertexIndex, Vector3f position, int shadeColor,
			EaglerTextureAtlasSprite sprite, BlockFaceUV faceUV, EnumFacing facing, Vector3f calcNormal) {
		int i = storeIndex * stride;
		faceData[i + 3] = shadeColor;
		faceData[i + 4] = Float.floatToRawIntBits(sprite.getInterpolatedU((double) faceUV.func_178348_a(vertexIndex)));
		faceData[i + 4 + 1] = Float
				.floatToRawIntBits(sprite.getInterpolatedV((double) faceUV.func_178346_b(vertexIndex)));
		if (stride == 8) {
			if (!Minecraft.getMinecraft().gameSettings.shaders) {
				faceData[i] = Float.floatToRawIntBits(position.x);
				faceData[i + 1] = Float.floatToRawIntBits(position.y);
				faceData[i + 2] = Float.floatToRawIntBits(position.z);
			} else {
				faceData[i] = Float.floatToRawIntBits(position.x * VertexMarkerState.localCoordDeriveHackX);
				faceData[i + 1] = Float.floatToRawIntBits(position.y * VertexMarkerState.localCoordDeriveHackY);
				faceData[i + 2] = Float.floatToRawIntBits(position.z * VertexMarkerState.localCoordDeriveHackZ);
			}
			if (calcNormal != null) {
				int x = (byte) ((int) (calcNormal.x * 127.0F)) & 255;
				int y = (byte) ((int) (calcNormal.y * 127.0F)) & 255;
				int z = (byte) ((int) (calcNormal.z * 127.0F)) & 255;
				int l = x | y << 8 | z << 16 | ((byte) VertexMarkerState.markId) << 24;
				faceData[i + 6] = l;
			} else {
				Vec3i vec = facing.getDirectionVec();
				int x = (byte) ((int) (vec.x * 127.0F)) & 255;
				int y = (byte) ((int) (vec.y * 127.0F)) & 255;
				int z = (byte) ((int) (vec.z * 127.0F)) & 255;
				int l = x | y << 8 | z << 16 | ((byte) VertexMarkerState.markId) << 24;
				faceData[i + 6] = l;
			}
		} else {
			faceData[i] = Float.floatToRawIntBits(position.x);
			faceData[i + 1] = Float.floatToRawIntBits(position.y);
			faceData[i + 2] = Float.floatToRawIntBits(position.z);
		}
	}

	private void func_178407_a(Vector3f partRotation, BlockPartRotation parBlockPartRotation) {
		if (parBlockPartRotation != null) {
			Matrix4f matrix4f = this.getMatrixIdentity();
			Vector3f vector3f = new Vector3f(0.0F, 0.0F, 0.0F);
			switch (parBlockPartRotation.axis) {
			case X:
				Matrix4f.rotate(parBlockPartRotation.angle * 0.017453292F, new Vector3f(1.0F, 0.0F, 0.0F), matrix4f,
						matrix4f);
				vector3f.set(0.0F, 1.0F, 1.0F);
				break;
			case Y:
				Matrix4f.rotate(parBlockPartRotation.angle * 0.017453292F, new Vector3f(0.0F, 1.0F, 0.0F), matrix4f,
						matrix4f);
				vector3f.set(1.0F, 0.0F, 1.0F);
				break;
			case Z:
				Matrix4f.rotate(parBlockPartRotation.angle * 0.017453292F, new Vector3f(0.0F, 0.0F, 1.0F), matrix4f,
						matrix4f);
				vector3f.set(1.0F, 1.0F, 0.0F);
			}

			if (parBlockPartRotation.rescale) {
				if (Math.abs(parBlockPartRotation.angle) == 22.5F) {
					vector3f.scale(field_178418_a);
				} else {
					vector3f.scale(field_178417_b);
				}

				Vector3f.add(vector3f, new Vector3f(1.0F, 1.0F, 1.0F), vector3f);
			} else {
				vector3f.set(1.0F, 1.0F, 1.0F);
			}

			this.rotateScale(partRotation, new Vector3f(parBlockPartRotation.origin), matrix4f, vector3f);
		}
	}

	public int rotateVertex(Vector3f position, EnumFacing facing, int vertexIndex, ModelRotation modelRotationIn,
			boolean uvLocked) {
		if (modelRotationIn == ModelRotation.X0_Y0) {
			return vertexIndex;
		} else {
			this.rotateScale(position, new Vector3f(0.5F, 0.5F, 0.5F), modelRotationIn.getMatrix4d(),
					new Vector3f(1.0F, 1.0F, 1.0F));
			return modelRotationIn.rotateVertex(facing, vertexIndex);
		}
	}

	private void rotateScale(Vector3f position, Vector3f rotationOrigin, Matrix4f rotationMatrix, Vector3f scale) {
		Vector4f vector4f = new Vector4f(position.x - rotationOrigin.x, position.y - rotationOrigin.y,
				position.z - rotationOrigin.z, 1.0F);
		Matrix4f.transform(rotationMatrix, vector4f, vector4f);
		vector4f.x *= scale.x;
		vector4f.y *= scale.y;
		vector4f.z *= scale.z;
		position.set(vector4f.x + rotationOrigin.x, vector4f.y + rotationOrigin.y, vector4f.z + rotationOrigin.z);
	}

	private Matrix4f getMatrixIdentity() {
		Matrix4f matrix4f = new Matrix4f();
		matrix4f.setIdentity();
		return matrix4f;
	}

	public static Vector3f getNormalFromVertexData(int[] faceData) {
		Vector3f vector3f = new Vector3f(Float.intBitsToFloat(faceData[0]), Float.intBitsToFloat(faceData[1]),
				Float.intBitsToFloat(faceData[2]));
		Vector3f vector3f1 = new Vector3f(Float.intBitsToFloat(faceData[7]), Float.intBitsToFloat(faceData[8]),
				Float.intBitsToFloat(faceData[9]));
		Vector3f vector3f2 = new Vector3f(Float.intBitsToFloat(faceData[14]), Float.intBitsToFloat(faceData[15]),
				Float.intBitsToFloat(faceData[16]));
		Vector3f vector3f3 = new Vector3f();
		Vector3f vector3f4 = new Vector3f();
		Vector3f vector3f5 = new Vector3f();
		Vector3f.sub(vector3f, vector3f1, vector3f3);
		Vector3f.sub(vector3f2, vector3f1, vector3f4);
		Vector3f.cross(vector3f4, vector3f3, vector3f5);
		float f = (float) Math
				.sqrt((double) (vector3f5.x * vector3f5.x + vector3f5.y * vector3f5.y + vector3f5.z * vector3f5.z));
		vector3f5.x /= f;
		vector3f5.y /= f;
		vector3f5.z /= f;
		return vector3f5;
	}

	public static EnumFacing getFacingFromVertexData(Vector3f normal) {
		EnumFacing enumfacing = null;
		float f1 = 0.0F;

		EnumFacing[] facings = EnumFacing._VALUES;
		for (int i = 0; i < facings.length; ++i) {
			EnumFacing enumfacing1 = facings[i];
			Vec3i vec3i = enumfacing1.getDirectionVec();
			Vector3f vector3f6 = new Vector3f((float) vec3i.getX(), (float) vec3i.getY(), (float) vec3i.getZ());
			float f2 = Vector3f.dot(normal, vector3f6);
			if (f2 >= 0.0F && f2 > f1) {
				f1 = f2;
				enumfacing = enumfacing1;
			}
		}

		if (enumfacing == null) {
			return EnumFacing.UP;
		} else {
			return enumfacing;
		}
	}

	public void func_178409_a(int[] facing, EnumFacing parEnumFacing, BlockFaceUV parBlockFaceUV,
			EaglerTextureAtlasSprite parTextureAtlasSprite) {
		for (int i = 0; i < 4; ++i) {
			this.func_178401_a(i, facing, parEnumFacing, parBlockFaceUV, parTextureAtlasSprite);
		}

	}

	private void func_178408_a(int[] parArrayOfInt, EnumFacing parEnumFacing) {
		int[] aint = new int[parArrayOfInt.length];
		System.arraycopy(parArrayOfInt, 0, aint, 0, parArrayOfInt.length);
		float[] afloat = new float[EnumFacing._VALUES.length];
		afloat[EnumFaceDirection.Constants.WEST_INDEX] = 999.0F;
		afloat[EnumFaceDirection.Constants.DOWN_INDEX] = 999.0F;
		afloat[EnumFaceDirection.Constants.NORTH_INDEX] = 999.0F;
		afloat[EnumFaceDirection.Constants.EAST_INDEX] = -999.0F;
		afloat[EnumFaceDirection.Constants.UP_INDEX] = -999.0F;
		afloat[EnumFaceDirection.Constants.SOUTH_INDEX] = -999.0F;

		for (int i = 0; i < 4; ++i) {
			int j = stride * i;
			float f = Float.intBitsToFloat(aint[j]);
			float f1 = Float.intBitsToFloat(aint[j + 1]);
			float f2 = Float.intBitsToFloat(aint[j + 2]);
			if (f < afloat[EnumFaceDirection.Constants.WEST_INDEX]) {
				afloat[EnumFaceDirection.Constants.WEST_INDEX] = f;
			}

			if (f1 < afloat[EnumFaceDirection.Constants.DOWN_INDEX]) {
				afloat[EnumFaceDirection.Constants.DOWN_INDEX] = f1;
			}

			if (f2 < afloat[EnumFaceDirection.Constants.NORTH_INDEX]) {
				afloat[EnumFaceDirection.Constants.NORTH_INDEX] = f2;
			}

			if (f > afloat[EnumFaceDirection.Constants.EAST_INDEX]) {
				afloat[EnumFaceDirection.Constants.EAST_INDEX] = f;
			}

			if (f1 > afloat[EnumFaceDirection.Constants.UP_INDEX]) {
				afloat[EnumFaceDirection.Constants.UP_INDEX] = f1;
			}

			if (f2 > afloat[EnumFaceDirection.Constants.SOUTH_INDEX]) {
				afloat[EnumFaceDirection.Constants.SOUTH_INDEX] = f2;
			}
		}

		EnumFaceDirection enumfacedirection = EnumFaceDirection.getFacing(parEnumFacing);

		for (int i1 = 0; i1 < 4; ++i1) {
			int j1 = stride * i1;
			EnumFaceDirection.VertexInformation enumfacedirection$vertexinformation = enumfacedirection
					.func_179025_a(i1);
			float f8 = afloat[enumfacedirection$vertexinformation.field_179184_a];
			float f3 = afloat[enumfacedirection$vertexinformation.field_179182_b];
			float f4 = afloat[enumfacedirection$vertexinformation.field_179183_c];
			parArrayOfInt[j1] = Float.floatToRawIntBits(f8);
			parArrayOfInt[j1 + 1] = Float.floatToRawIntBits(f3);
			parArrayOfInt[j1 + 2] = Float.floatToRawIntBits(f4);

			for (int k = 0; k < 4; ++k) {
				int l = stride * k;
				float f5 = Float.intBitsToFloat(aint[l]);
				float f6 = Float.intBitsToFloat(aint[l + 1]);
				float f7 = Float.intBitsToFloat(aint[l + 2]);
				if (MathHelper.epsilonEquals(f8, f5) && MathHelper.epsilonEquals(f3, f6)
						&& MathHelper.epsilonEquals(f4, f7)) {
					parArrayOfInt[j1 + 4] = aint[l + 4];
					parArrayOfInt[j1 + 4 + 1] = aint[l + 4 + 1];
				}
			}
		}

	}

	private void func_178401_a(int facing, int[] parArrayOfInt, EnumFacing parEnumFacing, BlockFaceUV parBlockFaceUV,
			EaglerTextureAtlasSprite parTextureAtlasSprite) {
		int i = stride * facing;
		float f = Float.intBitsToFloat(parArrayOfInt[i]);
		float f1 = Float.intBitsToFloat(parArrayOfInt[i + 1]);
		float f2 = Float.intBitsToFloat(parArrayOfInt[i + 2]);
		if (f < -0.1F || f >= 1.1F) {
			f -= (float) MathHelper.floor_float(f);
		}

		if (f1 < -0.1F || f1 >= 1.1F) {
			f1 -= (float) MathHelper.floor_float(f1);
		}

		if (f2 < -0.1F || f2 >= 1.1F) {
			f2 -= (float) MathHelper.floor_float(f2);
		}

		float f3 = 0.0F;
		float f4 = 0.0F;
		switch (parEnumFacing) {
		case DOWN:
			f3 = f * 16.0F;
			f4 = (1.0F - f2) * 16.0F;
			break;
		case UP:
			f3 = f * 16.0F;
			f4 = f2 * 16.0F;
			break;
		case NORTH:
			f3 = (1.0F - f) * 16.0F;
			f4 = (1.0F - f1) * 16.0F;
			break;
		case SOUTH:
			f3 = f * 16.0F;
			f4 = (1.0F - f1) * 16.0F;
			break;
		case WEST:
			f3 = f2 * 16.0F;
			f4 = (1.0F - f1) * 16.0F;
			break;
		case EAST:
			f3 = (1.0F - f2) * 16.0F;
			f4 = (1.0F - f1) * 16.0F;
		}

		int j = parBlockFaceUV.func_178345_c(facing) * stride;
		parArrayOfInt[j + 4] = Float.floatToRawIntBits(parTextureAtlasSprite.getInterpolatedU((double) f3));
		parArrayOfInt[j + 4 + 1] = Float.floatToRawIntBits(parTextureAtlasSprite.getInterpolatedV((double) f4));
	}
}