package net.minecraft.entity;

import net.minecraft.block.material.Material;
import net.minecraft.entity.monster.IMob;
import net.minecraft.entity.passive.EntityAmbientCreature;
import net.minecraft.entity.passive.EntityAnimal;
import net.minecraft.entity.passive.EntityWaterMob;
import net.minecraft.entity.passive.IAnimals;

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
public enum EnumCreatureType {
	MONSTER(IMob.class, 70, Material.air, false, false), CREATURE(EntityAnimal.class, 10, Material.air, true, true),
	AMBIENT(EntityAmbientCreature.class, 15, Material.air, true, false),
	WATER_CREATURE(EntityWaterMob.class, 5, Material.water, true, false);

	public static final EnumCreatureType[] _VALUES = values();

	private final Class<? extends IAnimals> creatureClass;
	private final int maxNumberOfCreature;
	private final Material creatureMaterial;
	private final boolean isPeacefulCreature;
	private final boolean isAnimal;

	private EnumCreatureType(Class<? extends IAnimals> creatureClassIn, int maxNumberOfCreatureIn,
			Material creatureMaterialIn, boolean isPeacefulCreatureIn, boolean isAnimalIn) {
		this.creatureClass = creatureClassIn;
		this.maxNumberOfCreature = maxNumberOfCreatureIn;
		this.creatureMaterial = creatureMaterialIn;
		this.isPeacefulCreature = isPeacefulCreatureIn;
		this.isAnimal = isAnimalIn;
	}

	public Class<? extends IAnimals> getCreatureClass() {
		return this.creatureClass;
	}

	public int getMaxNumberOfCreature() {
		return this.maxNumberOfCreature;
	}

	/**+
	 * Gets whether or not this creature type is peaceful.
	 */
	public boolean getPeacefulCreature() {
		return this.isPeacefulCreature;
	}

	/**+
	 * Return whether this creature type is an animal.
	 */
	public boolean getAnimal() {
		return this.isAnimal;
	}
}