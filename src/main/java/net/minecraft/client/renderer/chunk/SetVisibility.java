package net.minecraft.client.renderer.chunk;

import java.util.BitSet;
import java.util.Set;

import net.minecraft.util.EnumFacing;

/**+
 * This portion of EaglercraftX contains deobfuscated Minecraft 1.8 source code.
 * 
 * Minecraft 1.8.8 bytecode is (c) 2015 Mojang AB. "Do not distribute!"
 * Mod Coder Pack v9.18 deobfuscation configs are (c) Copyright by the MCP Team
 * 
 * EaglercraftX 1.8 patch files (c) 2022-2024 lax1dude, ayunami2000. All Rights Reserved.
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
public class SetVisibility {
	private static final int COUNT_FACES = EnumFacing.values().length;
	private final BitSet bitSet;

	public SetVisibility() {
		this.bitSet = new BitSet(COUNT_FACES * COUNT_FACES);
	}

	public void setManyVisible(Set<EnumFacing> parSet) {
		for (EnumFacing enumfacing : parSet) {
			for (EnumFacing enumfacing1 : parSet) {
				this.setVisible(enumfacing, enumfacing1, true);
			}
		}

	}

	public void setVisible(EnumFacing facing, EnumFacing facing2, boolean parFlag) {
		this.bitSet.set(facing.ordinal() + facing2.ordinal() * COUNT_FACES, parFlag);
		this.bitSet.set(facing2.ordinal() + facing.ordinal() * COUNT_FACES, parFlag);
	}

	public void setAllVisible(boolean visible) {
		this.bitSet.set(0, this.bitSet.size(), visible);
	}

	public boolean isVisible(EnumFacing facing, EnumFacing facing2) {
		return this.bitSet.get(facing.ordinal() + facing2.ordinal() * COUNT_FACES);
	}

	public String toString() {
		StringBuilder stringbuilder = new StringBuilder();
		stringbuilder.append(' ');

		for (EnumFacing enumfacing : EnumFacing.values()) {
			stringbuilder.append(' ').append(enumfacing.toString().toUpperCase().charAt(0));
		}

		stringbuilder.append('\n');

		for (EnumFacing enumfacing2 : EnumFacing.values()) {
			stringbuilder.append(enumfacing2.toString().toUpperCase().charAt(0));

			for (EnumFacing enumfacing1 : EnumFacing.values()) {
				if (enumfacing2 == enumfacing1) {
					stringbuilder.append("  ");
				} else {
					boolean flag = this.isVisible(enumfacing2, enumfacing1);
					stringbuilder.append(' ').append((char) (flag ? 'Y' : 'n'));
				}
			}

			stringbuilder.append('\n');
		}

		return stringbuilder.toString();
	}
}