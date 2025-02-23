package net.minecraft.client.renderer;

import net.lax1dude.eaglercraft.v1_8.opengl.RealOpenGLEnums;
import net.lax1dude.eaglercraft.v1_8.opengl.WorldRenderer;
import net.lax1dude.eaglercraft.v1_8.opengl.WorldVertexBufferUploader;

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
public class Tessellator {
	private WorldRenderer worldRenderer;

	public static final int GL_TRIANGLES = RealOpenGLEnums.GL_TRIANGLES;
	public static final int GL_TRIANGLE_STRIP = RealOpenGLEnums.GL_TRIANGLE_STRIP;
	public static final int GL_TRIANGLE_FAN = RealOpenGLEnums.GL_TRIANGLE_FAN;
	public static final int GL_QUADS = RealOpenGLEnums.GL_QUADS;
	public static final int GL_LINES = RealOpenGLEnums.GL_LINES;
	public static final int GL_LINE_STRIP = RealOpenGLEnums.GL_LINE_STRIP;
	public static final int GL_LINE_LOOP = RealOpenGLEnums.GL_LINE_LOOP;

	/**+
	 * The static instance of the Tessellator.
	 */
	private static final Tessellator instance = new Tessellator(2097152);

	public static Tessellator getInstance() {
		return instance;
	}

	public Tessellator(int bufferSize) {
		this.worldRenderer = new WorldRenderer(bufferSize);
	}

	/**+
	 * Draws the data set up in this tessellator and resets the
	 * state to prepare for new drawing.
	 */
	public void draw() {
		this.worldRenderer.finishDrawing();
		WorldVertexBufferUploader.func_181679_a(this.worldRenderer);
	}

	public void uploadDisplayList(int displayList) {
		this.worldRenderer.finishDrawing();
		WorldVertexBufferUploader.uploadDisplayList(displayList, this.worldRenderer);
	}

	public WorldRenderer getWorldRenderer() {
		return this.worldRenderer;
	}
}