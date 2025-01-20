package net.minecraft.entity.ai.attributes;

import net.minecraft.util.MathHelper;

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
public class RangedAttribute extends BaseAttribute {
	private final double minimumValue;
	private final double maximumValue;
	private String description;

	public RangedAttribute(IAttribute parIAttribute, String unlocalizedNameIn, double defaultValue,
			double minimumValueIn, double maximumValueIn) {
		super(parIAttribute, unlocalizedNameIn, defaultValue);
		this.minimumValue = minimumValueIn;
		this.maximumValue = maximumValueIn;
		if (minimumValueIn > maximumValueIn) {
			throw new IllegalArgumentException("Minimum value cannot be bigger than maximum value!");
		} else if (defaultValue < minimumValueIn) {
			throw new IllegalArgumentException("Default value cannot be lower than minimum value!");
		} else if (defaultValue > maximumValueIn) {
			throw new IllegalArgumentException("Default value cannot be bigger than maximum value!");
		}
	}

	public RangedAttribute setDescription(String descriptionIn) {
		this.description = descriptionIn;
		return this;
	}

	public String getDescription() {
		return this.description;
	}

	public double clampValue(double d0) {
		d0 = MathHelper.clamp_double(d0, this.minimumValue, this.maximumValue);
		return d0;
	}
}