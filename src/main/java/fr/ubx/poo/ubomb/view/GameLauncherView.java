package fr.ubx.poo.ubomb.view;

import fr.ubx.poo.ubomb.engine.GameEngine;
import fr.ubx.poo.ubomb.game.Configuration;
import fr.ubx.poo.ubomb.game.Game;
import fr.ubx.poo.ubomb.game.Level;
import fr.ubx.poo.ubomb.game.Position;
import fr.ubx.poo.ubomb.launcher.GameLauncher;
import fr.ubx.poo.ubomb.launcher.MapLevel;
import javafx.scene.control.*;
import javafx.scene.input.KeyCombination;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.*;
import java.util.Properties;


public class GameLauncherView extends BorderPane {
    private final FileChooser fileChooser = new FileChooser();
    private boolean modeScore;

    public GameLauncherView(Stage stage)  {
        // Create menu
        MenuBar menuBar = new MenuBar();
        Menu menuFile = new Menu("File");
        MenuItem loadItem = new MenuItem("Load from file ...");
        MenuItem defaultItem = new MenuItem("Load default configuration");
        MenuItem scoreModeItem = new MenuItem("scoreMode");
        MenuItem exitItem = new MenuItem("Exit");
        exitItem.setAccelerator(KeyCombination.keyCombination("Ctrl+Q"));
        menuFile.getItems().addAll(
                loadItem, defaultItem, new SeparatorMenuItem(),
                scoreModeItem,new SeparatorMenuItem(),
                exitItem);

        menuBar.getMenus().addAll(menuFile);
        this.setTop(menuBar);

        Text text = new Text("UBomb 2022");
        text.getStyleClass().add("message");
        VBox scene = new VBox();
        scene.getChildren().add(text);
        scene.getStylesheets().add(getClass().getResource("/css/application.css").toExternalForm());
        scene.getStyleClass().add("message");
        this.setCenter(scene);

        // Load from file
        loadItem.setOnAction(e -> {
            File file = fileChooser.showOpenDialog(stage);
            if (file != null) {
                // Implementation de lire le fichier
                java.util.Properties config = new Properties();;
                Reader in = null;
                try {
                    in = new FileReader(file);
                } catch (FileNotFoundException ex) {
                    throw new RuntimeException(ex);
                }
                try {
                    config.load(in);
                } catch (IOException ex) {
                    throw new RuntimeException(ex);
                }

                // Les varaibles qu'on a enregistré
                boolean compression = Boolean.parseBoolean(config.getProperty("compression"));
                int levels = Integer.parseInt(config.getProperty("levels"));
                int playerLives = Integer.parseInt(config.getProperty("playerLives"));
                // On va tester ici dans la future
                int bombBagCapacity = Integer.parseInt(config.getProperty("bombBagCapacity"));
                int monsterVelocity = Integer.parseInt(config.getProperty("monsterVelocity"));
                int playerInvisibilityTime = Integer.parseInt(config.getProperty("playerInvisibilityTime"));
                int monsterInvisibilityTime = Integer.parseInt(config.getProperty("monsterInvisibilityTime"));

                // Success for the player position initial '0x0' '1x1' '12x5'
                StringBuffer tmpx = new StringBuffer();
                StringBuffer tmpy = new StringBuffer();
                int ind = 0;
                while(config.getProperty("player").charAt(ind) != 'x'){
                    tmpx.append(config.getProperty("player").charAt(ind));
                    ind++;
                }
                ind++;
                for(int i = ind; i <config.getProperty("player").length();i++ ){
                    tmpy.append(config.getProperty("player").charAt(i));
                }
                int xPlayer = Integer.parseInt(tmpx.toString());
                int yPlayer = Integer.parseInt(tmpy.toString());

                // For the detail of the maps
                String string = config.getProperty("level1");
                Configuration configuration = new Configuration(new Position(xPlayer,yPlayer),bombBagCapacity,playerLives,playerInvisibilityTime,monsterVelocity,monsterInvisibilityTime);
                Game game = new Game(configuration,new Level(GameLauncher.load(string)));
                // Save all maps in the game
                game.stringMaps = new String[levels];
                for(int i = 0; i < levels; i++){
                    game.stringMaps[i] = config.getProperty("level"+(i+1));
                }
                modeScore = false;
                GameEngine engine = new GameEngine(game, stage,1,modeScore);
                engine.start();

            }
        });


        defaultItem.setOnAction(e -> {
            Game game = GameLauncher.load();
            modeScore = false;
            GameEngine engine = new GameEngine(game, stage,1,modeScore);
            engine.start();
        });

        // ScoreMode
        scoreModeItem.setOnAction(e -> {
            Game game = GameLauncher.loadScore();
            modeScore = true;
            GameEngine engine = new GameEngine(game, stage,1,modeScore);
            engine.start();
        });


        // Exit
        exitItem.setOnAction(e -> System.exit(0));

    }


}
