package net.onixary.shapeShifterCurseFabric.mixin.extra_logic;

import net.minecraft.client.render.entity.model.BipedEntityModel;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.ArmorItem;
import net.minecraft.item.ArmorMaterial;
import net.minecraft.item.ItemStack;
import net.minecraftforge.client.extensions.common.IClientItemExtensions;
import net.onixary.shapeShifterCurseFabric.items.armors.MorphScaleArmor;
import net.onixary.shapeShifterCurseFabric.items.armors.MorphscaleArmorRenderer;
import org.jetbrains.annotations.NotNull;
import org.spongepowered.asm.mixin.Mixin;
import software.bernie.geckolib.renderer.GeoArmorRenderer;

import java.util.function.Consumer;

@Mixin(MorphScaleArmor.class)
public class MorphScaleRenderRegisterA extends ArmorItem  {
    public MorphScaleRenderRegisterA(ArmorMaterial material, Type type, Settings settings) {
        super(material, type, settings);
    }

    @Override
    public void initializeClient(Consumer<IClientItemExtensions> consumer) {
        consumer.accept(new IClientItemExtensions() {
            private GeoArmorRenderer<MorphScaleArmor> renderer;

            @Override
            public @NotNull GeoArmorRenderer<MorphScaleArmor> getHumanoidArmorModel(LivingEntity livingEntity, ItemStack itemStack, EquipmentSlot equipmentSlot, BipedEntityModel<?> original) {
                if(this.renderer == null) {
                    this.renderer = new MorphscaleArmorRenderer();
                }
                this.renderer.prepForRender(livingEntity, itemStack, equipmentSlot, original);
                return this.renderer;
            }
        });
    }
}
