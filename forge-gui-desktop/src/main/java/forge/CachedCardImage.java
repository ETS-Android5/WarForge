package forge;

import java.awt.image.BufferedImage;

import forge.game.card.CardView;
import forge.game.player.PlayerView;
import forge.util.ImageFetcher;
import forge.util.SwingImageFetcher;


public abstract class CachedCardImage implements ImageFetcher.Callback {
    final CardView card;
    final Iterable<PlayerView> viewers;
    final int width;
    final int height;

    static final SwingImageFetcher fetcher = new SwingImageFetcher();

    public CachedCardImage(final CardView card, final Iterable<PlayerView> viewers, final int width, final int height) {
        this.card = card;
        this.viewers = viewers;
        this.width = width;
        this.height = height;
        BufferedImage image = ImageCache.getImageNoDefault(card, viewers, width, height);
        if (image == null) {
            String key = card.getCurrentState().getImageKey(viewers);
            fetcher.fetchImage(key, this);
        }
    }

    public BufferedImage getImage() {
        return ImageCache.getImage(card, viewers, width, height);
    }

    public BufferedImage getFrontImage() {
        return ImageCache.getImage(card, null, width, height);
    }

    public abstract void onImageFetched();
}
