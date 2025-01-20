package net.minecraft.util;

import net.minecraft.nbt.NBTTagFloat;
import net.minecraft.nbt.NBTTagList;

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
public class Rotations {
	protected final float x;
	protected final float y;
	protected final float z;

	public Rotations(float x, float y, float z) {
		this.x = x;
		this.y = y;
		this.z = z;
	}

	public Rotations(NBTTagList nbt) {
		this.x = nbt.getFloatAt(0);
		this.y = nbt.getFloatAt(1);
		this.z = nbt.getFloatAt(2);
	}

	public NBTTagList writeToNBT() {
		NBTTagList nbttaglist = new NBTTagList();
		nbttaglist.appendTag(new NBTTagFloat(this.x));
		nbttaglist.appendTag(new NBTTagFloat(this.y));
		nbttaglist.appendTag(new NBTTagFloat(this.z));
		return nbttaglist;
	}

	public boolean equals(Object object) {
		if (!(object instanceof Rotations)) {
			return false;
		} else {
			Rotations rotations = (Rotations) object;
			return this.x == rotations.x && this.y == rotations.y && this.z == rotations.z;
		}
	}

	/**+
	 * Gets the X axis rotation
	 */
	public float getX() {
		return this.x;
	}

	/**+
	 * Gets the Y axis rotation
	 */
	public float getY() {
		return this.y;
	}

	/**+
	 * Gets the Z axis rotation
	 */
	public float getZ() {
		return this.z;
	}
}