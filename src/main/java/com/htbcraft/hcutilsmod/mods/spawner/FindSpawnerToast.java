package com.htbcraft.hcutilsmod.mods.spawner;

import com.htbcraft.hcutilsmod.common.HCSettings;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.gui.GuiComponent;
import net.minecraft.client.gui.components.toasts.Toast;
import net.minecraft.client.gui.components.toasts.ToastComponent;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.resources.ResourceLocation;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.LinkedHashSet;

public class FindSpawnerToast implements Toast {
    private static final Logger LOGGER = LogManager.getLogger();

    private static final ResourceLocation SPAWNER_ICON = new ResourceLocation("textures/block/spawner.png");
    private static final LinkedHashSet<BlockPos> blockPosList = new LinkedHashSet<>();

    private final BlockPos blockPos;
    private final Boolean visibility;

    public FindSpawnerToast(BlockPos blockPos) {
        this.blockPos = blockPos;

        // 表示している座標との重複をチェックするためにLinkedHashSetを使う
        this.visibility = blockPosList.add(blockPos);
    }

    @Override
    public Toast.Visibility render(PoseStack p_94896_, ToastComponent p_94897_, long p_94898_) {
        // すでに表示している座標の場合は即非表示
        if (!this.visibility) {
            LOGGER.info("Already displayed!! " + this.blockPos);
            return Visibility.HIDE;
            // HIDE を返すと音が鳴るのがイケてない。
        }

        // 設定されている時間だけ表示
        if (p_94898_ > (HCSettings.getInstance().timeFindSpawner * 1000L)) {
            LOGGER.info("Time up!! " + this.blockPos);
            blockPosList.remove(this.blockPos);
            return Visibility.HIDE;
        }

        // トーストの枠
        RenderSystem.setShaderTexture(0, TEXTURE);
        RenderSystem.setShaderFogColor(1.0F, 1.0F, 1.0F);
        p_94897_.blit(p_94896_, 0, 0, 0, 0, this.width(), this.height());

        // スポナーのアイコン
        RenderSystem.setShaderTexture(0, SPAWNER_ICON);
        RenderSystem.enableBlend();
        GuiComponent.blit(p_94896_, 6, 6, 0, 0, 0, 20, 20, 20, 20);
        RenderSystem.enableBlend();

        // 見つけたスポナーの座標
        String text = "X:" + this.blockPos.getX() + " Y:" + this.blockPos.getY() + " Z:" + this.blockPos.getZ();
        p_94897_.getMinecraft().font.draw(p_94896_, new TranslatableComponent("hcutilsmod.findspawner.text"), 30.0F, 7.0F, 14737632);
        p_94897_.getMinecraft().font.draw(p_94896_, text, 36.0F, 18.0F, 14737632);

        return Visibility.SHOW;
    }
}
