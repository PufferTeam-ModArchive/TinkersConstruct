package tconstruct.util;

import java.util.LinkedList;
import java.util.List;

import net.minecraft.block.Block;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.fluids.FluidStack;

import cofh.api.energy.IEnergyContainerItem;
import cpw.mods.fml.common.FMLLog;
import cpw.mods.fml.common.event.FMLInterModComms;
import tconstruct.TConstruct;
import tconstruct.library.TConstructRegistry;
import tconstruct.library.crafting.CastingRecipe;
import tconstruct.library.crafting.FluidType;
import tconstruct.library.crafting.Smeltery;
import tconstruct.library.tools.DynamicToolPart;
import tconstruct.library.util.IPattern;
import tconstruct.smeltery.TinkerSmeltery;
import tconstruct.tools.TinkerTools;

public final class IMCHandler {

    private IMCHandler() {}

    public static void processIMC(List<FMLInterModComms.IMCMessage> messages) {
        for (FMLInterModComms.IMCMessage message : messages) {
            String type = message.key;
            if (type == null || type.isEmpty()) continue;

            // process materials added from mods
            switch (type) {
                case "addPartCastingMaterial": {
                    if (!message.isNBTMessage()) {
                        logInvalidMessage(message);
                        continue;
                    }

                    NBTTagCompound tag = message.getNBTValue();

                    if (!checkRequiredTags("Casting", tag, "MaterialId", "FluidName")) continue;

                    if (!tag.hasKey("MaterialId")) {
                        FMLLog.bigWarning("Casting IMC: Not material ID for the result present");
                        continue;
                    }

                    int matID = tag.getInteger("MaterialId");
                    FluidStack liquid = FluidStack.loadFluidStackFromNBT(tag);
                    if (liquid == null) {
                        FMLLog.bigWarning("Casting IMC: No fluid found");
                        continue;
                    }

                    if (TConstructRegistry.getMaterial(matID) == null) {
                        FMLLog.bigWarning("Casting IMC: Unknown Material ID " + matID);
                        continue;
                    }

                    // we add the toolpart to all smeltery recipies that use iron and create a toolpart
                    List<CastingRecipe> newRecipies = new LinkedList<>();
                    for (CastingRecipe recipe : TConstructRegistry.getTableCasting().getCastingRecipes()) {
                        if (recipe.castingMetal.getFluid() != TinkerSmeltery.moltenIronFluid) continue;
                        if (recipe.cast == null || !(recipe.cast.getItem() instanceof IPattern)) continue;
                        if (!(recipe.getResult().getItem() instanceof DynamicToolPart)) // has to be dynamic toolpart to
                                                                                        // support automatic addition
                            continue;

                        newRecipies.add(recipe);
                    }

                    FluidType ft = FluidType.getFluidType(liquid.getFluid());
                    if (ft == null) {
                        ft = new FluidType(TinkerSmeltery.glueBlock, 0, 500, liquid.getFluid(), true);
                        FluidType.registerFluidType(liquid.getFluid().getName(), ft);
                    }

                    // has to be done separately so we have all checks and no concurrent modification exception
                    for (CastingRecipe recipe : newRecipies) {
                        ItemStack output = recipe.getResult().copy();
                        output.setItemDamage(matID);

                        FluidStack liquid2 = new FluidStack(liquid, recipe.castingMetal.amount);

                        // ok, this recipe creates a toolpart and uses iron for it. add a new one for the IMC stuff!
                        TConstructRegistry.getTableCasting()
                                .addCastingRecipe(output, liquid2, recipe.cast, recipe.consumeCast, recipe.coolTime);
                        // and make it melt!
                        Smeltery.addMelting(ft, output, 0, liquid2.amount);
                    }

                    TConstruct.logger
                            .debug("Casting IMC: Added fluid " + tag.getString("FluidName") + " to part casting");
                    break;
                }
                case "addSmelteryMelting": {
                    if (!message.isNBTMessage()) {
                        logInvalidMessage(message);
                        continue;
                    }
                    NBTTagCompound tag = message.getNBTValue();

                    if (!checkRequiredTags("Smeltery", tag, "FluidName", "Temperature", "Item", "Block")) continue;

                    FluidStack liquid = FluidStack.loadFluidStackFromNBT(tag);
                    if (liquid == null) {
                        FMLLog.bigWarning("Smeltery IMC: No fluid found");
                        continue;
                    }
                    if (liquid.amount <= 0) {
                        FMLLog.bigWarning("Smeltery IMC: Liquid has to have an amount greater than zero");
                        continue;
                    }

                    ItemStack item = ItemStack.loadItemStackFromNBT(tag.getCompoundTag("Item"));
                    ItemStack block = ItemStack.loadItemStackFromNBT(tag.getCompoundTag("Block"));
                    int temperature = tag.getInteger("Temperature");

                    if (FluidType.getFluidType(liquid.getFluid()) == null) FluidType.registerFluidType(
                            liquid.getFluid().getName(),
                            Block.getBlockFromItem(block.getItem()),
                            block.getItemDamage(),
                            temperature,
                            liquid.getFluid(),
                            false);

                    Smeltery.addMelting(
                            item,
                            Block.getBlockFromItem(block.getItem()),
                            block.getItemDamage(),
                            temperature,
                            liquid);
                    TConstruct.logger.debug(
                            "Smeltery IMC: Added melting: " + item
                                    .getDisplayName() + " to " + liquid.amount + "mb " + liquid.getLocalizedName());
                    break;
                }
                case "addSmelteryFuel": {
                    if (!message.isNBTMessage()) {
                        logInvalidMessage(message);
                        continue;
                    }
                    NBTTagCompound tag = message.getNBTValue();

                    if (!checkRequiredTags("Smeltery", tag, "FluidName", "Temperature", "Duration")) continue;

                    FluidStack liquid = FluidStack.loadFluidStackFromNBT(tag);
                    if (liquid == null) {
                        FMLLog.bigWarning("Smeltery IMC: No fluid found");
                        continue;
                    }

                    int temperature = tag.getInteger("Temperature");
                    int duration = tag.getInteger("Duration");

                    Smeltery.addSmelteryFuel(liquid.getFluid(), temperature, duration);

                    TConstruct.logger.debug(
                            "Smeltery IMC: Added fuel: " + liquid
                                    .getLocalizedName() + " (" + temperature + ", " + duration + ")");
                    break;
                }
                case "addFluxBattery":
                    if (!message.isItemStackMessage()) {
                        logInvalidMessage(message, "ItemStack");
                        continue;
                    }
                    ItemStack battery = message.getItemStackValue();
                    battery.stackSize = 1; // avoid getting a stack size of 0 or larger than 1

                    if (!(battery.getItem() instanceof IEnergyContainerItem)) {
                        FMLLog.bigWarning("Flux Battery IMC: ItemStack is no instance of IEnergyContainerItem");
                    }

                    if (TinkerTools.modFlux != null) {
                        TinkerTools.modFlux.batteries.add(battery);
                    }
                    break;
            }
        }
    }

