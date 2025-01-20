package net.minecraft.dispenser;

import net.minecraft.block.BlockDispenser;
import net.minecraft.entity.Entity;
import net.minecraft.entity.IProjectile;
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
public abstract class BehaviorProjectileDispense extends BehaviorDefaultDispenseItem {
	/**+
	 * Dispense the specified stack, play the dispense sound and
	 * spawn particles.
	 */
	public ItemStack dispenseStack(IBlockSource iblocksource, ItemStack itemstack) {
		World world = iblocksource.getWorld();
		IPosition iposition = BlockDispenser.getDispensePosition(iblocksource);
		EnumFacing enumfacing = BlockDispenser.getFacing(iblocksource.getBlockMetadata());
		IProjectile iprojectile = this.getProjectileEntity(world, iposition);
		iprojectile.setThrowableHeading((double) enumfacing.getFrontOffsetX(),
				(double) ((float) enumfacing.getFrontOffsetY() + 0.1F), (double) enumfacing.getFrontOffsetZ(),
				this.func_82500_b(), this.func_82498_a());
		world.spawnEntityInWorld((Entity) iprojectile);
		itemstack.splitStack(1);
		return itemstack;
	}

	/**+
	 * Play the dispense sound from the specified block.
	 */
	protected void playDispenseSound(IBlockSource iblocksource) {
		iblocksource.getWorld().playAuxSFX(1002, iblocksource.getBlockPos(), 0);
	}

	protected abstract IProjectile getProjectileEntity(World var1, IPosition var2);

	protected float func_82498_a() {
		return 6.0F;
	}

	protected float func_82500_b() {
		return 1.1F;
	}
}