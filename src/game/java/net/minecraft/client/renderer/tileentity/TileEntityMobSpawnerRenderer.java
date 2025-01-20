package net.minecraft.client.renderer.tileentity;

import net.lax1dude.eaglercraft.v1_8.opengl.GlStateManager;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.Entity;
import net.minecraft.tileentity.MobSpawnerBaseLogic;
import net.minecraft.tileentity.TileEntityMobSpawner;

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
public class TileEntityMobSpawnerRenderer extends TileEntitySpecialRenderer<TileEntityMobSpawner> {
	public void renderTileEntityAt(TileEntityMobSpawner tileentitymobspawner, double d0, double d1, double d2, float f,
			int var9) {
		GlStateManager.pushMatrix();
		GlStateManager.translate((float) d0 + 0.5F, (float) d1, (float) d2 + 0.5F);
		renderMob(tileentitymobspawner.getSpawnerBaseLogic(), d0, d1, d2, f);
		GlStateManager.popMatrix();
	}

	/**+
	 * Render the mob inside the mob spawner.
	 */
	public static void renderMob(MobSpawnerBaseLogic mobSpawnerLogic, double posX, double posY, double posZ,
			float partialTicks) {
		Entity entity = mobSpawnerLogic.func_180612_a(mobSpawnerLogic.getSpawnerWorld());
		if (entity != null) {
			float f = 0.4375F;
			GlStateManager.translate(0.0F, 0.4F, 0.0F);
			GlStateManager.rotate((float) (mobSpawnerLogic.getPrevMobRotation()
					+ (mobSpawnerLogic.getMobRotation() - mobSpawnerLogic.getPrevMobRotation()) * (double) partialTicks)
					* 10.0F, 0.0F, 1.0F, 0.0F);
			GlStateManager.rotate(-30.0F, 1.0F, 0.0F, 0.0F);
			GlStateManager.translate(0.0F, -0.4F, 0.0F);
			GlStateManager.scale(f, f, f);
			entity.setLocationAndAngles(posX, posY, posZ, 0.0F, 0.0F);
			Minecraft.getMinecraft().getRenderManager().renderEntityWithPosYaw(entity, 0.0D, 0.0D, 0.0D, 0.0F,
					partialTicks);
		}

	}
}