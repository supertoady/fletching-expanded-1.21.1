package toady.fletching.mixin;

import net.minecraft.block.BlockState;
import net.minecraft.block.FletchingTableBlock;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.screen.NamedScreenHandlerFactory;
import net.minecraft.screen.ScreenHandlerContext;
import net.minecraft.screen.SimpleNamedScreenHandlerFactory;
import net.minecraft.screen.SmithingScreenHandler;
import net.minecraft.stat.Stats;
import net.minecraft.text.Text;
import net.minecraft.util.ActionResult;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import toady.fletching.gui.FletchingScreenHandler;

@Mixin(FletchingTableBlock.class)
public class FletchingTableBlockMixin {

    //fairly obvious, makes fletching tables trigger fletching table gui
    @Unique
    private static final Text SCREEN_TITLE = Text.translatable("container.fletching");

    @Unique
    protected NamedScreenHandlerFactory createScreenHandlerFactory(BlockState state, World world, BlockPos pos) {
        return new SimpleNamedScreenHandlerFactory((syncId, inventory, player) ->
                new FletchingScreenHandler(syncId, inventory), SCREEN_TITLE);
    }

    @Inject(method = "onUse", at = @At("HEAD"), cancellable = true)
    public void onUse(BlockState state, World world, BlockPos pos, PlayerEntity player, BlockHitResult hit, CallbackInfoReturnable<ActionResult> cir){
        if (world.isClient) {
            cir.setReturnValue(ActionResult.SUCCESS);
        } else {
            player.openHandledScreen(createScreenHandlerFactory(state, world, pos));
            player.incrementStat(Stats.INTERACT_WITH_SMITHING_TABLE);
            cir.setReturnValue(ActionResult.CONSUME);
        }
    }
}
