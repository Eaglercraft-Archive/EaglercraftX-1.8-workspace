package net.minecraft.client.renderer;

import net.lax1dude.eaglercraft.v1_8.internal.buffer.ByteBuffer;
import net.lax1dude.eaglercraft.v1_8.internal.buffer.FloatBuffer;
import net.lax1dude.eaglercraft.v1_8.internal.buffer.IntBuffer;

import net.lax1dude.eaglercraft.v1_8.EagRuntime;
import net.lax1dude.eaglercraft.v1_8.opengl.EaglercraftGPU;

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
public class GLAllocation {
	/**+
	 * Generates the specified number of display lists and returns
	 * the first index.
	 */
	public static int generateDisplayLists() {
		return EaglercraftGPU.glGenLists();
	}

	public static void deleteDisplayLists(int list) {
		EaglercraftGPU.glDeleteLists(list);
	}

	/**+
	 * Creates and returns a direct byte buffer with the specified
	 * capacity. Applies native ordering to speed up access.
	 */
	public static ByteBuffer createDirectByteBuffer(int capacity) {
		return EagRuntime.allocateByteBuffer(capacity);
	}

	/**+
	 * Creates and returns a direct int buffer with the specified
	 * capacity. Applies native ordering to speed up access.
	 */
	public static IntBuffer createDirectIntBuffer(int capacity) {
		return EagRuntime.allocateIntBuffer(capacity);
	}

	/**+
	 * Creates and returns a direct float buffer with the specified
	 * capacity. Applies native ordering to speed up access.
	 */
	public static FloatBuffer createDirectFloatBuffer(int capacity) {
		return EagRuntime.allocateFloatBuffer(capacity);
	}
}