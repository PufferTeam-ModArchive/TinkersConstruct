package tconstruct.blocks.traps;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.IIcon;
import net.minecraft.world.Explosion;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import mantle.blocks.MantleBlock;
import tconstruct.library.TConstructRegistry;
import tconstruct.world.model.BarricadeRender;

public class BarricadeBlock extends MantleBlock {

    Block modelBlock;
    int modelMeta;
    String modelType;

    public BarricadeBlock(Block model, int meta, String type) {
        super(Material.wood);
        this.modelBlock = model;
        this.modelMeta = meta;
        this.modelType = type;
        setHardness(0.5F);
        setStepSound(Block.soundTypeWood);
        this.setCreativeTab(TConstructRegistry.blockTab);
    }

    @Override
    @SideOnly(Side.CLIENT)
    public IIcon getIcon(int side, int meta) {
        return modelBlock.getIcon(2, modelMeta);
    }

    @Override
    public String getItemIconName() {
        return "tinker:" + modelType + "_lumber";
    }

    @Override
    public void registerBlockIcons(IIconRegister par1IconRegister) {}

    @Override
    public boolean renderAsNormalBlock() {
        return false;
    }

    @Override
    public boolean isOpaqueCube() {
        return false;
    }

    @Override
    public int getRenderType() {
        return BarricadeRender.model;
    }

    @Override
    public AxisAlignedBB getCollisionBoundingBoxFromPool(World par1World, int x, int y, int z) {
        int meta = par1World.getBlockMetadata(x, y, z);
        int type = meta % 4;
        AxisAlignedBB colbox = AxisAlignedBB.getBoundingBox(x, y, z, x + 1, y + 1, z + 1);

        if (meta / 4 == 0) {
            // if (type >= 0)
            // TODO setRenderBounds
            colbox = AxisAlignedBB.getBoundingBox(x + 0.125F, y + 0.0F, z + 0.5F, x + 0.375F, y + 1.0F, z + 0.75F);

            if (type >= 1) {
                colbox = AxisAlignedBB.getBoundingBox(x + 0.125F, y + 0.0F, z + 0.5F, x + 0.875F, y + 1.0F, z + 0.75F);
            }

            if (type >= 2) {
                colbox = AxisAlignedBB.getBoundingBox(x + 0.0F, y + 0.0F, z + 0.25F, x + 1.0F, y + 1.0F, z + 0.75F);
            }
        }

        if (meta / 4 == 1) {
            // if (type >= 0)
            colbox = AxisAlignedBB.getBoundingBox(x + 0.25F, y + 0.0F, z + 0.125F, x + 0.5F, y + 1.0F, z + 0.375F);

            if (type >= 1) {
                colbox = AxisAlignedBB.getBoundingBox(x + 0.25F, y + 0.0F, z + 0.125F, x + 0.5F, y + 1.0F, z + 0.875F);
            }

            if (type >= 2) {
                colbox = AxisAlignedBB.getBoundingBox(x + 0.25F, y + 0.0F, z + 0.0F, x + 0.75F, y + 1.0F, z + 1.0F);
            }
        }

        if (meta / 4 == 2) {
            // if (type >= 0)
            colbox = AxisAlignedBB.getBoundingBox(x + 0.125F, y + 0.0F, z + 0.25F, x + 0.375F, y + 1.0F, z + 0.5F);

            if (type >= 1) {
                colbox = AxisAlignedBB.getBoundingBox(x + 0.125F, y + 0.0F, z + 0.25F, x + 0.875F, y + 1.0F, z + 0.5F);
            }

            if (type >= 2) {
                colbox = AxisAlignedBB.getBoundingBox(x + 0.0F, y + 0.0F, z + 0.25F, x + 1.0F, y + 1.0F, z + 0.75F);
            }
        }

        if (meta / 4 == 3) {
            // if (type >= 0)
            colbox = AxisAlignedBB.getBoundingBox(x + 0.5F, y + 0.0F, z + 0.125F, x + 0.75F, y + 1.0F, z + 0.375F);

            if (type >= 1) {
                colbox = AxisAlignedBB.getBoundingBox(x + 0.5F, y + 0.0F, z + 0.125F, x + 0.75F, y + 1.0F, z + 0.875F);
            }

            if (type >= 2) {
                colbox = AxisAlignedBB.getBoundingBox(x + 0.25F, y + 0.0F, z + 0.0F, x + 0.75F, y + 1.0F, z + 1.0F);
            }
        }

        return colbox;
    }

