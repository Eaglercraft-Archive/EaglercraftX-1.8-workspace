package net.minecraft.world.gen.structure;

import java.util.Iterator;
import java.util.LinkedList;
import net.lax1dude.eaglercraft.v1_8.EaglercraftRandom;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.world.ChunkCoordIntPair;
import net.minecraft.world.World;

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
public abstract class StructureStart {
	/**+
	 * List of all StructureComponents that are part of this
	 * structure
	 */
	protected LinkedList<StructureComponent> components = new LinkedList();
	protected StructureBoundingBox boundingBox;
	private int chunkPosX;
	private int chunkPosZ;

	public StructureStart() {
	}

	public StructureStart(int chunkX, int chunkZ) {
		this.chunkPosX = chunkX;
		this.chunkPosZ = chunkZ;
	}

	public StructureBoundingBox getBoundingBox() {
		return this.boundingBox;
	}

	public LinkedList<StructureComponent> getComponents() {
		return this.components;
	}

	/**+
	 * Keeps iterating Structure Pieces and spawning them until the
	 * checks tell it to stop
	 */
	public void generateStructure(World world, EaglercraftRandom random, StructureBoundingBox structureboundingbox) {
		Iterator iterator = this.components.iterator();

		while (iterator.hasNext()) {
			StructureComponent structurecomponent = (StructureComponent) iterator.next();
			if (structurecomponent.getBoundingBox().intersectsWith(structureboundingbox)
					&& !structurecomponent.addComponentParts(world, random, structureboundingbox)) {
				iterator.remove();
			}
		}

	}

	/**+
	 * Calculates total bounding box based on components' bounding
	 * boxes and saves it to boundingBox
	 */
	protected void updateBoundingBox() {
		this.boundingBox = StructureBoundingBox.getNewBoundingBox();

		for (StructureComponent structurecomponent : this.components) {
			this.boundingBox.expandTo(structurecomponent.getBoundingBox());
		}

	}

	public NBTTagCompound writeStructureComponentsToNBT(int chunkX, int chunkZ) {
		NBTTagCompound nbttagcompound = new NBTTagCompound();
		nbttagcompound.setString("id", MapGenStructureIO.getStructureStartName(this));
		nbttagcompound.setInteger("ChunkX", chunkX);
		nbttagcompound.setInteger("ChunkZ", chunkZ);
		nbttagcompound.setTag("BB", this.boundingBox.toNBTTagIntArray());
		NBTTagList nbttaglist = new NBTTagList();

		for (StructureComponent structurecomponent : this.components) {
			nbttaglist.appendTag(structurecomponent.createStructureBaseNBT());
		}

		nbttagcompound.setTag("Children", nbttaglist);
		this.writeToNBT(nbttagcompound);
		return nbttagcompound;
	}

	public void writeToNBT(NBTTagCompound var1) {
	}

	public void readStructureComponentsFromNBT(World worldIn, NBTTagCompound tagCompound) {
		this.chunkPosX = tagCompound.getInteger("ChunkX");
		this.chunkPosZ = tagCompound.getInteger("ChunkZ");
		if (tagCompound.hasKey("BB")) {
			this.boundingBox = new StructureBoundingBox(tagCompound.getIntArray("BB"));
		}

		NBTTagList nbttaglist = tagCompound.getTagList("Children", 10);

		for (int i = 0; i < nbttaglist.tagCount(); ++i) {
			this.components.add(MapGenStructureIO.getStructureComponent(nbttaglist.getCompoundTagAt(i), worldIn));
		}

		this.readFromNBT(tagCompound);
	}

	public void readFromNBT(NBTTagCompound var1) {
	}

	/**+
	 * offsets the structure Bounding Boxes up to a certain height,
	 * typically 63 - 10
	 */
	protected void markAvailableHeight(World worldIn, EaglercraftRandom rand, int parInt1) {
		int i = worldIn.func_181545_F() - parInt1;
		int j = this.boundingBox.getYSize() + 1;
		if (j < i) {
			j += rand.nextInt(i - j);
		}

		int k = j - this.boundingBox.maxY;
		this.boundingBox.offset(0, k, 0);

		for (StructureComponent structurecomponent : this.components) {
			structurecomponent.func_181138_a(0, k, 0);
		}

	}

	protected void setRandomHeight(World worldIn, EaglercraftRandom rand, int parInt1, int parInt2) {
		int i = parInt2 - parInt1 + 1 - this.boundingBox.getYSize();
		int j = 1;
		if (i > 1) {
			j = parInt1 + rand.nextInt(i);
		} else {
			j = parInt1;
		}

		int k = j - this.boundingBox.minY;
		this.boundingBox.offset(0, k, 0);

		for (StructureComponent structurecomponent : this.components) {
			structurecomponent.func_181138_a(0, k, 0);
		}

	}

	/**+
	 * currently only defined for Villages, returns true if Village
	 * has more than 2 non-road components
	 */
	public boolean isSizeableStructure() {
		return true;
	}

	public boolean func_175788_a(ChunkCoordIntPair var1) {
		return true;
	}

	public void func_175787_b(ChunkCoordIntPair var1) {
	}

	public int getChunkPosX() {
		return this.chunkPosX;
	}

	public int getChunkPosZ() {
		return this.chunkPosZ;
	}
}