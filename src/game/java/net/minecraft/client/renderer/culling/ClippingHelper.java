package net.minecraft.client.renderer.culling;

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
public class ClippingHelper {
	public float[][] frustum = new float[6][4];
	public float[] projectionMatrix = new float[16];
	public float[] modelviewMatrix = new float[16];
	public float[] clippingMatrix = new float[16];

	private double dot(float[] parArrayOfFloat, double parDouble1, double parDouble2, double parDouble3) {
		return (double) parArrayOfFloat[0] * parDouble1 + (double) parArrayOfFloat[1] * parDouble2
				+ (double) parArrayOfFloat[2] * parDouble3 + (double) parArrayOfFloat[3];
	}

	/**+
	 * Returns true if the box is inside all 6 clipping planes,
	 * otherwise returns false.
	 */
	public boolean isBoxInFrustum(double parDouble1, double parDouble2, double parDouble3, double parDouble4,
			double parDouble5, double parDouble6) {
		for (int i = 0; i < 6; ++i) {
			float[] afloat = this.frustum[i];
			if (this.dot(afloat, parDouble1, parDouble2, parDouble3) <= 0.0D
					&& this.dot(afloat, parDouble4, parDouble2, parDouble3) <= 0.0D
					&& this.dot(afloat, parDouble1, parDouble5, parDouble3) <= 0.0D
					&& this.dot(afloat, parDouble4, parDouble5, parDouble3) <= 0.0D
					&& this.dot(afloat, parDouble1, parDouble2, parDouble6) <= 0.0D
					&& this.dot(afloat, parDouble4, parDouble2, parDouble6) <= 0.0D
					&& this.dot(afloat, parDouble1, parDouble5, parDouble6) <= 0.0D
					&& this.dot(afloat, parDouble4, parDouble5, parDouble6) <= 0.0D) {
				return false;
			}
		}

		return true;
	}
}