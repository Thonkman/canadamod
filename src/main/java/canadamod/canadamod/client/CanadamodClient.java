package canadamod.canadamod.client;

import canadamod.canadamod.Canadamod;
import canadamod.canadamod.block.carbonator.CarbonatorScreen;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.client.screenhandler.v1.ScreenRegistry;

@Environment(net.fabricmc.api.EnvType.CLIENT)
public class CanadamodClient implements ClientModInitializer {

    public void onInitializeClient() {
        ScreenRegistry.register(Canadamod.CARBONATOR_SCREEN_HANDLER, CarbonatorScreen::new);
    }
}
