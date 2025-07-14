package tconstruct.world;

import net.minecraft.block.Block;
import net.minecraft.block.Block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.EnumCreatureType;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.FurnaceRecipes;
import net.minecraft.potion.Potion;
import net.minecraftforge.common.BiomeDictionary;
import net.minecraftforge.common.ChestGenHooks;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidRegistry;
import net.minecraftforge.oredict.OreDictionary;
import net.minecraftforge.oredict.ShapedOreRecipe;
import net.minecraftforge.oredict.ShapelessOreRecipe;

import cpw.mods.fml.common.Loader;
import cpw.mods.fml.common.Mod.Instance;
import cpw.mods.fml.common.SidedProxy;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLPostInitializationEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import cpw.mods.fml.common.registry.EntityRegistry;
import cpw.mods.fml.common.registry.GameRegistry;
import cpw.mods.fml.common.registry.GameRegistry.ObjectHolder;
import mantle.pulsar.pulse.Handler;
import mantle.pulsar.pulse.Pulse;
import mantle.utils.RecipeRemover;
import tconstruct.TConstruct;
import tconstruct.blocks.SlabBase;
import tconstruct.blocks.slime.SlimeFluid;
import tconstruct.blocks.slime.SlimeGel;
import tconstruct.blocks.slime.SlimeGrass;
import tconstruct.blocks.slime.SlimeLeaves;
import tconstruct.blocks.slime.SlimeSapling;
import tconstruct.blocks.slime.SlimeTallGrass;
import tconstruct.blocks.traps.BarricadeBlock;
import tconstruct.blocks.traps.Punji;
import tconstruct.client.StepSoundSlime;
import tconstruct.library.TConstructRegistry;
import tconstruct.library.crafting.FluidType;
import tconstruct.smeltery.blocks.MetalOre;
import tconstruct.tools.TinkerTools;
import tconstruct.tools.entity.ArrowEntity;
import tconstruct.tools.entity.DaggerEntity;
import tconstruct.tools.entity.FancyEntityItem;
import tconstruct.tools.entity.LaunchedPotion;
import tconstruct.util.config.PHConstruct;
import tconstruct.world.blocks.ConveyorBase;
import tconstruct.world.blocks.GravelOre;
import tconstruct.world.blocks.MeatBlock;
import tconstruct.world.blocks.OreberryBush;
import tconstruct.world.blocks.OreberryBushEssence;
import tconstruct.world.blocks.SlimeExplosive;
import tconstruct.world.blocks.SlimePad;
import tconstruct.world.blocks.TMetalBlock;
import tconstruct.world.blocks.WoodRail;
import tconstruct.world.entity.BlueSlime;
import tconstruct.world.entity.Crystal;
import tconstruct.world.entity.KingBlueSlime;
import tconstruct.world.gen.TBaseWorldGenerator;
import tconstruct.world.gen.TerrainGenEventHandler;
import tconstruct.world.itemblocks.BarricadeItem;
import tconstruct.world.itemblocks.HamboneItemBlock;
import tconstruct.world.itemblocks.OreberryBushItem;
import tconstruct.world.itemblocks.OreberryBushSecondItem;
import tconstruct.world.itemblocks.SlimeGelItemBlock;
import tconstruct.world.itemblocks.SlimeGrassItemBlock;
import tconstruct.world.itemblocks.SlimeLeavesItemBlock;
import tconstruct.world.itemblocks.SlimeSaplingItemBlock;
import tconstruct.world.itemblocks.SlimeTallGrassItem;
import tconstruct.world.itemblocks.WoolSlab1Item;
import tconstruct.world.itemblocks.WoolSlab2Item;
import tconstruct.world.items.GoldenHead;
import tconstruct.world.items.OreBerries;
import tconstruct.world.items.StrangeFood;

@ObjectHolder(TConstruct.modID)
@Pulse(id = "Tinkers' World", description = "Ores, slime islands, essence berries, and the like.", forced = true)
public class TinkerWorld {

    @Instance("TinkerWorld")
    public static TinkerWorld instance;

    @SidedProxy(
            clientSide = "tconstruct.world.TinkerWorldProxyClient",
            serverSide = "tconstruct.world.TinkerWorldProxyCommon")
    public static TinkerWorldProxyCommon proxy;

