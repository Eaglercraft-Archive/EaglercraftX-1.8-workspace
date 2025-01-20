package net.minecraft.client.model;

import net.lax1dude.eaglercraft.v1_8.opengl.WorldRenderer;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
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
public class TexturedQuad {
	public PositionTextureVertex[] vertexPositions;
	public int nVertices;
	private boolean invertNormal;

	public TexturedQuad(PositionTextureVertex[] vertices) {
		this.vertexPositions = vertices;
		this.nVertices = vertices.length;
	}

	public TexturedQuad(PositionTextureVertex[] vertices, int texcoordU1, int texcoordV1, int texcoordU2,
			int texcoordV2, float textureWidth, float textureHeight) {
		this(vertices);
		float f = 0.0F / textureWidth;
		float f1 = 0.0F / textureHeight;
		vertices[0] = vertices[0].setTexturePosition((float) texcoordU2 / textureWidth - f,
				(float) texcoordV1 / textureHeight + f1);
		vertices[1] = vertices[1].setTexturePosition((float) texcoordU1 / textureWidth + f,
				(float) texcoordV1 / textureHeight + f1);
		vertices[2] = vertices[2].setTexturePosition((float) texcoordU1 / textureWidth + f,
				(float) texcoordV2 / textureHeight - f1);
		vertices[3] = vertices[3].setTexturePosition((float) texcoordU2 / textureWidth - f,
				(float) texcoordV2 / textureHeight - f1);
	}

	public void flipFace() {
		PositionTextureVertex[] apositiontexturevertex = new PositionTextureVertex[this.vertexPositions.length];

		for (int i = 0; i < this.vertexPositions.length; ++i) {
			apositiontexturevertex[i] = this.vertexPositions[this.vertexPositions.length - i - 1];
		}

		this.vertexPositions = apositiontexturevertex;
	}

	/**+
	 * Draw this primitve. This is typically called only once as the
	 * generated drawing instructions are saved by the renderer and
	 * reused later.
	 */
	public void draw(WorldRenderer renderer, float scale) {
		Vec3 vec3 = this.vertexPositions[1].vector3D.subtractReverse(this.vertexPositions[0].vector3D);
		Vec3 vec31 = this.vertexPositions[1].vector3D.subtractReverse(this.vertexPositions[2].vector3D);
		Vec3 vec32 = vec31.crossProduct(vec3).normalize();
		float f = (float) vec32.xCoord;
		float f1 = (float) vec32.yCoord;
		float f2 = (float) vec32.zCoord;
		if (this.invertNormal) {
			f = -f;
			f1 = -f1;
			f2 = -f2;
		}

		renderer.begin(7, DefaultVertexFormats.OLDMODEL_POSITION_TEX_NORMAL);

		for (int i = 0; i < 4; ++i) {
			PositionTextureVertex positiontexturevertex = this.vertexPositions[i];
			renderer.pos(positiontexturevertex.vector3D.xCoord * (double) scale,
					positiontexturevertex.vector3D.yCoord * (double) scale,
					positiontexturevertex.vector3D.zCoord * (double) scale)
					.tex((double) positiontexturevertex.texturePositionX,
							(double) positiontexturevertex.texturePositionY)
					.normal(f, f1, f2).endVertex();
		}

		Tessellator.getInstance().draw();
	}
}