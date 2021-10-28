package dev.bernasss12.bebooks.util;

import net.minecraft.client.item.TooltipData;
import net.minecraft.item.ItemStack;

import java.util.ArrayList;
import java.util.List;

public record IconsTooltipData(ArrayList<List<ItemStack>> applicableItemsList) implements TooltipData {

}
