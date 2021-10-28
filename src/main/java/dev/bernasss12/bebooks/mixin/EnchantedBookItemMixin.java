package dev.bernasss12.bebooks.mixin;

import dev.bernasss12.bebooks.BetterEnchantedBooks;
import dev.bernasss12.bebooks.client.gui.ModConfig;
import dev.bernasss12.bebooks.util.IconsTooltipData;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.item.TooltipData;
import net.minecraft.item.EnchantedBookItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtList;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Mixin(EnchantedBookItem.class)
@Environment(EnvType.CLIENT)
public abstract class EnchantedBookItemMixin extends Item {
    public EnchantedBookItemMixin(Settings settings) {
        super(settings);
    }

    @Inject(method = "hasGlint", at = @At(value = "RETURN"), cancellable = true)
    public void hasGlintReturn(CallbackInfoReturnable<Boolean> info) {
        info.setReturnValue(ModConfig.glintSetting);
    }

    @Override
    public Optional<TooltipData> getTooltipData(ItemStack stack) {
        ArrayList<List<ItemStack>> applicableItemsList;

        NbtList enchantments = EnchantedBookItem.getEnchantmentNbt(stack);
        applicableItemsList = new ArrayList<>(enchantments.size());
        for (int i = 0; i < enchantments.size(); i++) {
            NbtCompound nbtCompound = enchantments.getCompound(i);
            Registry.ENCHANTMENT.getOrEmpty(Identifier.tryParse(nbtCompound.getString("id"))).ifPresent((enchantment) -> {
                applicableItemsList.add(BetterEnchantedBooks.getApplicableItems(enchantment));
            });
        }

        return Optional.of(new IconsTooltipData(applicableItemsList));
    }
}
