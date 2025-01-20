package net.minecraft.entity.ai.attributes;

import net.lax1dude.eaglercraft.v1_8.EaglercraftUUID;

import org.apache.commons.lang3.Validate;

import net.lax1dude.eaglercraft.v1_8.ThreadLocalRandom;
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
public class AttributeModifier {
	private final double amount;
	private final int operation;
	private final String name;
	private final EaglercraftUUID id;
	private boolean isSaved;

	public AttributeModifier(String nameIn, double amountIn, int operationIn) {
		this(MathHelper.getRandomUuid(ThreadLocalRandom.current()), nameIn, amountIn, operationIn);
	}

	public AttributeModifier(EaglercraftUUID idIn, String nameIn, double amountIn, int operationIn) {
		this.isSaved = true;
		this.id = idIn;
		this.name = nameIn;
		this.amount = amountIn;
		this.operation = operationIn;
		Validate.notEmpty(nameIn, "Modifier name cannot be empty", new Object[0]);
		Validate.inclusiveBetween(0L, 2L, (long) operationIn, "Invalid operation");
	}

	public EaglercraftUUID getID() {
		return this.id;
	}

	public String getName() {
		return this.name;
	}

	public int getOperation() {
		return this.operation;
	}

	public double getAmount() {
		return this.amount;
	}

	/**+
	 * @see #isSaved
	 */
	public boolean isSaved() {
		return this.isSaved;
	}

	/**+
	 * @see #isSaved
	 */
	public AttributeModifier setSaved(boolean saved) {
		this.isSaved = saved;
		return this;
	}

	public boolean equals(Object object) {
		if (this == object) {
			return true;
		} else if (object != null && this.getClass() == object.getClass()) {
			AttributeModifier attributemodifier = (AttributeModifier) object;
			if (this.id != null) {
				if (!this.id.equals(attributemodifier.id)) {
					return false;
				}
			} else if (attributemodifier.id != null) {
				return false;
			}

			return true;
		} else {
			return false;
		}
	}

	public int hashCode() {
		return this.id != null ? this.id.hashCode() : 0;
	}

	public String toString() {
		return "AttributeModifier{amount=" + this.amount + ", operation=" + this.operation + ", name=\'" + this.name
				+ '\'' + ", id=" + this.id + ", serialize=" + this.isSaved + '}';
	}
}