    public static Item strangeFood;
    // Decoration
    public static Block stoneTorch;
    public static Block stoneLadder;
    public static Block meatBlock;
    public static Block woolSlab1;
    public static Block woolSlab2;
    public static Block barricadeOak;
    public static Block barricadeSpruce;
    public static Block barricadeBirch;
    public static Block barricadeJungle;
    public static Block barricadeAcacia;
    public static Block barricadeDarkOak;
    public static Block barricadeSacredOak;
    public static Block barricadeCherry;
    public static Block barricadeDark;
    public static Block barricadeFir;
    public static Block barricadeEthereal;
    public static Block barricadeMagic;
    public static Block barricadeMangrove;
    public static Block barricadePalm;
    public static Block barricadeRedwood;
    public static Block barricadeWillow;
    public static Block barricadePine;
    public static Block barricadeHellbark;
    public static Block barricadeJacaranda;
    public static Block barricadeMahogany;
    public static Block barricadeGreatwood;
    public static Block barricadeSilverwood;
    public static Block slimeExplosive;
    public static Fluid blueSlimeFluid;
    // Slime
    public static SoundType slimeStep;
    public static Block slimePool;
    public static Block slimeGel;
    public static Block slimeGrass;
    public static Block slimeTallGrass;
    public static SlimeLeaves slimeLeaves;
    public static SlimeSapling slimeSapling;
    public static Block slimeChannel;
    public static Block slimePad;
    public static Block bloodChannel;
    // Ores
    public static Block oreSlag;
    public static Block oreGravel;
    public static OreberryBush oreBerry;
    public static OreberryBush oreBerrySecond;
    public static Item oreBerries;
    // Rail-related
    public static Block woodenRail;
    // Chest hooks
    public static ChestGenHooks tinkerHouseChest;
    public static ChestGenHooks tinkerHousePatterns;
    public static Block punji;
    public static Block metalBlock;
    // Morbid
    public static Item goldHead;

