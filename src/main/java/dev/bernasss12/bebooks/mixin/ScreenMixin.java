package dev.bernasss12.bebooks.mixin;

import dev.bernasss12.bebooks.client.gui.ModConfig;
import dev.bernasss12.bebooks.client.gui.tooltip.IconTooltipComponent;
import dev.bernasss12.bebooks.util.IconsTooltipData;
import dev.bernasss12.bebooks.util.text.IconTooltipDataText;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.gui.DrawableHelper;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.tooltip.OrderedTextTooltipComponent;
import net.minecraft.client.gui.tooltip.TooltipComponent;
import net.minecraft.client.item.TooltipData;
import net.minecraft.text.OrderedText;
import org.spongepowered.asm.mixin.Dynamic;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyVariable;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;

import java.util.List;
import java.util.stream.Collectors;

@Mixin(Screen.class)
@Environment(EnvType.CLIENT)
public abstract class ScreenMixin extends DrawableHelper {

    // converts tooltip data to tooltip component
    @Dynamic("renderTooltip(..., List<Text> lines, ...)'s lambda")
    @Inject(method = "method_32635", at = @At("HEAD"), cancellable = true, locals = LocalCapture.CAPTURE_FAILHARD, remap = false)
    private static void onComponentConstruct(List<TooltipComponent> list, TooltipData data, CallbackInfo ci) {
        if (data instanceof IconsTooltipData iconsData) {
            list.add(new IconTooltipComponent(iconsData.applicableItemsList().get(0)));
            ci.cancel();
        }
    }

    @ModifyVariable(method = "Lnet/minecraft/client/gui/screen/Screen;renderTooltipFromComponents(Lnet/minecraft/client/util/math/MatrixStack;Ljava/util/List;II)V", argsOnly = true, at = @At("HEAD"))
    private List<TooltipComponent> convertTooltipComponents(List<TooltipComponent> components) {
        if (ModConfig.tooltipSetting == ModConfig.TooltipSetting.ENABLED || (ModConfig.tooltipSetting == ModConfig.TooltipSetting.ON_SHIFT && Screen.hasShiftDown())) {
            return components.stream().map(
                originalComponent -> {
                    if (originalComponent instanceof OrderedTextTooltipComponent) {
                        OrderedText text = ((OrderedTextTooltipComponentAccessor) originalComponent).getText();
                        if (text instanceof IconTooltipDataText) {
                            return new IconTooltipComponent(((IconTooltipDataText) text).icons());
                        }
                    }
                    return originalComponent;
                }
            ).collect(Collectors.toList());
        }
        return components;
    }
}
