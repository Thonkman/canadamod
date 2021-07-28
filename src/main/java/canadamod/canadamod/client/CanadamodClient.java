package canadamod.canadamod.client;

import canadamod.canadamod.Canadamod;
import canadamod.canadamod.block.BoxScreen;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.screenhandler.v1.ScreenRegistry;

@net.fabricmc.api.Environment(net.fabricmc.api.EnvType.CLIENT)
public class CanadamodClient implements ClientModInitializer {

    public void onInitializeClient() {
        //ScreenRegistry.register(Canadamod.BOX_SCREEN_HANDLER, BoxScreen::new);
    }
}
