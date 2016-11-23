package us.ichun.mods.tabula.common;

import me.ichun.mods.ichunutil.common.core.Logger;
import me.ichun.mods.ichunutil.common.core.config.ConfigHandler;
import me.ichun.mods.ichunutil.common.core.network.PacketChannel;
import me.ichun.mods.ichunutil.common.iChunUtil;
import me.ichun.mods.ichunutil.common.module.update.UpdateChecker;
import net.minecraft.block.Block;
import net.minecraft.client.model.ModelBase;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.SidedProxy;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLInterModComms;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import us.ichun.mods.tabula.client.core.ResourceHelper;
import us.ichun.mods.tabula.common.core.CommonProxy;
import us.ichun.mods.tabula.common.core.Config;
import us.ichun.mods.tabula.common.core.EventHandler;

import java.io.File;

@Mod(modid = Tabula.MOD_ID, name = Tabula.MOD_NAME,
        version = Tabula.VERSION,
        guiFactory = "me.ichun.mods.ichunutil.common.core.config.GenericModGuiFactory",
        dependencies = "required-after:ichunutil@[" + iChunUtil.VERSION_MAJOR +".0.0," + (iChunUtil.VERSION_MAJOR + 1) + ".0.0)",
        acceptableRemoteVersions = "[" + iChunUtil.VERSION_MAJOR +".0.0," + iChunUtil.VERSION_MAJOR + ".1.0)"
)
public class Tabula
{
    public static final String VERSION = iChunUtil.VERSION_MAJOR + ".0.0";
    public static final String MOD_NAME = "Tabula";
    public static final String MOD_ID = "tabula";

    public static final Logger LOGGER = Logger.createLogger(Tabula.MOD_NAME);

    @Mod.Instance(MOD_ID)
    public static Tabula instance;

    @SidedProxy(clientSide = "me.ichun.mods.tabula.client.core.ClientProxy", serverSide = "me.ichun.mods.tabula.common.core.CommonProxy")
    public static CommonProxy proxy;

    public static PacketChannel channel;

    public static Config config;

    public static Block blockTabulaRasa;

    @Mod.EventHandler
    public void preLoad(FMLPreInitializationEvent event)
    {
        proxy.preInit();

        if(FMLCommonHandler.instance().getEffectiveSide().isClient())
        {
            config = ConfigHandler.registerConfig(new Config(new File(ResourceHelper.getConfigDir(), "config.cfg")));
        }

        EventHandler handler = new EventHandler();
        FMLCommonHandler.instance().bus().register(handler);
        MinecraftForge.EVENT_BUS.register(handler);

        UpdateChecker.registerMod(new UpdateChecker.ModVersionInfo(MOD_NAME, iChunUtil.VERSION_OF_MC, VERSION, false));
    }

    @Mod.EventHandler
    public void init(FMLInitializationEvent event)
    {
        proxy.init();
    }

    @Mod.EventHandler
    public void postInit(FMLPostInitializationEvent event)
    {
        proxy.postInit();
    }

    @Mod.EventHandler
    public void onIMCMessage(FMLInterModComms.IMCEvent event)
    {
        for(FMLInterModComms.IMCMessage message : event.getMessages())
        {
            if(message.key.equalsIgnoreCase("blacklist"))
            {
                try
                {
                    Class clz = Class.forName(message.getStringValue());
                    if(ModelBase.class.isAssignableFrom(clz))
                    {
                        if(ModelList.modelBlacklist.contains(clz))
                        {
                            Tabula.LOGGER.warn(message.getStringValue() + " is already blacklisted");
                        }
                        else
                        {
                            ModelList.modelBlacklist.add(clz);
                            Tabula.LOGGER.warn(message.getStringValue() + " blacklisted from Tabula's import list");
                        }
                    }
                    else
                    {
                        Tabula.LOGGER.warn(message.getStringValue() + " is not a model class!");
                    }
                }
                catch(Exception e)
                {
                    Tabula.LOGGER.warn("Could not find class " + message.getStringValue() + " for blacklist");
                }
            }
        }
    }
}
