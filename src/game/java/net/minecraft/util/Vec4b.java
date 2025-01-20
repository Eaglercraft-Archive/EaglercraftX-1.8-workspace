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
public class Vec4b {
	private byte field_176117_a;
	private byte field_176115_b;
	private byte field_176116_c;
	private byte field_176114_d;

	public Vec4b(byte parByte1, byte parByte2, byte parByte3, byte parByte4) {
		this.field_176117_a = parByte1;
		this.field_176115_b = parByte2;
		this.field_176116_c = parByte3;
		this.field_176114_d = parByte4;
	}

	public Vec4b(Vec4b parVec4b) {
		this.field_176117_a = parVec4b.field_176117_a;
		this.field_176115_b = parVec4b.field_176115_b;
		this.field_176116_c = parVec4b.field_176116_c;
		this.field_176114_d = parVec4b.field_176114_d;
	}

	public byte func_176110_a() {
		return this.field_176117_a;
	}

	public byte func_176112_b() {
		return this.field_176115_b;
	}

	public byte func_176113_c() {
		return this.field_176116_c;
	}

	public byte func_176111_d() {
		return this.field_176114_d;
	}

	public boolean equals(Object object) {
		if (this == object) {
			return true;
		} else if (!(object instanceof Vec4b)) {
			return false;
		} else {
			Vec4b vec4b = (Vec4b) object;
			return this.field_176117_a != vec4b.field_176117_a ? false
					: (this.field_176114_d != vec4b.field_176114_d ? false
							: (this.field_176115_b != vec4b.field_176115_b ? false
									: this.field_176116_c == vec4b.field_176116_c));
		}
	}

	public int hashCode() {
		int i = this.field_176117_a;
		i = 31 * i + this.field_176115_b;
		i = 31 * i + this.field_176116_c;
		i = 31 * i + this.field_176114_d;
		return i;
	}
}