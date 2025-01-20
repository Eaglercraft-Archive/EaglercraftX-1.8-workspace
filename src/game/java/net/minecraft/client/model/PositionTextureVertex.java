package net.minecraft.client.model;

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
public class PositionTextureVertex {
	public Vec3 vector3D;
	public float texturePositionX;
	public float texturePositionY;

	public PositionTextureVertex(float parFloat1, float parFloat2, float parFloat3, float parFloat4, float parFloat5) {
		this(new Vec3((double) parFloat1, (double) parFloat2, (double) parFloat3), parFloat4, parFloat5);
	}

	public PositionTextureVertex setTexturePosition(float parFloat1, float parFloat2) {
		return new PositionTextureVertex(this, parFloat1, parFloat2);
	}

	public PositionTextureVertex(PositionTextureVertex textureVertex, float texturePositionXIn,
			float texturePositionYIn) {
		this.vector3D = textureVertex.vector3D;
		this.texturePositionX = texturePositionXIn;
		this.texturePositionY = texturePositionYIn;
	}

	public PositionTextureVertex(Vec3 vector3DIn, float texturePositionXIn, float texturePositionYIn) {
		this.vector3D = vector3DIn;
		this.texturePositionX = texturePositionXIn;
		this.texturePositionY = texturePositionYIn;
	}
}