    @Handler
    public void preInit(FMLPreInitializationEvent event) {
        MinecraftForge.EVENT_BUS.register(new TinkerWorldEvents());

        // Blocks
        TinkerWorld.woolSlab1 = new SlabBase(Material.cloth, Blocks.wool, 0, 8).setBlockName("cloth");
        TinkerWorld.woolSlab1.setStepSound(Block.soundTypeCloth).setCreativeTab(CreativeTabs.tabDecorations);
        TinkerWorld.woolSlab2 = new SlabBase(Material.cloth, Blocks.wool, 8, 8).setBlockName("cloth");
        TinkerWorld.woolSlab2.setStepSound(Block.soundTypeCloth).setCreativeTab(CreativeTabs.tabDecorations);
        // Traps
        TinkerWorld.punji = new Punji().setBlockName("trap.punji");
        TinkerWorld.barricadeOak = new BarricadeBlock(Blocks.planks, 0, "oak").setBlockName("oak_lumber");
        TinkerWorld.barricadeSpruce = new BarricadeBlock(Blocks.planks, 1, "spruce").setBlockName("spruce_lumber");
        TinkerWorld.barricadeBirch = new BarricadeBlock(Blocks.planks, 2, "birch").setBlockName("birch_lumber");
        TinkerWorld.barricadeJungle = new BarricadeBlock(Blocks.planks, 3, "jungle").setBlockName("jungle_lumber");
        TinkerWorld.barricadeAcacia = new BarricadeBlock(Blocks.planks, 4, "acacia").setBlockName("acacia_lumber");
        TinkerWorld.barricadeDarkOak = new BarricadeBlock(Blocks.planks, 5, "dark_oak").setBlockName("dark_oak_lumber");
        TinkerWorld.barricadeSacredOak = new BarricadeBlock(
                GameRegistry.findBlock("BiomesOPlenty", "planks"),
                0,
                "sacredoak").setBlockName("sacredoak_lumber");
        TinkerWorld.barricadeCherry = new BarricadeBlock(GameRegistry.findBlock("BiomesOPlenty", "planks"), 1, "cherry")
                .setBlockName("cherry_lumber");
        TinkerWorld.barricadeDark = new BarricadeBlock(GameRegistry.findBlock("BiomesOPlenty", "planks"), 2, "dark")
                .setBlockName("dark_lumber");
        TinkerWorld.barricadeFir = new BarricadeBlock(GameRegistry.findBlock("BiomesOPlenty", "planks"), 3, "fir")
                .setBlockName("fir_lumber");
        TinkerWorld.barricadeEthereal = new BarricadeBlock(
                GameRegistry.findBlock("BiomesOPlenty", "planks"),
                4,
                "ethereal").setBlockName("ethereal_lumber");
        TinkerWorld.barricadeMagic = new BarricadeBlock(GameRegistry.findBlock("BiomesOPlenty", "planks"), 5, "magic")
                .setBlockName("magic_lumber");
        TinkerWorld.barricadeMangrove = new BarricadeBlock(
                GameRegistry.findBlock("BiomesOPlenty", "planks"),
                6,
                "mangrove").setBlockName("mangrove_lumber");
        TinkerWorld.barricadePalm = new BarricadeBlock(GameRegistry.findBlock("BiomesOPlenty", "planks"), 7, "palm")
                .setBlockName("palm_lumber");
        TinkerWorld.barricadeRedwood = new BarricadeBlock(
                GameRegistry.findBlock("BiomesOPlenty", "planks"),
                8,
                "redwood").setBlockName("redwood_lumber");
        TinkerWorld.barricadeWillow = new BarricadeBlock(GameRegistry.findBlock("BiomesOPlenty", "planks"), 9, "willow")
                .setBlockName("willow_lumber");
        TinkerWorld.barricadePine = new BarricadeBlock(GameRegistry.findBlock("BiomesOPlenty", "planks"), 11, "pine")
                .setBlockName("pine_lumber");
        TinkerWorld.barricadeHellbark = new BarricadeBlock(
                GameRegistry.findBlock("BiomesOPlenty", "planks"),
                12,
                "hellbark").setBlockName("hellbark_lumber");
        TinkerWorld.barricadeJacaranda = new BarricadeBlock(
                GameRegistry.findBlock("BiomesOPlenty", "planks"),
                13,
                "jacaranda").setBlockName("jacaranda_lumber");
        TinkerWorld.barricadeMahogany = new BarricadeBlock(
                GameRegistry.findBlock("BiomesOPlenty", "planks"),
                14,
                "mahogany").setBlockName("mahogany_lumber");
        TinkerWorld.barricadeGreatwood = new BarricadeBlock(
                GameRegistry.findBlock("Thaumcraft", "blockWoodenDevice"),
                6,
                "greatwood").setBlockName("greatwood_lumber");
        TinkerWorld.barricadeSilverwood = new BarricadeBlock(
                GameRegistry.findBlock("Thaumcraft", "blockWoodenDevice"),
                7,
                "silverwood").setBlockName("silverwood_lumber");

        // Slime
        TinkerWorld.slimeStep = new StepSoundSlime("mob.slime", 1.0f, 1.0f);

        TinkerWorld.blueSlimeFluid = new Fluid("slime.blue");
        if (!FluidRegistry.registerFluid(TinkerWorld.blueSlimeFluid))
            TinkerWorld.blueSlimeFluid = FluidRegistry.getFluid("slime.blue");
        TinkerWorld.slimePool = new SlimeFluid(TinkerWorld.blueSlimeFluid, Material.water)
                .setCreativeTab(TConstructRegistry.blockTab).setStepSound(TinkerWorld.slimeStep)
                .setBlockName("liquid.slime");
        GameRegistry.registerBlock(TinkerWorld.slimePool, "liquid.slime");
        TinkerWorld.blueSlimeFluid.setBlock(TinkerWorld.slimePool);

        // Slime Islands
        TinkerWorld.slimeGel = new SlimeGel().setStepSound(TinkerWorld.slimeStep).setLightOpacity(0)
                .setBlockName("slime.gel");
        TinkerWorld.slimeGel.setHarvestLevel("axe", 0, 1);
        TinkerWorld.slimeGrass = new SlimeGrass().setStepSound(Block.soundTypeGrass).setLightOpacity(0)
                .setBlockName("slime.grass");
        TinkerWorld.slimeTallGrass = new SlimeTallGrass().setStepSound(Block.soundTypeGrass)
                .setBlockName("slime.grass.tall");
        TinkerWorld.slimeLeaves = (SlimeLeaves) new SlimeLeaves().setStepSound(TinkerWorld.slimeStep)
                .setBlockName("slime.leaves");
        TinkerWorld.slimeSapling = (SlimeSapling) new SlimeSapling().setStepSound(TinkerWorld.slimeStep)
                .setBlockName("slime.sapling");
        TinkerWorld.slimeChannel = new ConveyorBase(Material.water, "greencurrent").setHardness(0.3f)
                .setStepSound(TinkerWorld.slimeStep).setBlockName("slime.channel");
        TinkerWorld.bloodChannel = new ConveyorBase(Material.water, "liquid_cow").setHardness(0.3f)
                .setStepSound(TinkerWorld.slimeStep).setBlockName("blood.channel");
        TinkerWorld.slimePad = new SlimePad(Material.cloth).setStepSound(TinkerWorld.slimeStep).setHardness(0.3f)
                .setBlockName("slime.pad");

        // Ores
        String[] berryOres = new String[] { "berry_iron", "berry_gold", "berry_copper", "berry_tin", "berry_iron_ripe",
                "berry_gold_ripe", "berry_copper_ripe", "berry_tin_ripe" };
        TinkerWorld.oreBerry = (OreberryBush) new OreberryBush(
                berryOres,
                0,
                4,
                new String[] { "oreIron", "oreGold", "oreCopper", "oreTin" }).setBlockName("ore.berries.one");
        String[] berryOresTwo = new String[] { "berry_aluminum", "berry_essence", "", "", "berry_aluminum_ripe",
                "berry_essence_ripe", "", "" };
        TinkerWorld.oreBerrySecond = (OreberryBush) new OreberryBushEssence(
                berryOresTwo,
                4,
                2,
                new String[] { "oreAluminum", "oreSilver" }).setBlockName("ore.berries.two");

        // Rail
        if (!Loader.isModLoaded("dreamcraft")) {
            TinkerWorld.woodenRail = new WoodRail().setStepSound(Block.soundTypeWood)
                    .setCreativeTab(TConstructRegistry.blockTab).setBlockName("rail.wood");
        }

        GameRegistry.registerBlock(TinkerWorld.woolSlab1, WoolSlab1Item.class, "WoolSlab1");
        GameRegistry.registerBlock(TinkerWorld.woolSlab2, WoolSlab2Item.class, "WoolSlab2");

        // Traps
        GameRegistry.registerBlock(TinkerWorld.punji, "trap.punji");
        GameRegistry.registerBlock(TinkerWorld.barricadeOak, BarricadeItem.class, "oak_lumber");
        GameRegistry.registerBlock(TinkerWorld.barricadeSpruce, BarricadeItem.class, "spruce_lumber");
        GameRegistry.registerBlock(TinkerWorld.barricadeBirch, BarricadeItem.class, "birch_lumber");
        GameRegistry.registerBlock(TinkerWorld.barricadeJungle, BarricadeItem.class, "jungle_lumber");
        GameRegistry.registerBlock(TinkerWorld.barricadeAcacia, BarricadeItem.class, "acacia_lumber");
        GameRegistry.registerBlock(TinkerWorld.barricadeDarkOak, BarricadeItem.class, "dark_oak_lumber");
        GameRegistry.registerBlock(TinkerWorld.barricadeSacredOak, BarricadeItem.class, "sacredoak_lumber");
        GameRegistry.registerBlock(TinkerWorld.barricadeCherry, BarricadeItem.class, "cherry_lumber");
        GameRegistry.registerBlock(TinkerWorld.barricadeDark, BarricadeItem.class, "dark_lumber");
        GameRegistry.registerBlock(TinkerWorld.barricadeFir, BarricadeItem.class, "fir_lumber");
        GameRegistry.registerBlock(TinkerWorld.barricadeEthereal, BarricadeItem.class, "ethereal_lumber");
        GameRegistry.registerBlock(TinkerWorld.barricadeMagic, BarricadeItem.class, "magic_lumber");
        GameRegistry.registerBlock(TinkerWorld.barricadeMangrove, BarricadeItem.class, "mangrove_lumber");
        GameRegistry.registerBlock(TinkerWorld.barricadePalm, BarricadeItem.class, "palm_lumber");
        GameRegistry.registerBlock(TinkerWorld.barricadeRedwood, BarricadeItem.class, "redwood_lumber");
        GameRegistry.registerBlock(TinkerWorld.barricadeWillow, BarricadeItem.class, "willow_lumber");
        GameRegistry.registerBlock(TinkerWorld.barricadePine, BarricadeItem.class, "pine_lumber");
        GameRegistry.registerBlock(TinkerWorld.barricadeHellbark, BarricadeItem.class, "hellbark_lumber");
        GameRegistry.registerBlock(TinkerWorld.barricadeJacaranda, BarricadeItem.class, "jacaranda_lumber");
        GameRegistry.registerBlock(TinkerWorld.barricadeMahogany, BarricadeItem.class, "mahogany_lumber");
        GameRegistry.registerBlock(TinkerWorld.barricadeGreatwood, BarricadeItem.class, "greatwood_lumber");
        GameRegistry.registerBlock(TinkerWorld.barricadeSilverwood, BarricadeItem.class, "silverwood_lumber");

        // fluids

        // Slime Islands
        GameRegistry.registerBlock(TinkerWorld.slimeGel, SlimeGelItemBlock.class, "slime.gel");
        GameRegistry.registerBlock(TinkerWorld.slimeGrass, SlimeGrassItemBlock.class, "slime.grass");
        GameRegistry.registerBlock(TinkerWorld.slimeTallGrass, SlimeTallGrassItem.class, "slime.grass.tall");
        GameRegistry.registerBlock(TinkerWorld.slimeLeaves, SlimeLeavesItemBlock.class, "slime.leaves");
        GameRegistry.registerBlock(TinkerWorld.slimeSapling, SlimeSaplingItemBlock.class, "slime.sapling");
        GameRegistry.registerBlock(TinkerWorld.slimeChannel, "slime.channel");
        GameRegistry.registerBlock(TinkerWorld.bloodChannel, "blood.channel");
        GameRegistry.registerBlock(TinkerWorld.slimePad, "slime.pad");
        // TODO fix this
        /*
         * TConstructRegistry.drawbridgeState[TRepo.slimePad] = 1;
         * TConstructRegistry.drawbridgeState[TRepo.bloodChannel] = 1;
         */

        // Ores
        GameRegistry.registerBlock(TinkerWorld.oreBerry, OreberryBushItem.class, "ore.berries.one");
        GameRegistry.registerBlock(TinkerWorld.oreBerrySecond, OreberryBushSecondItem.class, "ore.berries.two");

        // Rail
        if (TinkerWorld.woodenRail != null) {
            GameRegistry.registerBlock(TinkerWorld.woodenRail, "rail.wood");
        }

        TinkerWorld.strangeFood = new StrangeFood().setUnlocalizedName("tconstruct.strangefood");
        TinkerWorld.oreBerries = new OreBerries().setUnlocalizedName("oreberry");
        GameRegistry.registerItem(TinkerWorld.strangeFood, "strangeFood");
        GameRegistry.registerItem(TinkerWorld.oreBerries, "oreBerries");
        String[] oreberries = { "Iron", "Gold", "Copper", "Tin", "Aluminum", "Essence" };

        for (int i = 0; i < oreberries.length; i++) {
            TConstructRegistry
                    .addItemStackToDirectory("oreberry" + oreberries[i], new ItemStack(TinkerWorld.oreBerries, 1, i));
        }
        TConstructRegistry.addItemStackToDirectory("blueSlimeFood", new ItemStack(TinkerWorld.strangeFood, 1, 0));

        FluidType.registerFluidType("Slime", TinkerWorld.slimeGel, 0, 250, TinkerWorld.blueSlimeFluid, false);

        oreRegistry();
    }

