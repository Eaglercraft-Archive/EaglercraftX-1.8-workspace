package net.minecraft.entity.item;

import java.util.ArrayList;

import com.google.common.collect.Lists;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityHanging;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
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
public class EntityPainting extends EntityHanging {
	public EntityPainting.EnumArt art;

	public EntityPainting(World worldIn) {
		super(worldIn);
	}

	public EntityPainting(World worldIn, BlockPos pos, EnumFacing facing) {
		super(worldIn, pos);
		ArrayList arraylist = Lists.newArrayList();

		EntityPainting.EnumArt[] types = EntityPainting.EnumArt._VALUES;
		for (int i = 0; i < types.length; ++i) {
			this.art = types[i];
			this.updateFacingWithBoundingBox(facing);
			if (this.onValidSurface()) {
				arraylist.add(types[i]);
			}
		}

		if (!arraylist.isEmpty()) {
			this.art = (EntityPainting.EnumArt) arraylist.get(this.rand.nextInt(arraylist.size()));
		}

		this.updateFacingWithBoundingBox(facing);
	}

	public EntityPainting(World worldIn, BlockPos pos, EnumFacing facing, String title) {
		this(worldIn, pos, facing);

		EntityPainting.EnumArt[] types = EntityPainting.EnumArt._VALUES;
		for (int i = 0; i < types.length; ++i) {
			EntityPainting.EnumArt entitypainting$enumart = types[i];
			if (entitypainting$enumart.title.equals(title)) {
				this.art = entitypainting$enumart;
				break;
			}
		}

		this.updateFacingWithBoundingBox(facing);
	}

	/**+
	 * (abstract) Protected helper method to write subclass entity
	 * data to NBT.
	 */
	public void writeEntityToNBT(NBTTagCompound nbttagcompound) {
		nbttagcompound.setString("Motive", this.art.title);
		super.writeEntityToNBT(nbttagcompound);
	}

	/**+
	 * (abstract) Protected helper method to read subclass entity
	 * data from NBT.
	 */
	public void readEntityFromNBT(NBTTagCompound nbttagcompound) {
		String s = nbttagcompound.getString("Motive");

		EntityPainting.EnumArt[] types = EntityPainting.EnumArt._VALUES;
		for (int i = 0; i < types.length; ++i) {
			EntityPainting.EnumArt entitypainting$enumart = types[i];
			if (entitypainting$enumart.title.equals(s)) {
				this.art = entitypainting$enumart;
			}
		}

		if (this.art == null) {
			this.art = EntityPainting.EnumArt.KEBAB;
		}

		super.readEntityFromNBT(nbttagcompound);
	}

	public int getWidthPixels() {
		return this.art.sizeX;
	}

	public int getHeightPixels() {
		return this.art.sizeY;
	}

	/**+
	 * Called when this entity is broken. Entity parameter may be
	 * null.
	 */
	public void onBroken(Entity entity) {
		if (this.worldObj.getGameRules().getBoolean("doEntityDrops")) {
			if (entity instanceof EntityPlayer) {
				EntityPlayer entityplayer = (EntityPlayer) entity;
				if (entityplayer.capabilities.isCreativeMode) {
					return;
				}
			}

			this.entityDropItem(new ItemStack(Items.painting), 0.0F);
		}
	}

	/**+
	 * Sets the location and Yaw/Pitch of an entity in the world
	 */
	public void setLocationAndAngles(double d0, double d1, double d2, float var7, float var8) {
		BlockPos blockpos = this.hangingPosition.add(d0 - this.posX, d1 - this.posY, d2 - this.posZ);
		this.setPosition((double) blockpos.getX(), (double) blockpos.getY(), (double) blockpos.getZ());
	}

	public void setPositionAndRotation2(double d0, double d1, double d2, float var7, float var8, int var9,
			boolean var10) {
		BlockPos blockpos = this.hangingPosition.add(d0 - this.posX, d1 - this.posY, d2 - this.posZ);
		this.setPosition((double) blockpos.getX(), (double) blockpos.getY(), (double) blockpos.getZ());
	}

	public static enum EnumArt {
		KEBAB("Kebab", 16, 16, 0, 0), AZTEC("Aztec", 16, 16, 16, 0), ALBAN("Alban", 16, 16, 32, 0),
		AZTEC_2("Aztec2", 16, 16, 48, 0), BOMB("Bomb", 16, 16, 64, 0), PLANT("Plant", 16, 16, 80, 0),
		WASTELAND("Wasteland", 16, 16, 96, 0), POOL("Pool", 32, 16, 0, 32), COURBET("Courbet", 32, 16, 32, 32),
		SEA("Sea", 32, 16, 64, 32), SUNSET("Sunset", 32, 16, 96, 32), CREEBET("Creebet", 32, 16, 128, 32),
		WANDERER("Wanderer", 16, 32, 0, 64), GRAHAM("Graham", 16, 32, 16, 64), MATCH("Match", 32, 32, 0, 128),
		BUST("Bust", 32, 32, 32, 128), STAGE("Stage", 32, 32, 64, 128), VOID("Void", 32, 32, 96, 128),
		SKULL_AND_ROSES("SkullAndRoses", 32, 32, 128, 128), WITHER("Wither", 32, 32, 160, 128),
		FIGHTERS("Fighters", 64, 32, 0, 96), POINTER("Pointer", 64, 64, 0, 192), PIGSCENE("Pigscene", 64, 64, 64, 192),
		BURNING_SKULL("BurningSkull", 64, 64, 128, 192), SKELETON("Skeleton", 64, 48, 192, 64),
		DONKEY_KONG("DonkeyKong", 64, 48, 192, 112);

		public static final EnumArt[] _VALUES = values();

		public static final int field_180001_A = "SkullAndRoses".length();
		public final String title;
		public final int sizeX;
		public final int sizeY;
		public final int offsetX;
		public final int offsetY;

		private EnumArt(String titleIn, int width, int height, int textureU, int textureV) {
			this.title = titleIn;
			this.sizeX = width;
			this.sizeY = height;
			this.offsetX = textureU;
			this.offsetY = textureV;
		}
	}
}