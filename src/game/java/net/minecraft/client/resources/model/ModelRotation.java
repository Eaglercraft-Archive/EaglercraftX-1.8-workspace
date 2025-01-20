package net.minecraft.client.resources.model;

import com.carrotsearch.hppc.IntObjectHashMap;
import com.carrotsearch.hppc.IntObjectMap;

import net.lax1dude.eaglercraft.v1_8.vector.Matrix4f;
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
public enum ModelRotation {
	X0_Y0(0, 0), X0_Y90(0, 90), X0_Y180(0, 180), X0_Y270(0, 270), X90_Y0(90, 0), X90_Y90(90, 90), X90_Y180(90, 180),
	X90_Y270(90, 270), X180_Y0(180, 0), X180_Y90(180, 90), X180_Y180(180, 180), X180_Y270(180, 270), X270_Y0(270, 0),
	X270_Y90(270, 90), X270_Y180(270, 180), X270_Y270(270, 270);

	private static final IntObjectMap<ModelRotation> mapRotations = new IntObjectHashMap<>();
	private final int combinedXY;
	private final Matrix4f matrix4d;
	private final int quartersX;
	private final int quartersY;

	private static int combineXY(int parInt1, int parInt2) {
		return parInt1 * 360 + parInt2;
	}

	private ModelRotation(int parInt2, int parInt3) {
		this.combinedXY = combineXY(parInt2, parInt3);
		this.matrix4d = new Matrix4f();
		Matrix4f matrix4f = new Matrix4f();
		matrix4f.setIdentity();
		Matrix4f.rotate((float) (-parInt2) * 0.017453292F, new Vector3f(1.0F, 0.0F, 0.0F), matrix4f, matrix4f);
		this.quartersX = MathHelper.abs_int(parInt2 / 90);
		Matrix4f matrix4f1 = new Matrix4f();
		matrix4f1.setIdentity();
		Matrix4f.rotate((float) (-parInt3) * 0.017453292F, new Vector3f(0.0F, 1.0F, 0.0F), matrix4f1, matrix4f1);
		this.quartersY = MathHelper.abs_int(parInt3 / 90);
		Matrix4f.mul(matrix4f1, matrix4f, this.matrix4d);
	}

	public Matrix4f getMatrix4d() {
		return this.matrix4d;
	}

	public EnumFacing rotateFace(EnumFacing parEnumFacing) {
		EnumFacing enumfacing = parEnumFacing;

		for (int i = 0; i < this.quartersX; ++i) {
			enumfacing = enumfacing.rotateAround(EnumFacing.Axis.X);
		}

		if (enumfacing.getAxis() != EnumFacing.Axis.Y) {
			for (int j = 0; j < this.quartersY; ++j) {
				enumfacing = enumfacing.rotateAround(EnumFacing.Axis.Y);
			}
		}

		return enumfacing;
	}

	public int rotateVertex(EnumFacing facing, int vertexIndex) {
		int i = vertexIndex;
		if (facing.getAxis() == EnumFacing.Axis.X) {
			i = (vertexIndex + this.quartersX) % 4;
		}

		EnumFacing enumfacing = facing;

		for (int j = 0; j < this.quartersX; ++j) {
			enumfacing = enumfacing.rotateAround(EnumFacing.Axis.X);
		}

		if (enumfacing.getAxis() == EnumFacing.Axis.Y) {
			i = (i + this.quartersY) % 4;
		}

		return i;
	}

	public static ModelRotation getModelRotation(int parInt1, int parInt2) {
		return mapRotations
				.get(combineXY(MathHelper.normalizeAngle(parInt1, 360), MathHelper.normalizeAngle(parInt2, 360)));
	}

	static {
		ModelRotation[] lst = values();
		for (int i = 0; i < lst.length; ++i) {
			mapRotations.put(lst[i].combinedXY, lst[i]);
		}

	}
}