    @Handler
    public void init(FMLInitializationEvent event) {
        if (!PHConstruct.disableAllRecipes) {
            craftingTableRecipes();
            addRecipesForFurnace();
        }
        addLoot();
        createEntities();
        proxy.initialize();

        GameRegistry.registerWorldGenerator(new TBaseWorldGenerator(), 0);
        MinecraftForge.TERRAIN_GEN_BUS.register(new TerrainGenEventHandler());
    }

    @Handler
    public void postInit(FMLPostInitializationEvent evt) {}

    public void createEntities() {
        EntityRegistry.registerModEntity(FancyEntityItem.class, "Fancy Item", 0, TConstruct.instance, 32, 5, true);
        EntityRegistry.registerModEntity(DaggerEntity.class, "Dagger", 1, TConstruct.instance, 32, 5, true);
        EntityRegistry.registerModEntity(Crystal.class, "Crystal", 2, TConstruct.instance, 32, 3, true);
        EntityRegistry.registerModEntity(LaunchedPotion.class, "Launched Potion", 3, TConstruct.instance, 32, 3, true);
        EntityRegistry.registerModEntity(ArrowEntity.class, "Arrow", 4, TConstruct.instance, 32, 5, true);

        EntityRegistry.registerModEntity(BlueSlime.class, "EdibleSlime", 12, TConstruct.instance, 64, 5, true);
        EntityRegistry.registerModEntity(KingBlueSlime.class, "KingSlime", 14, TConstruct.instance, 64, 5, true);
        // EntityRegistry.registerModEntity(MetalSlime.class, "MetalSlime", 13,
        // TConstruct.instance, 64, 5, true);

        if (PHConstruct.naturalSlimeSpawn > 0) {
            EntityRegistry.addSpawn(
                    BlueSlime.class,
                    PHConstruct.naturalSlimeSpawn,
                    4,
                    20,
                    EnumCreatureType.monster,
                    BiomeDictionary.getBiomesForType(BiomeDictionary.Type.FOREST));
            EntityRegistry.addSpawn(
                    BlueSlime.class,
                    PHConstruct.naturalSlimeSpawn,
                    4,
                    20,
                    EnumCreatureType.monster,
                    BiomeDictionary.getBiomesForType(BiomeDictionary.Type.PLAINS));
            EntityRegistry.addSpawn(
                    BlueSlime.class,
                    PHConstruct.naturalSlimeSpawn,
                    4,
                    20,
                    EnumCreatureType.monster,
                    BiomeDictionary.getBiomesForType(BiomeDictionary.Type.MOUNTAIN));
            EntityRegistry.addSpawn(
                    BlueSlime.class,
                    PHConstruct.naturalSlimeSpawn,
                    4,
                    20,
                    EnumCreatureType.monster,
                    BiomeDictionary.getBiomesForType(BiomeDictionary.Type.HILLS));
            EntityRegistry.addSpawn(
                    BlueSlime.class,
                    PHConstruct.naturalSlimeSpawn,
                    4,
                    20,
                    EnumCreatureType.monster,
                    BiomeDictionary.getBiomesForType(BiomeDictionary.Type.SWAMP));
            EntityRegistry.addSpawn(
                    BlueSlime.class,
                    PHConstruct.naturalSlimeSpawn,
                    4,
                    20,
                    EnumCreatureType.monster,
                    BiomeDictionary.getBiomesForType(BiomeDictionary.Type.JUNGLE));
            EntityRegistry.addSpawn(
                    BlueSlime.class,
                    PHConstruct.naturalSlimeSpawn,
                    4,
                    20,
                    EnumCreatureType.monster,
                    BiomeDictionary.getBiomesForType(BiomeDictionary.Type.WASTELAND));
        }
    }

