package net.minecraft.client.renderer.tileentity;

import net.lax1dude.eaglercraft.v1_8.opengl.GlStateManager;
import net.minecraft.client.model.ModelBook;
import net.minecraft.entity.Entity;
import net.minecraft.tileentity.TileEntityEnchantmentTable;
import net.minecraft.util.MathHelper;
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
public class TileEntityEnchantmentTableRenderer extends TileEntitySpecialRenderer<TileEntityEnchantmentTable> {
	/**+
	 * The texture for the book above the enchantment table.
	 */
	private static final ResourceLocation TEXTURE_BOOK = new ResourceLocation(
			"textures/entity/enchanting_table_book.png");
	private ModelBook field_147541_c = new ModelBook();

	public void renderTileEntityAt(TileEntityEnchantmentTable tileentityenchantmenttable, double d0, double d1,
			double d2, float f, int var9) {
		GlStateManager.pushMatrix();
		GlStateManager.translate((float) d0 + 0.5F, (float) d1 + 0.75F, (float) d2 + 0.5F);
		float f1 = (float) tileentityenchantmenttable.tickCount + f;
		GlStateManager.translate(0.0F, 0.1F + MathHelper.sin(f1 * 0.1F) * 0.01F, 0.0F);

		float f2;
		for (f2 = tileentityenchantmenttable.bookRotation
				- tileentityenchantmenttable.bookRotationPrev; f2 >= 3.1415927F; f2 -= 6.2831855F) {
			;
		}

		while (f2 < -3.1415927F) {
			f2 += 6.2831855F;
		}

		float f3 = tileentityenchantmenttable.bookRotationPrev + f2 * f;
		GlStateManager.rotate(-f3 * 180.0F / 3.1415927F, 0.0F, 1.0F, 0.0F);
		GlStateManager.rotate(80.0F, 0.0F, 0.0F, 1.0F);
		this.bindTexture(TEXTURE_BOOK);
		float f4 = tileentityenchantmenttable.pageFlipPrev
				+ (tileentityenchantmenttable.pageFlip - tileentityenchantmenttable.pageFlipPrev) * f + 0.25F;
		float f5 = tileentityenchantmenttable.pageFlipPrev
				+ (tileentityenchantmenttable.pageFlip - tileentityenchantmenttable.pageFlipPrev) * f + 0.75F;
		f4 = (f4 - (float) MathHelper.truncateDoubleToInt((double) f4)) * 1.6F - 0.3F;
		f5 = (f5 - (float) MathHelper.truncateDoubleToInt((double) f5)) * 1.6F - 0.3F;
		if (f4 < 0.0F) {
			f4 = 0.0F;
		}

		if (f5 < 0.0F) {
			f5 = 0.0F;
		}

		if (f4 > 1.0F) {
			f4 = 1.0F;
		}

		if (f5 > 1.0F) {
			f5 = 1.0F;
		}

		float f6 = tileentityenchantmenttable.bookSpreadPrev
				+ (tileentityenchantmenttable.bookSpread - tileentityenchantmenttable.bookSpreadPrev) * f;
		GlStateManager.enableCull();
		this.field_147541_c.render((Entity) null, f1, f4, f5, f6, 0.0F, 0.0625F);
		GlStateManager.popMatrix();
	}
}