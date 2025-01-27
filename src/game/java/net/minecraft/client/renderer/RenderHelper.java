package net.minecraft.client.renderer;

import net.lax1dude.eaglercraft.v1_8.opengl.GlStateManager;
import net.lax1dude.eaglercraft.v1_8.opengl.ext.deferred.DeferredStateManager;
import net.minecraft.util.Vec3;

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
public class RenderHelper {
	private static final Vec3 LIGHT0_POS = (new Vec3(0.20000000298023224D, 1.0D, -0.699999988079071D)).normalize();
	private static final Vec3 LIGHT1_POS = (new Vec3(-0.20000000298023224D, 1.0D, 0.699999988079071D)).normalize();

	/**+
	 * Disables the OpenGL lighting properties enabled by
	 * enableStandardItemLighting
	 */
	public static void disableStandardItemLighting() {
		if (!DeferredStateManager.isInDeferredPass()) {
			GlStateManager.disableLighting();
			GlStateManager.disableMCLight(0);
			GlStateManager.disableMCLight(1);
			GlStateManager.disableColorMaterial();
		}
	}

	/**+
	 * Sets the OpenGL lighting properties to the values used when
	 * rendering blocks as items
	 */
	public static void enableStandardItemLighting() {
		if (!DeferredStateManager.isInDeferredPass()) {
			GlStateManager.enableLighting();
			GlStateManager.enableMCLight(0, 0.6f, LIGHT0_POS.xCoord, LIGHT0_POS.yCoord, LIGHT0_POS.zCoord, 0.0D);
			GlStateManager.enableMCLight(1, 0.6f, LIGHT1_POS.xCoord, LIGHT1_POS.yCoord, LIGHT1_POS.zCoord, 0.0D);
			GlStateManager.setMCLightAmbient(0.4f, 0.4f, 0.4f);
			GlStateManager.enableColorMaterial();
		}
	}

	/**+
	 * Sets OpenGL lighting for rendering blocks as items inside GUI
	 * screens (such as containers).
	 */
	public static void enableGUIStandardItemLighting() {
		if (!DeferredStateManager.isInDeferredPass()) {
			GlStateManager.pushMatrix();
			GlStateManager.rotate(-30.0F, 0.0F, 1.0F, 0.0F);
			GlStateManager.rotate(165.0F, 1.0F, 0.0F, 0.0F);
			enableStandardItemLighting();
			GlStateManager.popMatrix();
		}
	}
}