    private void craftingTableRecipes() {
        String[] patBlock = { "###", "###", "###" };
        String[] patSurround = { "###", "#m#", "###" };

        String[] dyeTypes = { "dyeBlack", "dyeRed", "dyeGreen", "dyeBrown", "dyeBlue", "dyePurple", "dyeCyan",
                "dyeLightGray", "dyeGray", "dyePink", "dyeLime", "dyeYellow", "dyeLightBlue", "dyeMagenta", "dyeOrange",
                "dyeWhite" };
        String color;
        for (int i = 0; i < 16; i++) {
            color = dyeTypes[15 - i];
            GameRegistry.addRecipe(
                    new ShapedOreRecipe(
                            new ItemStack(Blocks.wool, 8, i),
                            patSurround,
                            'm',
                            color,
                            '#',
                            new ItemStack(Blocks.wool, 1, Short.MAX_VALUE)));
        }
        // Stone Ladder Recipe
        GameRegistry.addRecipe(
                new ShapedOreRecipe(new ItemStack(TinkerWorld.stoneLadder, 3), "w w", "www", "w w", 'w', "rodStone"));
        // Wooden Rail (if registered) Recipe
        if (TinkerWorld.woodenRail != null) {
            GameRegistry.addRecipe(
                    new ShapedOreRecipe(
                            new ItemStack(TinkerWorld.woodenRail, 4, 0),
                            "b b",
                            "bxb",
                            "b b",
                            'b',
                            "plankWood",
                            'x',
                            "stickWood"));
        }
        // Stonesticks Recipes
        GameRegistry.addRecipe(new ItemStack(TinkerTools.toolRod, 4, 1), "c", "c", 'c', new ItemStack(Blocks.stone));
        GameRegistry
                .addRecipe(new ItemStack(TinkerTools.toolRod, 2, 1), "c", "c", 'c', new ItemStack(Blocks.cobblestone));
        //
        ItemStack aluBrass = new ItemStack(TinkerTools.materials, 1, 14);
        // Clock Recipe - Vanilla alternative
        GameRegistry.addRecipe(
                new ShapedOreRecipe(
                        new ItemStack(Items.clock),
                        " i ",
                        "iri",
                        " i ",
                        'i',
                        aluBrass,
                        'r',
                        "dustRedstone"));
        // Gold Pressure Plate - Vanilla alternative
        // todo: temporarily disabled due to light weighted pressure plate being smeltable to gold
        // GameRegistry.addRecipe(new ItemStack(Blocks.light_weighted_pressure_plate, 0, 1), "ii", 'i', aluBrass);

        // Ultra hardcore recipes
        GameRegistry.addRecipe(
                new ShapedOreRecipe(
                        new ItemStack(goldHead),
                        patSurround,
                        '#',
                        "ingotGold",
                        'm',
                        new ItemStack(Items.skull, 1, 3)));

        // Wool Slab Recipes
        for (int sc = 0; sc <= 7; sc++) {
            GameRegistry.addRecipe(
                    new ItemStack(TinkerWorld.woolSlab1, 6, sc),
                    "www",
                    'w',
                    new ItemStack(Blocks.wool, 1, sc));
            GameRegistry.addRecipe(
                    new ItemStack(TinkerWorld.woolSlab2, 6, sc),
                    "www",
                    'w',
                    new ItemStack(Blocks.wool, 1, sc + 8));

            GameRegistry.addShapelessRecipe(
                    new ItemStack(Blocks.wool, 1, sc),
                    new ItemStack(TinkerWorld.woolSlab1, 1, sc),
                    new ItemStack(TinkerWorld.woolSlab1, 1, sc));
            GameRegistry.addShapelessRecipe(
                    new ItemStack(Blocks.wool, 1, sc + 8),
                    new ItemStack(TinkerWorld.woolSlab2, 1, sc),
                    new ItemStack(TinkerWorld.woolSlab2, 1, sc));
        }
        GameRegistry.addRecipe(new ShapelessOreRecipe(new ItemStack(Blocks.wool, 1, 0), "slabCloth", "slabCloth"));

        // Slime Recipes
        GameRegistry.addRecipe(new ItemStack(TinkerWorld.slimeGel, 1, 0), "##", "##", '#', TinkerWorld.strangeFood);
        GameRegistry.addRecipe(
                new ItemStack(TinkerWorld.strangeFood, 4, 0),
                "#",
                '#',
                new ItemStack(TinkerWorld.slimeGel, 1, 0));
        GameRegistry.addRecipe(new ItemStack(TinkerWorld.slimeGel, 1, 1), "##", "##", '#', Items.slime_ball);
        GameRegistry
                .addRecipe(new ItemStack(Items.slime_ball, 4, 0), "#", '#', new ItemStack(TinkerWorld.slimeGel, 1, 1));
        // slimeExplosive
        GameRegistry.addShapelessRecipe(new ItemStack(TinkerWorld.slimeExplosive, 1, 0), Items.slime_ball, Blocks.tnt);
        GameRegistry.addShapelessRecipe(
                new ItemStack(TinkerWorld.slimeExplosive, 1, 2),
                TinkerWorld.strangeFood,
                Blocks.tnt);
        GameRegistry.addRecipe(
                new ShapelessOreRecipe(new ItemStack(TinkerWorld.slimeExplosive, 1, 0), "slimeball", Blocks.tnt));

        GameRegistry.addRecipe(
                new ShapelessOreRecipe(
                        new ItemStack(TinkerWorld.slimeChannel, 1, 0),
                        new ItemStack(TinkerWorld.slimeGel, 1, Short.MAX_VALUE),
                        "dustRedstone"));
        GameRegistry.addRecipe(
                new ShapelessOreRecipe(
                        new ItemStack(TinkerWorld.bloodChannel, 1, 0),
                        new ItemStack(TinkerWorld.strangeFood, 1, 1),
                        new ItemStack(TinkerWorld.strangeFood, 1, 1),
                        new ItemStack(TinkerWorld.strangeFood, 1, 1),
                        new ItemStack(TinkerWorld.strangeFood, 1, 1),
                        "dustRedstone"));
        GameRegistry.addRecipe(
                new ShapelessOreRecipe(
                        new ItemStack(TinkerWorld.slimeChannel, 1, 0),
                        "slimeball",
                        "slimeball",
                        "slimeball",
                        "slimeball",
                        "dustRedstone"));
        GameRegistry.addRecipe(
                new ShapelessOreRecipe(
                        new ItemStack(TinkerWorld.slimePad, 1, 0),
                        TinkerWorld.slimeChannel,
                        "slimeball"));
    }

