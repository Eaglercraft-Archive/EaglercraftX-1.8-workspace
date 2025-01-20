package net.minecraft.client.renderer;

import static net.lax1dude.eaglercraft.v1_8.opengl.RealOpenGLEnums.*;

import net.lax1dude.eaglercraft.v1_8.opengl.EaglercraftGPU;
import net.lax1dude.eaglercraft.v1_8.opengl.GlStateManager;
import net.minecraft.block.Block;
import net.minecraft.block.BlockLiquid;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.BlockPos;
import net.minecraft.util.MathHelper;
import net.minecraft.util.Vec3;
import net.minecraft.world.World;

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
public class ActiveRenderInfo {
	/**+
	 * The current GL viewport
	 */
	private static final int[] VIEWPORT = new int[4];
	/**+
	 * The current GL modelview matrix
	 */
	private static final float[] MODELVIEW = new float[16];
	/**+
	 * The current GL projection matrix
	 */
	private static final float[] PROJECTION = new float[16];
	/**+
	 * The computed view object coordinates
	 */
	private static final float[] OBJECTCOORDS = new float[3];
	private static Vec3 position = new Vec3(0.0D, 0.0D, 0.0D);
	private static float rotationX;
	private static float rotationXZ;
	private static float rotationZ;
	private static float rotationYZ;
	private static float rotationXY;

	/**+
	 * Updates the current render info and camera location based on
	 * entity look angles and 1st/3rd person view mode
	 */
	public static void updateRenderInfo(EntityPlayer entityplayerIn, boolean parFlag) {
		GlStateManager.getFloat(GL_MODELVIEW_MATRIX, MODELVIEW);
		GlStateManager.getFloat(GL_PROJECTION_MATRIX, PROJECTION);
		EaglercraftGPU.glGetInteger(GL_VIEWPORT, VIEWPORT);
		float f = (float) ((VIEWPORT[0] + VIEWPORT[2]) / 2);
		float f1 = (float) ((VIEWPORT[1] + VIEWPORT[3]) / 2);
		GlStateManager.gluUnProject(f, f1, 0.0F, MODELVIEW, PROJECTION, VIEWPORT, OBJECTCOORDS);
		position = new Vec3((double) OBJECTCOORDS[0], (double) OBJECTCOORDS[1], (double) OBJECTCOORDS[2]);
		int i = parFlag ? 1 : 0;
		float f2 = entityplayerIn.rotationPitch;
		float f3 = entityplayerIn.rotationYaw;
		rotationX = MathHelper.cos(f3 * 3.1415927F / 180.0F) * (float) (1 - i * 2);
		rotationZ = MathHelper.sin(f3 * 3.1415927F / 180.0F) * (float) (1 - i * 2);
		rotationYZ = -rotationZ * MathHelper.sin(f2 * 3.1415927F / 180.0F) * (float) (1 - i * 2);
		rotationXY = rotationX * MathHelper.sin(f2 * 3.1415927F / 180.0F) * (float) (1 - i * 2);
		rotationXZ = MathHelper.cos(f2 * 3.1415927F / 180.0F);
	}

	public static Vec3 projectViewFromEntity(Entity parEntity, double parDouble1) {
		double d0 = parEntity.prevPosX + (parEntity.posX - parEntity.prevPosX) * parDouble1;
		double d1 = parEntity.prevPosY + (parEntity.posY - parEntity.prevPosY) * parDouble1 + parEntity.getEyeHeight();
		double d2 = parEntity.prevPosZ + (parEntity.posZ - parEntity.prevPosZ) * parDouble1;
		double d3 = d0 + position.xCoord;
		double d4 = d1 + position.yCoord;
		double d5 = d2 + position.zCoord;
		return new Vec3(d3, d4, d5);
	}

	public static Block getBlockAtEntityViewpoint(World worldIn, Entity parEntity, float parFloat1) {
		Vec3 vec3 = projectViewFromEntity(parEntity, (double) parFloat1);
		BlockPos blockpos = new BlockPos(vec3);
		IBlockState iblockstate = worldIn.getBlockState(blockpos);
		Block block = iblockstate.getBlock();
		if (block.getMaterial().isLiquid()) {
			float f = 0.0F;
			if (iblockstate.getBlock() instanceof BlockLiquid) {
				f = BlockLiquid.getLiquidHeightPercent(((Integer) iblockstate.getValue(BlockLiquid.LEVEL)).intValue())
						- 0.11111111F;
			}

			float f1 = (float) (blockpos.getY() + 1) - f;
			if (vec3.yCoord >= (double) f1) {
				block = worldIn.getBlockState(blockpos.up()).getBlock();
			}
		}

		return block;
	}

	public static Vec3 getPosition() {
		return position;
	}

	public static float getRotationX() {
		return rotationX;
	}

	public static float getRotationXZ() {
		return rotationXZ;
	}

	public static float getRotationZ() {
		return rotationZ;
	}

	public static float getRotationYZ() {
		return rotationYZ;
	}

	public static float getRotationXY() {
		return rotationXY;
	}
}