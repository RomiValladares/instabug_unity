package com.duelit.model;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.os.Parcel;
import android.os.Parcelable;
import android.support.annotation.DrawableRes;
import android.support.v4.content.ContextCompat;

import java.util.HashMap;
import java.util.Map;

import com.duelit.R;

/**
 * Created by romin on 24/3/2016.
 */
public class Game implements Parcelable {
    public static final int FRUIT_SMASH_ID = 2;
    public static final int PENALTY_KICKOFF_ID = 3;
    public static final int ICE_SHOOTER_ID = 4;
    public static final int SEA_BLOCKS_ID = 6;
    public static final int TRIVIA_ID = 7;
    public static final int CUPS_ID = 8;

    public enum GameResource {
        //used in recently played games
        THUMBNAIL,
        //used as tile background in games screen
        TILE_BACKGROUND,
        //used as icon background in games screen
        TILE_ICON,
        //the icon for this game's menu screen, and icon for header in open/public tournaments
        GAME_MENU_ICON,
        //used as card background in open tournaments
        CARD_BACKGROUND,
        //header background in open/public tournaments
        HEADER_BACKGROUND,
        //used in unity practice mode
        BACK_BUTTON
    }

    private int id;

    private Map<GameResource, Drawable> cachedDrawables;

    private String title;
    private String legend;
    private String appIconURL;

    public Game() {
        cachedDrawables = new HashMap<>();
    }

    public Game(int id) {
        this();
        this.id = id;
    }

    /**
     * instantiating a Game without and id = not cool
     */
    public Game(String title) {
        this();
        this.title = title;
    }


    public Game(int id, String title, String legend) {
        this(id);
        this.title = title;
        this.legend = legend;
    }

    public Game(int id, String title, String legend, Drawable drawableIconImage, Drawable drawableBackgroundImage) {
        this(id, title, legend);
        cachedDrawables.put(GameResource.TILE_BACKGROUND, drawableBackgroundImage);
        cachedDrawables.put(GameResource.THUMBNAIL, drawableIconImage);
    }

    public String getLegend() {
        return legend;
    }