    private void addRecipesForFurnace() {
        FurnaceRecipes.smelting().func_151394_a(
                new ItemStack(TinkerTools.craftedSoil, 1, 3),
                new ItemStack(TinkerTools.craftedSoil, 1, 4),
                0.2f); // Concecrated
        // Soil

        FurnaceRecipes.smelting().func_151394_a(
                new ItemStack(TinkerTools.craftedSoil, 1, 0),
                new ItemStack(TinkerTools.materials, 1, 1),
                2f); // Slime
        FurnaceRecipes.smelting().func_151394_a(
                new ItemStack(TinkerTools.craftedSoil, 1, 1),
                new ItemStack(TinkerTools.materials, 1, 2),
                2f); // Seared brick item
        FurnaceRecipes.smelting().func_151394_a(
                new ItemStack(TinkerTools.craftedSoil, 1, 2),
                new ItemStack(TinkerTools.materials, 1, 17),
                2f); // Blue Slime
        FurnaceRecipes.smelting().func_151394_a(
                new ItemStack(TinkerTools.craftedSoil, 1, 6),
                new ItemStack(TinkerTools.materials, 1, 37),
                2f); // Nether seared
        // brick

        // FurnaceRecipes.smelting().func_151394_a(new ItemStack(TRepo.oreSlag,
        // 1, new ItemStack(TRepo.materials, 1, 3), 3f);
        // FurnaceRecipes.smelting().func_151394_a(new ItemStack(TRepo.oreSlag,
        // 2, new ItemStack(TRepo.materials, 1, 4), 3f);
        // FurnaceRecipes.smelting().func_151394_a(new
        // ItemStack(TRepo.oreBerries, 5, new ItemStack(TRepo.materials, 1, 23),
        // 0.2f);
    }

