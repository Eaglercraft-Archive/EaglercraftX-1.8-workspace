package net.minecraft.client.audio;

import java.util.Map;

import com.google.common.collect.Maps;

import net.minecraft.util.RegistrySimple;
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
public class SoundRegistry extends RegistrySimple<ResourceLocation, SoundEventAccessorComposite> {
	private Map<ResourceLocation, SoundEventAccessorComposite> soundRegistry;

	protected Map<ResourceLocation, SoundEventAccessorComposite> createUnderlyingMap() {
		this.soundRegistry = Maps.newHashMap();
		return this.soundRegistry;
	}

	public void registerSound(SoundEventAccessorComposite parSoundEventAccessorComposite) {
		this.putObject(parSoundEventAccessorComposite.getSoundEventLocation(), parSoundEventAccessorComposite);
	}

	/**+
	 * Reset the underlying sound map (Called on resource manager
	 * reload)
	 */
	public void clearMap() {
		this.soundRegistry.clear();
	}
}