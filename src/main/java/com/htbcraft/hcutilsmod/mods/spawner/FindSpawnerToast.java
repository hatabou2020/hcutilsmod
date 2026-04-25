package com.htbcraft.hcutilsmod.mods.spawner;

import com.htbcraft.hcutilsmod.HcUtilsMod;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.gui.components.toasts.Toast;
import net.minecraft.client.gui.components.toasts.ToastManager;
import net.minecraft.client.renderer.RenderPipelines;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.jspecify.annotations.NonNull;

import java.util.LinkedHashSet;

public class FindSpawnerToast implements Toast {
    private static final Logger LOGGER = LogManager.getLogger();

    private static final Identifier TEXTURE = Identifier.withDefaultNamespace("toast/advancement");
    private static final Identifier SPAWNER_ICON = Identifier.fromNamespaceAndPath(HcUtilsMod.MODID, "toast/spawner");
    private static final LinkedHashSet<BlockPos> blockPosList = new LinkedHashSet<>();

    private final BlockPos blockPos;
    private final long timeout;
    private final Boolean visibility;

    private Visibility wantedVisibility = Visibility.HIDE;

    public FindSpawnerToast(BlockPos blockPos, long timeout) {
        this.blockPos = blockPos;
        this.timeout = timeout;

        // 表示している座標との重複をチェックするためにLinkedHashSetを使う
        this.visibility = blockPosList.add(blockPos);
    }

    @Override
    public @NonNull Visibility getWantedVisibility() {
        return wantedVisibility;
    }

    @Override
    public void update(@NonNull ToastManager toastManager, long l) {
        // すでに表示している座標の場合は即非表示
        if (!this.visibility) {
            LOGGER.info("Already displayed!! {}", this.blockPos);
            wantedVisibility = Visibility.HIDE;
            // HIDE を返すと音が鳴るのがイケてない。
        }

        if (Minecraft.getInstance().screen != null) {
            LOGGER.info("Screen Display");
            wantedVisibility = Visibility.HIDE;
        }

        // 設定されている時間だけ表示
        if (l > (this.timeout * 1000L)) {
            LOGGER.info("Time up!! {}", this.blockPos);
            blockPosList.remove(this.blockPos);
            wantedVisibility = Visibility.HIDE;
        }
    }

    @Override
    public void extractRenderState(GuiGraphicsExtractor guiGraphicsExtractor, @NonNull Font font, long l) {
        // トーストの枠
        guiGraphicsExtractor.blitSprite(RenderPipelines.GUI_TEXTURED, TEXTURE, 0, 0, this.width(), this.height());

        // スポナーのアイコン
        guiGraphicsExtractor.blitSprite(RenderPipelines.GUI_TEXTURED, SPAWNER_ICON, 6, 6, 20, 20);

        // 見つけたスポナーの座標
        String text = "X:" + this.blockPos.getX() + " Y:" + this.blockPos.getY() + " Z:" + this.blockPos.getZ();
        guiGraphicsExtractor.text(font, Component.translatable("hcutilsmod.toast.findSpawner.message"), 30, 7, -2039584);
        guiGraphicsExtractor.text(font, text, 36, 18, -2039584);

        wantedVisibility = Visibility.SHOW;
    }
}