    public void oreRegistry() {

        OreDictionary.registerOre("nuggetIron", new ItemStack(TinkerWorld.oreBerries, 1, 0));
        OreDictionary.registerOre("nuggetCopper", new ItemStack(TinkerWorld.oreBerries, 1, 2));
        OreDictionary.registerOre("nuggetTin", new ItemStack(TinkerWorld.oreBerries, 1, 3));
        OreDictionary.registerOre("nuggetAluminum", new ItemStack(TinkerWorld.oreBerries, 1, 4));
        OreDictionary.registerOre("nuggetAluminium", new ItemStack(TinkerWorld.oreBerries, 1, 4));
        OreDictionary.registerOre("nuggetGold", new ItemStack(TinkerWorld.oreBerries, 1, 1));

        // also register berries as berries. durr
        OreDictionary.registerOre("oreberryIron", new ItemStack(TinkerWorld.oreBerries, 1, 0));
        OreDictionary.registerOre("oreberryCopper", new ItemStack(TinkerWorld.oreBerries, 1, 2));
        OreDictionary.registerOre("oreberryTin", new ItemStack(TinkerWorld.oreBerries, 1, 3));
        OreDictionary.registerOre("oreberryAluminum", new ItemStack(TinkerWorld.oreBerries, 1, 4));
        OreDictionary.registerOre("oreberryAluminium", new ItemStack(TinkerWorld.oreBerries, 1, 4));
        OreDictionary.registerOre("oreberryGold", new ItemStack(TinkerWorld.oreBerries, 1, 1));
        OreDictionary.registerOre("oreberryEssence", new ItemStack(TinkerWorld.oreBerries, 1, 5));

        OreDictionary.registerOre("orebushIron", new ItemStack(TinkerWorld.oreBerry, 1, 0));
        OreDictionary.registerOre("orebushGold", new ItemStack(TinkerWorld.oreBerry, 1, 1));
        OreDictionary.registerOre("orebushCopper", new ItemStack(TinkerWorld.oreBerry, 1, 2));
        OreDictionary.registerOre("orebushTin", new ItemStack(TinkerWorld.oreBerry, 1, 3));

        OreDictionary.registerOre("orebushAluminum", new ItemStack(TinkerWorld.oreBerrySecond, 1, 4));
        OreDictionary.registerOre("orebushAluminium", new ItemStack(TinkerWorld.oreBerrySecond, 1, 4));
        OreDictionary.registerOre("orebushEssence", new ItemStack(TinkerWorld.oreBerrySecond, 1, 5));

        OreDictionary.registerOre("slabCloth", new ItemStack(TinkerWorld.woolSlab1, 1, Short.MAX_VALUE));
        OreDictionary.registerOre("slabCloth", new ItemStack(TinkerWorld.woolSlab2, 1, Short.MAX_VALUE));

        ensureOreIsRegistered("stoneMossy", new ItemStack(Blocks.stonebrick, 1, 1));
        ensureOreIsRegistered("stoneMossy", new ItemStack(Blocks.mossy_cobblestone));

        OreDictionary.registerOre("crafterWood", new ItemStack(Blocks.crafting_table, 1));
        OreDictionary.registerOre("craftingTableWood", new ItemStack(Blocks.crafting_table, 1));

        // Vanilla stuff
        OreDictionary.registerOre("slimeball", new ItemStack(Items.slime_ball));
        OreDictionary.registerOre("slimeball", new ItemStack(TinkerWorld.strangeFood, 1, 0));
        OreDictionary.registerOre("slimeball", new ItemStack(TinkerWorld.strangeFood, 1, 1));
        OreDictionary.registerOre("blockGlass", new ItemStack(Blocks.glass));
        RecipeRemover.removeShapedRecipe(new ItemStack(Blocks.sticky_piston));
        RecipeRemover.removeShapedRecipe(new ItemStack(Items.magma_cream));
        RecipeRemover.removeShapedRecipe(new ItemStack(Items.lead));
        GameRegistry.addRecipe(new ShapelessOreRecipe(new ItemStack(Blocks.sticky_piston), "slimeball", Blocks.piston));
        GameRegistry
                .addRecipe(new ShapelessOreRecipe(new ItemStack(Items.magma_cream), "slimeball", Items.blaze_powder));
        GameRegistry.addRecipe(
                new ShapedOreRecipe(
                        new ItemStack(Items.lead, 2),
                        "ss ",
                        "sS ",
                        "  s",
                        's',
                        Items.string,
                        'S',
                        "slimeball"));
    }

    public static void ensureOreIsRegistered(String oreName, ItemStack is) {
        int oreId = OreDictionary.getOreID(is);
        if (oreId == -1) {
            OreDictionary.registerOre(oreName, is);
        }
    }

    public void addLoot() {}
}