    private static boolean checkRequiredTags(String prefix, NBTTagCompound tag, String... tags) {
        boolean ok = true;
        for (String t : tags) if (!tag.hasKey(t)) {
            FMLLog.bigWarning(String.format("%s IMC: Missing required NBT Tag %s", prefix, t));
            ok = false; // don't abort, report all missing tags
        }

        return ok;
    }

    private static void logInvalidMessage(FMLInterModComms.IMCMessage message) {
        logInvalidMessage(message, "NBT");
    }

    private static void logInvalidMessage(FMLInterModComms.IMCMessage message, String type) {
        FMLLog.bigWarning(
                String.format(
                        "Received invalid IMC '%s' from %s. Not a %s Message.",
                        message.key,
                        message.getSender(),
                        type));
    }

    // basically FMLLog.bigWarning
    public static void bigWarning(String format, Object... data) {
        StackTraceElement[] trace = Thread.currentThread().getStackTrace();
        TConstruct.logger.error("**********************************************************************");
        TConstruct.logger.error(String.format("* " + format, data));
        for (int i = 2; i < 8 && i < trace.length; i++) {
            TConstruct.logger.error(String.format("*  at %s%s", trace[i].toString(), i == 7 ? "..." : ""));
        }
        TConstruct.logger.error("**********************************************************************");
    }
}
