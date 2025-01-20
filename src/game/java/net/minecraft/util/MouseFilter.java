package net.minecraft.util;

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
public class MouseFilter {
	private float field_76336_a;
	private float field_76334_b;
	private float field_76335_c;

	/**+
	 * Smooths mouse input
	 */
	public float smooth(float parFloat1, float parFloat2) {
		this.field_76336_a += parFloat1;
		parFloat1 = (this.field_76336_a - this.field_76334_b) * parFloat2;
		this.field_76335_c += (parFloat1 - this.field_76335_c) * 0.5F;
		if (parFloat1 > 0.0F && parFloat1 > this.field_76335_c || parFloat1 < 0.0F && parFloat1 < this.field_76335_c) {
			parFloat1 = this.field_76335_c;
		}

		this.field_76334_b += parFloat1;
		return parFloat1;
	}

	public void reset() {
		this.field_76336_a = 0.0F;
		this.field_76334_b = 0.0F;
		this.field_76335_c = 0.0F;
	}
}