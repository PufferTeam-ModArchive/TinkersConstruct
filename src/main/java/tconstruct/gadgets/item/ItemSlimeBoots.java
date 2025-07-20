package tconstruct.gadgets.item;

import java.util.List;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.client.resources.I18n;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemArmor;
import net.minecraft.item.ItemStack;
import net.minecraft.util.DamageSource;
import net.minecraft.util.IIcon;
import net.minecraft.util.StatCollector;
import net.minecraft.util.Vec3;
import net.minecraft.world.World;
import net.minecraftforge.common.ISpecialArmor;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.util.EnumHelper;
import net.minecraftforge.event.entity.living.LivingFallEvent;

import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import tconstruct.library.SlimeBounceHandler;
import tconstruct.library.TConstructRegistry;
import tconstruct.library.armor.ArmorPart;
import tconstruct.tools.entity.FancyEntityItem;

public class ItemSlimeBoots extends ItemArmor implements ISpecialArmor {

    public static ArmorMaterial SLIME_MATERIAL = EnumHelper.addArmorMaterial("SLIME", 100, new int[] { 0, 0, 0, 0 }, 0);

    public final ArmorPart armorPart;
    protected final String textureFolder;
    protected final String textureName;

    public ItemSlimeBoots() {
        super(SLIME_MATERIAL, 0, 3);
        this.setCreativeTab(TConstructRegistry.gadgetsTab);
        this.setMaxStackSize(1);
        SLIME_MATERIAL.customCraftingMaterial = Items.slime_ball;
        armorPart = ArmorPart.Feet;
        textureFolder = "armor";
        textureName = "slime";
        MinecraftForge.EVENT_BUS.register(new EventHandler());
    }

    @Override
    public boolean isValidArmor(ItemStack stack, int armorType1, Entity entity) {
        // can be worn as boots
        return armorType1 == 3;
    }

    // equipping with right click
    @Override
    public ItemStack onItemRightClick(ItemStack itemStackIn, World worldIn, EntityPlayer playerIn) {
        int i = EntityLiving.getArmorPosition(itemStackIn) - 1;
        ItemStack itemstack = playerIn.getCurrentArmor(i);

        if (itemstack == null) {
            playerIn.setCurrentItemOrArmor(i + 1, itemStackIn.copy());
            itemStackIn.stackSize = 0;
        }

        return itemStackIn;
    }

    @Override
    public int getArmorDisplay(EntityPlayer player, ItemStack armor, int slot) {
        return 0;
    }

    @Override
    @SideOnly(Side.CLIENT)
    public String getArmorTexture(ItemStack stack, Entity entity, int slot, String type) {
        return "tinker:textures/armor/" + textureName + "_" + 1 + ".png";
    }

    @SideOnly(Side.CLIENT)
    protected IIcon[] modifiers;

    @Override
    @SideOnly(Side.CLIENT)
    public void registerIcons(IIconRegister iconRegister) {
        this.itemIcon = iconRegister.registerIcon("tinker:" + textureFolder + "/" + textureName + "_boots");
    }

    @Override
    @SideOnly(Side.CLIENT)
    public IIcon getIcon(ItemStack stack, int renderPass) {
        return itemIcon;
    }

    @Override
    @SideOnly(Side.CLIENT)
    public boolean requiresMultipleRenderPasses() {
        return false;
    }

    @Override
    @SideOnly(Side.CLIENT)
    public boolean hasEffect(ItemStack par1ItemStack) {
        return false;
    }

    // ISpecialArmor overrides
    @Override
    public ArmorProperties getProperties(EntityLivingBase player, ItemStack armor, DamageSource source, double damage,
            int slot) {
        return new ArmorProperties(0, 0.1D, 1);
    }

    @Override
    public void damageArmor(EntityLivingBase entity, ItemStack armor, DamageSource source, int damage, int slot) {
        armor.damageItem(1, entity);
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void getSubItems(Item par1, CreativeTabs par2CreativeTabs, List par3List) {
        par3List.add(new ItemStack(par1));
    }

    // Vanilla overrides
    @Override
    public boolean isItemTool(ItemStack par1ItemStack) {
        return false;
    }

    @Override
    public boolean isRepairable() {
        return true;
    }

    @Override
    public int getItemEnchantability() {
        return 0;
    }

    @Override
    public boolean isFull3D() {
        return true;
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void addInformation(ItemStack stack, EntityPlayer player, List list, boolean par4) {
        list.add(StatCollector.translateToLocal("gadgets.slime_boots.tooltip1"));
        list.add(
                StatCollector.translateToLocal("gadgets.slime_boots.tooltip2") + " "
                        + (I18n.format(Minecraft.getMinecraft().gameSettings.keyBindSneak.getKeyDescription()))
                        + StatCollector.translateToLocal("gadgets.slime_boots.tooltip3"));
    }

    /* Prevent armor from dying */
    @Override
    public boolean hasCustomEntity(ItemStack stack) {
        return true;
    }

    @Override
    public int getMaxDamage() {
        return 100;
    }

    @Override
    public Entity createEntity(World world, Entity location, ItemStack itemstack) {
        return new FancyEntityItem(world, location, itemstack);
    }

    public static class EventHandler {

        /** Called when an entity lands to handle the event */
        @SubscribeEvent
        // RUBBERY BOUNCY BOUNCERY WOOOOO
        public void onFall(LivingFallEvent event) {
            final EntityLivingBase living = event.entityLiving;
            // using fall distance as the event distance could be reduced by jump boost
            if (living == null || living.fallDistance <= 2f) {
                return;
            }
            // can the entity bounce?
            if (!SlimeBounceHandler.hasSlimeBoots(living)) {
                return;
            }

            // reduced fall damage when crouching
            if (living.isSneaking()) {
                event.distance = 1;
                return;
            }

            // thing is wearing slime boots. let's get bouncyyyyy
            event.setCanceled(true);
            // skip further client processing on players
            if (living.worldObj.isRemote) {
                living.playSound("mob.slime.small", 1f, 1f);
                SlimeBounceHandler.addBounceHandler(living);
                return;
            }

            // server players behave differently than non-server players, they have no
            // velocity during the event, so we need to reverse engineer it
            Vec3 motion = SlimeBounceHandler.getMotion(living);
            if (living instanceof EntityPlayerMP) {
                // velocity is lost on server players, but we don't have to defer the bounce
                double gravity = 0.2353455252;
                double time = Math.sqrt(living.fallDistance / gravity);
                double velocity = gravity * time / 2;
                living.motionX = motion.xCoord / 0.95f;
                living.motionY = velocity;
                living.motionZ = motion.zCoord / 0.95f;
                living.velocityChanged = true;
                // preserve momentum
                SlimeBounceHandler.addBounceHandler(living);
            } else {
                // for non-players, need to defer the bounce
                // only slow down half as much when bouncing
                living.motionX = motion.xCoord / 0.95f;
                living.motionY = motion.yCoord * -0.9;
                living.motionZ = motion.zCoord / 0.95f;
                SlimeBounceHandler.addBounceHandler(living, SlimeBounceHandler.getMotion(living).yCoord);
            }
            // update airborn status
            living.isAirBorne = true;
            living.onGround = false;
            event.distance = 0f;
            living.playSound("mob.slime.small", 1f, 1f);
        }

    }
}
