package net.minecraft.item;

import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.EntityHanging;
import net.minecraft.entity.item.EntityItemFrame;
import net.minecraft.entity.item.EntityPainting;
import net.minecraft.entity.player.EntityPlayer;
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
public class ItemHangingEntity extends Item {
	private final Class<? extends EntityHanging> hangingEntityClass;

	public ItemHangingEntity(Class<? extends EntityHanging> entityClass) {
		this.hangingEntityClass = entityClass;
		this.setCreativeTab(CreativeTabs.tabDecorations);
	}

	/**+
	 * Called when a Block is right-clicked with this Item
	 */
	public boolean onItemUse(ItemStack itemstack, EntityPlayer entityplayer, World world, BlockPos blockpos,
			EnumFacing enumfacing, float var6, float var7, float var8) {
		if (enumfacing == EnumFacing.DOWN) {
			return false;
		} else if (enumfacing == EnumFacing.UP) {
			return false;
		} else {
			BlockPos blockpos1 = blockpos.offset(enumfacing);
			if (!entityplayer.canPlayerEdit(blockpos1, enumfacing, itemstack)) {
				return false;
			} else {
				EntityHanging entityhanging = this.createEntity(world, blockpos1, enumfacing);
				if (entityhanging != null && entityhanging.onValidSurface()) {
					if (!world.isRemote) {
						world.spawnEntityInWorld(entityhanging);
					}
					--itemstack.stackSize;
				}

				return true;
			}
		}
	}

	private EntityHanging createEntity(World worldIn, BlockPos pos, EnumFacing clickedSide) {
		return (EntityHanging) (this.hangingEntityClass == EntityPainting.class
				? new EntityPainting(worldIn, pos, clickedSide)
				: (this.hangingEntityClass == EntityItemFrame.class ? new EntityItemFrame(worldIn, pos, clickedSide)
						: null));
	}
}