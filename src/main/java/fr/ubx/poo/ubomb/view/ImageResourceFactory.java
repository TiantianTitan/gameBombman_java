package fr.ubx.poo.ubomb.view;

import fr.ubx.poo.ubomb.game.Direction;
import javafx.scene.image.Image;

public class ImageResourceFactory {
    public static ImageResource digit(int i) {
        if (i < 0 || i > 9) throw new IllegalArgumentException("Digit must be in [0-9]");
        return ImageResource.valueOf("DIGIT_" + i);
    }

    public static ImageResource getPlayer(Direction direction,boolean bless) {
        if(!bless) return ImageResource.valueOf("PLAYER_" + direction);
        else return ImageResource.valueOf("PLAYER_BLESS_" + direction);
    }

    public static ImageResource getMonster(Direction direction, boolean attack) {
        if(!attack) return ImageResource.valueOf("MONSTER_" + direction);
        else return ImageResource.valueOf("MONSTER_ATTACK_" + direction);
    }

    public static ImageResource getBomb(int i) {
        if (i < 0 || i > 3)
            throw new IllegalArgumentException();
        return ImageResource.valueOf("BOMB_"+i);
    }

}