    @Override
    public void setBlockBoundsBasedOnState(IBlockAccess world, int x, int y, int z) {
        int meta = world.getBlockMetadata(x, y, z);
        int type = meta % 4;

        if (meta / 4 == 0) {
            // if (type >= 0)
            // TODO setRenderBounds
            setBlockBounds(0.125F, 0.0F, 0.5F, 0.375F, 1.0F, 0.75F);

            if (type >= 1) {
                setBlockBounds(0.125F, 0.0F, 0.5F, 0.875F, 1.0F, 0.75F);
            }

            if (type >= 2) {
                setBlockBounds(0.0F, 0.0F, 0.25F, 1.0F, 1.0F, 0.75F);
            }
        }

        if (meta / 4 == 1) {
            // if (type >= 0)
            setBlockBounds(0.25F, 0.0F, 0.125F, 0.5F, 1.0F, 0.375F);

            if (type >= 1) {
                setBlockBounds(0.25F, 0.0F, 0.125F, 0.5F, 1.0F, 0.875F);
            }

            if (type >= 2) {
                setBlockBounds(0.25F, 0.0F, 0.0F, 0.75F, 1.0F, 1.0F);
            }
        }

        if (meta / 4 == 2) {
            // if (type >= 0)
            setBlockBounds(0.125F, 0.0F, 0.25F, 0.375F, 1.0F, 0.5F);

            if (type >= 1) {
                setBlockBounds(0.125F, 0.0F, 0.25F, 0.875F, 1.0F, 0.5F);
            }

            if (type >= 2) {
                setBlockBounds(0.0F, 0.0F, 0.25F, 1.0F, 1.0F, 0.75F);
            }
        }

        if (meta / 4 == 3) {
            // if (type >= 0)
            setBlockBounds(0.5F, 0.0F, 0.125F, 0.75F, 1.0F, 0.375F);

            if (type >= 1) {
                setBlockBounds(0.5F, 0.0F, 0.125F, 0.75F, 1.0F, 0.875F);
            }

            if (type >= 2) {
                setBlockBounds(0.25F, 0.0F, 0.0F, 0.75F, 1.0F, 1.0F);
            }
        }
    }

    @Override
    public void harvestBlock(World world, EntityPlayer player, int x, int y, int z, int meta) {
        if (meta % 4 > 0) {
            world.setBlock(x, y, z, this, meta - 1, 3);
        }
        dropBlockAsItem(world, x, y, z, new ItemStack(this));
    }

    @Override
    public boolean onBlockActivated(World world, int x, int y, int z, EntityPlayer player, int par6, float par7,
            float par8, float par9) {
        ItemStack stack = player.getCurrentEquippedItem();
        if ((stack != null) && (stack.getItem() == Item.getItemFromBlock(this)) && (!player.isSneaking())) {
            int meta = world.getBlockMetadata(x, y, z);
            if (meta % 4 != 3) {
                world.setBlock(x, y, z, this, meta + 1, 3);
                this.onBlockPlacedBy(world, x, y, z, player, stack);
                this.onPostBlockPlaced(world, x, y, z, meta);

                Block var9 = this;
                world.playSoundEffect(
                        x + 0.5F,
                        y + 0.5F,
                        z + 0.5F,
                        "dig.wood",
                        (var9.stepSound.getVolume() + 1.0F) / 2.0F,
                        var9.stepSound.getPitch() * 0.8F);
                player.swingItem();
                if (!player.capabilities.isCreativeMode) stack.stackSize -= 1;

                return true;
            }
        }
        return false;
    }

    @Override
    public void onBlockExploded(World world, int x, int y, int z, Explosion explosion) {
        double distance = (x - explosion.explosionX) + (y - explosion.explosionY) + (z - explosion.explosionZ);
        distance = Math.abs(distance);
        double power = (explosion.explosionSize * 2) / distance;
        int meta = world.getBlockMetadata(x, y, z);
        int trueMeta = meta % 4;
        trueMeta -= power;
        if (trueMeta < 0) world.setBlock(x, y, z, Blocks.air, 0, 0);
        else world.setBlockMetadataWithNotify(x, y, z, (int) (meta - power), 3);
        onBlockDestroyedByExplosion(world, x, y, z, explosion);
    }

    @Override
    @SideOnly(Side.CLIENT)
    public boolean shouldSideBeRendered(IBlockAccess world, int x, int y, int z, int side) {
        return true;
    }
}
