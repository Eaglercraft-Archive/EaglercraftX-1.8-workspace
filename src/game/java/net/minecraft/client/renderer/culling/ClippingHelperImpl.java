package net.minecraft.client.renderer.culling;

import static net.lax1dude.eaglercraft.v1_8.opengl.RealOpenGLEnums.*;

import net.lax1dude.eaglercraft.v1_8.opengl.GlStateManager;
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
public class ClippingHelperImpl extends ClippingHelper {
	private static ClippingHelperImpl instance = new ClippingHelperImpl();

	/**+
	 * Initialises the ClippingHelper object then returns an
	 * instance of it.
	 */
	public static ClippingHelper getInstance() {
		instance.init();
		instance.destroy();
		return instance;
	}

	private void normalize(float[] parArrayOfFloat) {
		float f = MathHelper.sqrt_float(parArrayOfFloat[0] * parArrayOfFloat[0]
				+ parArrayOfFloat[1] * parArrayOfFloat[1] + parArrayOfFloat[2] * parArrayOfFloat[2]);
		parArrayOfFloat[0] /= f;
		parArrayOfFloat[1] /= f;
		parArrayOfFloat[2] /= f;
		parArrayOfFloat[3] /= f;
	}

	public void init() {
		float[] afloat = this.projectionMatrix;
		float[] afloat1 = this.modelviewMatrix;
		GlStateManager.getFloat(GL_PROJECTION_MATRIX, afloat);
		GlStateManager.getFloat(GL_MODELVIEW_MATRIX, afloat1);
		this.clippingMatrix[0] = afloat1[0] * afloat[0] + afloat1[1] * afloat[4] + afloat1[2] * afloat[8]
				+ afloat1[3] * afloat[12];
		this.clippingMatrix[1] = afloat1[0] * afloat[1] + afloat1[1] * afloat[5] + afloat1[2] * afloat[9]
				+ afloat1[3] * afloat[13];
		this.clippingMatrix[2] = afloat1[0] * afloat[2] + afloat1[1] * afloat[6] + afloat1[2] * afloat[10]
				+ afloat1[3] * afloat[14];
		this.clippingMatrix[3] = afloat1[0] * afloat[3] + afloat1[1] * afloat[7] + afloat1[2] * afloat[11]
				+ afloat1[3] * afloat[15];
		this.clippingMatrix[4] = afloat1[4] * afloat[0] + afloat1[5] * afloat[4] + afloat1[6] * afloat[8]
				+ afloat1[7] * afloat[12];
		this.clippingMatrix[5] = afloat1[4] * afloat[1] + afloat1[5] * afloat[5] + afloat1[6] * afloat[9]
				+ afloat1[7] * afloat[13];
		this.clippingMatrix[6] = afloat1[4] * afloat[2] + afloat1[5] * afloat[6] + afloat1[6] * afloat[10]
				+ afloat1[7] * afloat[14];
		this.clippingMatrix[7] = afloat1[4] * afloat[3] + afloat1[5] * afloat[7] + afloat1[6] * afloat[11]
				+ afloat1[7] * afloat[15];
		this.clippingMatrix[8] = afloat1[8] * afloat[0] + afloat1[9] * afloat[4] + afloat1[10] * afloat[8]
				+ afloat1[11] * afloat[12];
		this.clippingMatrix[9] = afloat1[8] * afloat[1] + afloat1[9] * afloat[5] + afloat1[10] * afloat[9]
				+ afloat1[11] * afloat[13];
		this.clippingMatrix[10] = afloat1[8] * afloat[2] + afloat1[9] * afloat[6] + afloat1[10] * afloat[10]
				+ afloat1[11] * afloat[14];
		this.clippingMatrix[11] = afloat1[8] * afloat[3] + afloat1[9] * afloat[7] + afloat1[10] * afloat[11]
				+ afloat1[11] * afloat[15];
		this.clippingMatrix[12] = afloat1[12] * afloat[0] + afloat1[13] * afloat[4] + afloat1[14] * afloat[8]
				+ afloat1[15] * afloat[12];
		this.clippingMatrix[13] = afloat1[12] * afloat[1] + afloat1[13] * afloat[5] + afloat1[14] * afloat[9]
				+ afloat1[15] * afloat[13];
		this.clippingMatrix[14] = afloat1[12] * afloat[2] + afloat1[13] * afloat[6] + afloat1[14] * afloat[10]
				+ afloat1[15] * afloat[14];
		this.clippingMatrix[15] = afloat1[12] * afloat[3] + afloat1[13] * afloat[7] + afloat1[14] * afloat[11]
				+ afloat1[15] * afloat[15];
		float[] afloat2 = this.frustum[0];
		afloat2[0] = this.clippingMatrix[3] - this.clippingMatrix[0];
		afloat2[1] = this.clippingMatrix[7] - this.clippingMatrix[4];
		afloat2[2] = this.clippingMatrix[11] - this.clippingMatrix[8];
		afloat2[3] = this.clippingMatrix[15] - this.clippingMatrix[12];
		this.normalize(afloat2);
		float[] afloat3 = this.frustum[1];
		afloat3[0] = this.clippingMatrix[3] + this.clippingMatrix[0];
		afloat3[1] = this.clippingMatrix[7] + this.clippingMatrix[4];
		afloat3[2] = this.clippingMatrix[11] + this.clippingMatrix[8];
		afloat3[3] = this.clippingMatrix[15] + this.clippingMatrix[12];
		this.normalize(afloat3);
		float[] afloat4 = this.frustum[2];
		afloat4[0] = this.clippingMatrix[3] + this.clippingMatrix[1];
		afloat4[1] = this.clippingMatrix[7] + this.clippingMatrix[5];
		afloat4[2] = this.clippingMatrix[11] + this.clippingMatrix[9];
		afloat4[3] = this.clippingMatrix[15] + this.clippingMatrix[13];
		this.normalize(afloat4);
		float[] afloat5 = this.frustum[3];
		afloat5[0] = this.clippingMatrix[3] - this.clippingMatrix[1];
		afloat5[1] = this.clippingMatrix[7] - this.clippingMatrix[5];
		afloat5[2] = this.clippingMatrix[11] - this.clippingMatrix[9];
		afloat5[3] = this.clippingMatrix[15] - this.clippingMatrix[13];
		this.normalize(afloat5);
		float[] afloat6 = this.frustum[4];
		afloat6[0] = this.clippingMatrix[3] - this.clippingMatrix[2];
		afloat6[1] = this.clippingMatrix[7] - this.clippingMatrix[6];
		afloat6[2] = this.clippingMatrix[11] - this.clippingMatrix[10];
		afloat6[3] = this.clippingMatrix[15] - this.clippingMatrix[14];
		this.normalize(afloat6);
		float[] afloat7 = this.frustum[5];
		afloat7[0] = this.clippingMatrix[3] + this.clippingMatrix[2];
		afloat7[1] = this.clippingMatrix[7] + this.clippingMatrix[6];
		afloat7[2] = this.clippingMatrix[11] + this.clippingMatrix[10];
		afloat7[3] = this.clippingMatrix[15] + this.clippingMatrix[14];
		this.normalize(afloat7);
	}

	public void destroy() {
	}

}