package net.minecraft.block;

import net.minecraft.block.material.MapColor;
import net.minecraft.block.material.Material;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.Entity;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumWorldBlockLayer;
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
public class BlockSlime extends BlockBreakable {
	public BlockSlime() {
		super(Material.clay, false, MapColor.grassColor);
		this.setCreativeTab(CreativeTabs.tabDecorations);
		this.slipperiness = 0.8F;
	}

	public EnumWorldBlockLayer getBlockLayer() {
		return EnumWorldBlockLayer.TRANSLUCENT;
	}

	/**+
	 * Block's chance to react to a living entity falling on it.
	 */
	public void onFallenUpon(World world, BlockPos blockpos, Entity entity, float f) {
		if (entity.isSneaking()) {
			super.onFallenUpon(world, blockpos, entity, f);
		} else {
			entity.fall(f, 0.0F);
		}

	}

	/**+
	 * Called when an Entity lands on this Block. This method *must*
	 * update motionY because the entity will not do that on its own
	 */
	public void onLanded(World world, Entity entity) {
		if (entity.isSneaking()) {
			super.onLanded(world, entity);
		} else if (entity.motionY < 0.0D) {
			entity.motionY = -entity.motionY;
		}

	}

	/**+
	 * Triggered whenever an entity collides with this block (enters
	 * into the block)
	 */
	public void onEntityCollidedWithBlock(World world, BlockPos blockpos, Entity entity) {
		if (Math.abs(entity.motionY) < 0.1D && !entity.isSneaking()) {
			double d0 = 0.4D + Math.abs(entity.motionY) * 0.2D;
			entity.motionX *= d0;
			entity.motionZ *= d0;
		}

		super.onEntityCollidedWithBlock(world, blockpos, entity);
	}
}