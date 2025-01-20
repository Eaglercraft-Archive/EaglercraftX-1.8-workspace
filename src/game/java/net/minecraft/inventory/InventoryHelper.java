package net.minecraft.inventory;

import net.lax1dude.eaglercraft.v1_8.EaglercraftRandom;

import net.minecraft.entity.Entity;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.BlockPos;
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
public class InventoryHelper {
	private static final EaglercraftRandom RANDOM = new EaglercraftRandom();

	public static void dropInventoryItems(World worldIn, BlockPos pos, IInventory parIInventory) {
		func_180174_a(worldIn, (double) pos.getX(), (double) pos.getY(), (double) pos.getZ(), parIInventory);
	}

	public static void func_180176_a(World worldIn, Entity parEntity, IInventory parIInventory) {
		func_180174_a(worldIn, parEntity.posX, parEntity.posY, parEntity.posZ, parIInventory);
	}

	private static void func_180174_a(World worldIn, double x, double y, double z, IInventory parIInventory) {
		for (int i = 0; i < parIInventory.getSizeInventory(); ++i) {
			ItemStack itemstack = parIInventory.getStackInSlot(i);
			if (itemstack != null) {
				spawnItemStack(worldIn, x, y, z, itemstack);
			}
		}

	}

	private static void spawnItemStack(World worldIn, double x, double y, double z, ItemStack stack) {
		float f = RANDOM.nextFloat() * 0.8F + 0.1F;
		float f1 = RANDOM.nextFloat() * 0.8F + 0.1F;
		float f2 = RANDOM.nextFloat() * 0.8F + 0.1F;

		while (stack.stackSize > 0) {
			int i = RANDOM.nextInt(21) + 10;
			if (i > stack.stackSize) {
				i = stack.stackSize;
			}

			stack.stackSize -= i;
			EntityItem entityitem = new EntityItem(worldIn, x + (double) f, y + (double) f1, z + (double) f2,
					new ItemStack(stack.getItem(), i, stack.getMetadata()));
			if (stack.hasTagCompound()) {
				entityitem.getEntityItem().setTagCompound((NBTTagCompound) stack.getTagCompound().copy());
			}

			float f3 = 0.05F;
			entityitem.motionX = RANDOM.nextGaussian() * (double) f3;
			entityitem.motionY = RANDOM.nextGaussian() * (double) f3 + 0.20000000298023224D;
			entityitem.motionZ = RANDOM.nextGaussian() * (double) f3;
			worldIn.spawnEntityInWorld(entityitem);
		}

	}
}