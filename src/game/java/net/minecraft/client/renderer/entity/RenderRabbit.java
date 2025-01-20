package net.minecraft.client.renderer.entity;

import net.minecraft.client.model.ModelBase;
import net.minecraft.entity.passive.EntityRabbit;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.ResourceLocation;

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
public class RenderRabbit extends RenderLiving<EntityRabbit> {
	private static final ResourceLocation BROWN = new ResourceLocation("textures/entity/rabbit/brown.png");
	private static final ResourceLocation WHITE = new ResourceLocation("textures/entity/rabbit/white.png");
	private static final ResourceLocation BLACK = new ResourceLocation("textures/entity/rabbit/black.png");
	private static final ResourceLocation GOLD = new ResourceLocation("textures/entity/rabbit/gold.png");
	private static final ResourceLocation SALT = new ResourceLocation("textures/entity/rabbit/salt.png");
	private static final ResourceLocation WHITE_SPLOTCHED = new ResourceLocation(
			"textures/entity/rabbit/white_splotched.png");
	private static final ResourceLocation TOAST = new ResourceLocation("textures/entity/rabbit/toast.png");
	private static final ResourceLocation CAERBANNOG = new ResourceLocation("textures/entity/rabbit/caerbannog.png");

	public RenderRabbit(RenderManager renderManagerIn, ModelBase modelBaseIn, float shadowSizeIn) {
		super(renderManagerIn, modelBaseIn, shadowSizeIn);
	}

	/**+
	 * Returns the location of an entity's texture. Doesn't seem to
	 * be called unless you call Render.bindEntityTexture.
	 */
	protected ResourceLocation getEntityTexture(EntityRabbit entityrabbit) {
		String s = EnumChatFormatting.getTextWithoutFormattingCodes(entityrabbit.getName());
		if (s != null && s.equals("Toast")) {
			return TOAST;
		} else {
			switch (entityrabbit.getRabbitType()) {
			case 0:
			default:
				return BROWN;
			case 1:
				return WHITE;
			case 2:
				return BLACK;
			case 3:
				return WHITE_SPLOTCHED;
			case 4:
				return GOLD;
			case 5:
				return SALT;
			case 99:
				return CAERBANNOG;
			}
		}
	}
}