    public String getTitle() {
        return title;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setLegend(String legend) {
        this.legend = legend;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    @DrawableRes
    public int getDrawableResource(GameResource resource) {
        int resourceId = -1;
        switch (id) {
            case PENALTY_KICKOFF_ID:
                switch (resource) {
                    case GAME_MENU_ICON:
                        resourceId = R.drawable.game_menu_logo_penalty_kickoff;
                        break;
                    case TILE_BACKGROUND:
                        resourceId = R.drawable.game_tile_background_penalty_kickoff;
                        break;
                    case THUMBNAIL:
                        resourceId = R.drawable.game_thumbnail_penalty_kickoff;
                        break;
                    case TILE_ICON:
                        resourceId = R.drawable.game_tile_icon_penalty_kickoff;
                        break;
                    case CARD_BACKGROUND:
                        resourceId = R.drawable.game_card_background_penalty_kickoff;
                        break;
                    case HEADER_BACKGROUND:
                        resourceId = R.drawable.game_header_background_penalty_kickoff;
                        break;
                    case BACK_BUTTON:
                        resourceId = R.drawable.btn_back_penalty;
                        break;
                }
                break;
            case FRUIT_SMASH_ID:
                switch (resource) {
                    case GAME_MENU_ICON:
                        resourceId = R.drawable.game_menu_logo_fruit_smash;
                        break;
                    case TILE_BACKGROUND:
                        resourceId = R.drawable.game_tile_background_fruit_smash;
                        break;
                    case THUMBNAIL:
                        resourceId = R.drawable.game_thumbnail_fruit_smash;
                        break;
                    case TILE_ICON:
                        resourceId = R.drawable.game_tile_icon_fruit_smash;
                        break;
                    case CARD_BACKGROUND:
                        resourceId = R.drawable.game_card_background_fruit_smash;
                        break;
                    case HEADER_BACKGROUND:
                        resourceId = R.drawable.game_header_background_fruit_smash;
                        break;
                    case BACK_BUTTON:
                        resourceId = R.drawable.btn_back_fruit;
                        break;
                }
                break;
            case ICE_SHOOTER_ID:
                switch (resource) {
                    case GAME_MENU_ICON:
                        resourceId = R.drawable.game_menu_logo_ice_shooter;
                        break;
                    case TILE_BACKGROUND:
                        resourceId = R.drawable.game_tile_background_ice_shooter;
                        break;
                    case THUMBNAIL:
                        resourceId = R.drawable.game_thumbnail_ice_shooter;
                        break;
                    case TILE_ICON:
                        resourceId = R.drawable.game_tile_icon_ice_shooter;
                        break;
                    case CARD_BACKGROUND:
                        resourceId = R.drawable.game_card_background_ice_shooter;
                        break;
                    case HEADER_BACKGROUND:
                        resourceId = R.drawable.game_header_background_ice_shooter;
                        break;
                    case BACK_BUTTON:
                        resourceId = R.drawable.btn_back_ice;
                        break;
                }
                break;
            case SEA_BLOCKS_ID:
                switch (resource) {
                    case GAME_MENU_ICON:
                        resourceId = R.drawable.game_menu_logo_sea_blocks;
                        break;
                    case TILE_BACKGROUND:
                        resourceId = R.drawable.game_tile_background_sea_blocks;
                        break;
                    case THUMBNAIL:
                        resourceId = R.drawable.game_thumbnail_sea_blocks;
                        break;
                    case TILE_ICON:
                        resourceId = R.drawable.game_tile_icon_sea_blocks;
                        break;
                    case CARD_BACKGROUND:
                        resourceId = R.drawable.game_card_background_sea_blocks;
                        break;
                    case HEADER_BACKGROUND:
                        resourceId = R.drawable.game_header_background_sea_blocks;
                        break;
                    case BACK_BUTTON:
                        resourceId = R.drawable.btn_back_sea;
                        break;
                }
                break;
            case CUPS_ID:
                switch (resource) {
                    case GAME_MENU_ICON:
                        resourceId = R.drawable.game_menu_logo_cups;
                        break;
                    case TILE_BACKGROUND:
                        resourceId = R.drawable.game_tile_background_cups;
                        break;
                    case THUMBNAIL:
                        resourceId = R.drawable.game_thumbnail_cups;
                        break;
                    case TILE_ICON:
                        resourceId = R.drawable.game_tile_icon_cups;
                        break;
                    case CARD_BACKGROUND:
                        resourceId = R.drawable.game_card_background_cups;
                        break;
                    case HEADER_BACKGROUND:
                        resourceId = R.drawable.game_header_background_cups;
                        break;
                    case BACK_BUTTON:
                        resourceId = R.drawable.btn_back_cups;
                        break;
                }
                break;
            case TRIVIA_ID:
                switch (resource) {
                    case GAME_MENU_ICON:
                        resourceId = R.drawable.game_menu_logo_trivia;
                        break;
                    case TILE_BACKGROUND:
                        resourceId = R.drawable.game_tile_background_trivia;
                        break;
                    case THUMBNAIL:
                        resourceId = R.drawable.game_thumbnail_trivia;
                        break;
                    case TILE_ICON:
                        resourceId = R.drawable.game_tile_icon_trivia;
                        break;
                    case CARD_BACKGROUND:
                        resourceId = R.drawable.game_card_background_trivia;
                        break;
                    case HEADER_BACKGROUND:
                        resourceId = R.drawable.game_header_background_trivia;
                        break;
                    case BACK_BUTTON:
                        resourceId = R.drawable.btn_back_trivia;
                        break;
                }
                break;
        }
        return resourceId;
    }

    public Drawable getDrawableImage(GameResource resource, Context context, boolean saveInCache) {
        //it could happen when saving to parcelable
        if (cachedDrawables == null) {
            cachedDrawables = new HashMap<>();
        }

        if (cachedDrawables.containsKey(resource)) {
            return cachedDrawables.get(resource);
        }

        int resourceId = getDrawableResource(resource);
        Drawable drawable = null;
        if (resourceId > -1) {
            drawable = ContextCompat.getDrawable(context, resourceId);
            if (saveInCache) {
                cachedDrawables.put(resource, drawable);
            }
        }
        return drawable;
    }

    /**
     * get the game's name from string resources
     * this means that if the games changed names from server, this method may not return the
     * last updated name
     * use as a last resort when you really need the game title and can't get it from server
     *
     * @return the game's name based on its gameID, from local resources
     */
    public String getOriginalTitle() {
        if (getTitle() != null)
            return getTitle();

        switch (id) {
            case FRUIT_SMASH_ID:
                return "Fruit Smash";
            case PENALTY_KICKOFF_ID:
                return "Kick Off";
            case ICE_SHOOTER_ID:
                return "Ice Shooter";
            case SEA_BLOCKS_ID:
                return "Sea Blocks";
            case TRIVIA_ID:
                return "Smarty";
            case CUPS_ID:
                return "Cups";
        }

        return "";
    }

    protected Game(Parcel in) {
        id = in.readInt();
        title = in.readString();
        legend = in.readString();
        appIconURL = in.readString();
    }

    @Override
    public String toString() {
        return "Game{" +
                "appIconURL='" + appIconURL + '\'' +
                ", id=" + id +
                ", title='" + title + '\'' +
                ", legend='" + legend + '\'' +
                '}';
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(id);
        dest.writeString(title);
        dest.writeString(legend);
        dest.writeString(appIconURL);
    }

    @SuppressWarnings("unused")
    public static final Parcelable.Creator<Game> CREATOR = new Parcelable.Creator<Game>() {
        @Override
        public Game createFromParcel(Parcel in) {
            return new Game(in);
        }

        @Override
        public Game[] newArray(int size) {
            return new Game[size];
        }
    };
}
