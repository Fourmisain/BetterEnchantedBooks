package dev.bernasss12.bebooks.client.gui.tooltip;

import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.gui.tooltip.TooltipComponent;
import net.minecraft.client.render.item.ItemRenderer;
import net.minecraft.client.texture.TextureManager;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.item.ItemStack;

import java.util.List;

public record IconTooltipComponent(List<ItemStack> icons) implements TooltipComponent {

    @Override
    public int getHeight() {
        return 10;
    }

    @Override
    public int getWidth(TextRenderer textRenderer) {
        return icons.size() * 8;
    }

    @Override
    public void drawItems(TextRenderer textRenderer, int x, int y, MatrixStack _matrices, ItemRenderer itemRenderer, int z, TextureManager textureManager) {
        float scale = 0.5f;
        int scaledX = (int) (x / scale);
        int scaledY = (int) (y / scale);
        int scaledOffset =  (int) (8 / scale);

        MatrixStack matrices = RenderSystem.getModelViewStack();
        matrices.push();
        matrices.scale(0.5f, 0.5f, 1.0f);

        // works outside of REI
        renderIcons(itemRenderer, scaledX, scaledY, scaledOffset);

//        // when offset, you can see the icons are actually rendered at the correct place but they hide behind the tooltip itself
//        renderIcons(itemRenderer, scaledX, scaledY + 16, scaledOffset);

//        // changing the zOffset doesn't work
//        itemRenderer.zOffset = -200f;
//        renderIcons(itemRenderer, scaledX, scaledY, scaledOffset);
//        itemRenderer.zOffset = -100f;
//        renderIcons(itemRenderer, scaledX, scaledY, scaledOffset);
//        itemRenderer.zOffset = 0f;
//        renderIcons(itemRenderer, scaledX, scaledY, scaledOffset);
//        itemRenderer.zOffset = 100f;
//        renderIcons(itemRenderer, scaledX, scaledY, scaledOffset);
//        itemRenderer.zOffset = 200f;
//        renderIcons(itemRenderer, scaledX, scaledY, scaledOffset);

//        // changing the z value directly doesn't work either...
//        matrices.translate(0, 0, 200); // or -200?
//        renderIcons(itemRenderer, scaledX, scaledY, scaledOffset);

        matrices.pop();
    }

    public void renderIcons(ItemRenderer itemRenderer, int x, int y, int offset) {
        for (int i = 0; i < icons.size(); i++) {
            itemRenderer.renderInGuiWithOverrides(icons.get(i), x + offset * i, y, 0);
        }
    }
}
