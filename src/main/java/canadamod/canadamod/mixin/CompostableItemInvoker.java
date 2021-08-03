package canadamod.canadamod.mixin;

import net.minecraft.item.ItemConvertible;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Invoker;

@SuppressWarnings("unused")
@Mixin(net.minecraft.block.ComposterBlock.class)
public interface CompostableItemInvoker {
    @Invoker
    static void invokeRegisterCompostableItem(float levelIncreaseChance, ItemConvertible item) {

    }
}
