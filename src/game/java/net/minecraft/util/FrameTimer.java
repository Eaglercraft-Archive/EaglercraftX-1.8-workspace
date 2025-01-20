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
public class FrameTimer {
	private final long[] field_181752_a = new long[240];
	private int field_181753_b;
	private int field_181754_c;
	private int field_181755_d;

	public void func_181747_a(long parLong1) {
		this.field_181752_a[this.field_181755_d] = parLong1;
		++this.field_181755_d;
		if (this.field_181755_d == 240) {
			this.field_181755_d = 0;
		}

		if (this.field_181754_c < 240) {
			this.field_181753_b = 0;
			++this.field_181754_c;
		} else {
			this.field_181753_b = this.func_181751_b(this.field_181755_d + 1);
		}

	}

	public int func_181748_a(long parLong1, int parInt1) {
		double d0 = (double) parLong1 / 1.6666666E7D;
		return (int) (d0 * (double) parInt1);
	}

	public int func_181749_a() {
		return this.field_181753_b;
	}

	public int func_181750_b() {
		return this.field_181755_d;
	}

	public int func_181751_b(int parInt1) {
		return parInt1 % 240;
	}

	public long[] func_181746_c() {
		return this.field_181752_a;
	}
}