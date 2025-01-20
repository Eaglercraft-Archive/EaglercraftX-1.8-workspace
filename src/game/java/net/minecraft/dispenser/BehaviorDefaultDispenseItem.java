package net.minecraft.dispenser;

import net.minecraft.block.BlockDispenser;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.item.ItemStack;
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
public class BehaviorDefaultDispenseItem implements IBehaviorDispenseItem {
	/**+
	 * Dispenses the specified ItemStack from a dispenser.
	 */
	public final ItemStack dispense(IBlockSource source, ItemStack stack) {
		ItemStack itemstack = this.dispenseStack(source, stack);
		this.playDispenseSound(source);
		this.spawnDispenseParticles(source, BlockDispenser.getFacing(source.getBlockMetadata()));
		return itemstack;
	}

	/**+
	 * Dispense the specified stack, play the dispense sound and
	 * spawn particles.
	 */
	protected ItemStack dispenseStack(IBlockSource iblocksource, ItemStack itemstack) {
		EnumFacing enumfacing = BlockDispenser.getFacing(iblocksource.getBlockMetadata());
		IPosition iposition = BlockDispenser.getDispensePosition(iblocksource);
		ItemStack itemstack1 = itemstack.splitStack(1);
		doDispense(iblocksource.getWorld(), itemstack1, 6, enumfacing, iposition);
		return itemstack;
	}

	public static void doDispense(World worldIn, ItemStack stack, int speed, EnumFacing facing, IPosition position) {
		double d0 = position.getX();
		double d1 = position.getY();
		double d2 = position.getZ();
		if (facing.getAxis() == EnumFacing.Axis.Y) {
			d1 = d1 - 0.125D;
		} else {
			d1 = d1 - 0.15625D;
		}

		EntityItem entityitem = new EntityItem(worldIn, d0, d1, d2, stack);
		double d3 = worldIn.rand.nextDouble() * 0.1D + 0.2D;
		entityitem.motionX = (double) facing.getFrontOffsetX() * d3;
		entityitem.motionY = 0.20000000298023224D;
		entityitem.motionZ = (double) facing.getFrontOffsetZ() * d3;
		entityitem.motionX += worldIn.rand.nextGaussian() * 0.007499999832361937D * (double) speed;
		entityitem.motionY += worldIn.rand.nextGaussian() * 0.007499999832361937D * (double) speed;
		entityitem.motionZ += worldIn.rand.nextGaussian() * 0.007499999832361937D * (double) speed;
		worldIn.spawnEntityInWorld(entityitem);
	}

	/**+
	 * Play the dispense sound from the specified block.
	 */
	protected void playDispenseSound(IBlockSource iblocksource) {
		iblocksource.getWorld().playAuxSFX(1000, iblocksource.getBlockPos(), 0);
	}

	/**+
	 * Order clients to display dispense particles from the
	 * specified block and facing.
	 */
	protected void spawnDispenseParticles(IBlockSource source, EnumFacing facingIn) {
		source.getWorld().playAuxSFX(2000, source.getBlockPos(), this.func_82488_a(facingIn));
	}

	private int func_82488_a(EnumFacing facingIn) {
		return facingIn.getFrontOffsetX() + 1 + (facingIn.getFrontOffsetZ() + 1) * 3;
	}
}