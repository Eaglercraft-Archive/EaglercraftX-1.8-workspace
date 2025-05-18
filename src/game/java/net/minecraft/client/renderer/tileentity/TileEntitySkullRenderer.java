package net.minecraft.client.renderer.tileentity;

import static net.lax1dude.eaglercraft.v1_8.opengl.RealOpenGLEnums.*;

import net.lax1dude.eaglercraft.v1_8.mojang.authlib.GameProfile;
import net.lax1dude.eaglercraft.v1_8.opengl.GlStateManager;
import net.minecraft.client.Minecraft;
import net.minecraft.client.model.ModelHumanoidHead;
import net.minecraft.client.model.ModelSkeletonHead;
import net.minecraft.client.network.NetHandlerPlayClient;
import net.minecraft.client.resources.DefaultPlayerSkin;
import net.minecraft.entity.Entity;
import net.minecraft.tileentity.TileEntitySkull;
import net.minecraft.util.EnumFacing;
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
public class TileEntitySkullRenderer extends TileEntitySpecialRenderer<TileEntitySkull> {
	private static final ResourceLocation SKELETON_TEXTURES = new ResourceLocation(
			"textures/entity/skeleton/skeleton.png");
	private static final ResourceLocation WITHER_SKELETON_TEXTURES = new ResourceLocation(
			"textures/entity/skeleton/wither_skeleton.png");
	private static final ResourceLocation ZOMBIE_TEXTURES = new ResourceLocation("textures/entity/zombie/zombie.png");
	private static final ResourceLocation CREEPER_TEXTURES = new ResourceLocation(
			"textures/entity/creeper/creeper.png");
	public static TileEntitySkullRenderer instance;
	private final ModelSkeletonHead skeletonHead = new ModelSkeletonHead(0, 0, 64, 32);
	private final ModelSkeletonHead humanoidHead = new ModelHumanoidHead();

	public void renderTileEntityAt(TileEntitySkull tileentityskull, double d0, double d1, double d2, float var8,
			int i) {
		EnumFacing enumfacing = EnumFacing.getFront(tileentityskull.getBlockMetadata() & 7);
		this.renderSkull((float) d0, (float) d1, (float) d2, enumfacing,
				(float) (tileentityskull.getSkullRotation() * 360) / 16.0F, tileentityskull.getSkullType(),
				tileentityskull.getPlayerProfile(), i);
	}

	public void setRendererDispatcher(TileEntityRendererDispatcher tileentityrendererdispatcher) {
		super.setRendererDispatcher(tileentityrendererdispatcher);
		instance = this;
	}

	public void renderSkull(float parFloat1, float parFloat2, float parFloat3, EnumFacing parEnumFacing,
			float parFloat4, int parInt1, GameProfile parGameProfile, int parInt2) {
		ModelSkeletonHead modelskeletonhead = this.skeletonHead;
		if (parInt2 >= 0) {
			this.bindTexture(DESTROY_STAGES[parInt2]);
			GlStateManager.matrixMode(GL_TEXTURE);
			GlStateManager.pushMatrix();
			GlStateManager.scale(4.0F, 2.0F, 1.0F);
			GlStateManager.translate(0.0625F, 0.0625F, 0.0625F);
			GlStateManager.matrixMode(GL_MODELVIEW);
		} else {
			switch (parInt1) {
			case 0:
			default:
				this.bindTexture(SKELETON_TEXTURES);
				break;
			case 1:
				this.bindTexture(WITHER_SKELETON_TEXTURES);
				break;
			case 2:
				this.bindTexture(ZOMBIE_TEXTURES);
				modelskeletonhead = this.humanoidHead;
				break;
			case 3:
				modelskeletonhead = this.humanoidHead;
				ResourceLocation resourcelocation = DefaultPlayerSkin.getDefaultSkinLegacy();
				if (parGameProfile != null && parGameProfile.getId() != null) {
					NetHandlerPlayClient netHandler = Minecraft.getMinecraft().getNetHandler();
					if (netHandler != null) {
						resourcelocation = netHandler.getTextureCache().getPlayerSkin(parGameProfile).getLocation();
					}
				}
				this.bindTexture(resourcelocation);
				break;
			case 4:
				this.bindTexture(CREEPER_TEXTURES);
			}
		}

		GlStateManager.pushMatrix();
		GlStateManager.disableCull();
		if (parEnumFacing != EnumFacing.UP) {
			switch (parEnumFacing) {
			case NORTH:
				GlStateManager.translate(parFloat1 + 0.5F, parFloat2 + 0.25F, parFloat3 + 0.74F);
				break;
			case SOUTH:
				GlStateManager.translate(parFloat1 + 0.5F, parFloat2 + 0.25F, parFloat3 + 0.26F);
				parFloat4 = 180.0F;
				break;
			case WEST:
				GlStateManager.translate(parFloat1 + 0.74F, parFloat2 + 0.25F, parFloat3 + 0.5F);
				parFloat4 = 270.0F;
				break;
			case EAST:
			default:
				GlStateManager.translate(parFloat1 + 0.26F, parFloat2 + 0.25F, parFloat3 + 0.5F);
				parFloat4 = 90.0F;
			}
		} else {
			GlStateManager.translate(parFloat1 + 0.5F, parFloat2, parFloat3 + 0.5F);
		}

		float f = 0.0625F;
		GlStateManager.enableRescaleNormal();
		GlStateManager.scale(-1.0F, -1.0F, 1.0F);
		GlStateManager.enableAlpha();
		modelskeletonhead.render((Entity) null, 0.0F, 0.0F, 0.0F, parFloat4, 0.0F, f);
		GlStateManager.popMatrix();
		if (parInt2 >= 0) {
			GlStateManager.matrixMode(GL_TEXTURE);
			GlStateManager.popMatrix();
			GlStateManager.matrixMode(GL_MODELVIEW);
		}

	}
}