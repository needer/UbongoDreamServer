package com.ubongo.gameHolder;

import com.google.gson.Gson;
import com.ubongo.dataTransfer.Response;
import com.ubongo.game.Game;
import com.ubongo.player.Player;

import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class GameHolder {
    private static GameHolder ourInstance = new GameHolder();
    private int uniqueId = 1001;
    private Gson gson;
    private HashMap<String, Game> games = new HashMap<String, Game>();

    public static GameHolder getInstance() {
        return ourInstance;
    }

    private GameHolder() {
        gson = new Gson();
    }

    public void startLobby(String name, PrintWriter out, int callId) {
        /**do not add into the vector
         just generate pin and return that
         */
        System.out.println("\nCalled method: 'Start Lobby'.  Arguments: Name " + name);
        String pin = generateUniqueId();
        Game game = new Game();
        games.put(pin, game);
        Response response = new Response(callId, pin);
        String json = gson.toJson(response);
        out.println(json);
        out.flush();
        System.out.println("Done ...");
    }

    public void joinPlayer(String name, String pin, PrintWriter out, int callId) {
        /**keep the name in ArrayList
         notify everyone already in the game by returning ArrayList of names
         */
        System.out.println("\nCalled method: 'Join Player'. Arguments: Name " + name + ", Pin " + pin);
        Game game = games.get(pin);
        if (game == null){
            System.out.println("Could NOT find such game. Existing games at this moment");
            for(Map.Entry m : games.entrySet()){
                System.out.println(m.getKey()+" "+m.getValue().toString());
            }
            out.println("");
            out.flush();
        }
        else{
            System.out.print("Game exists. Current players are ...  ");
            for(Player player : game.getPlayers()){
                System.out.print(player.getName() + ".. ");
            }
            System.out.println("\nAdding a new player. Name: " + name);
            game.addPlayer(name, out);

            System.out.println("Notifying everyone");
            ArrayList<String> playersName = new ArrayList<String>();
            for(Player player : game.getPlayers()){
                playersName.add(player.getName());
            }
            Response response = new Response(callId, playersName.toString());
            String json = gson.toJson(response);
            for(Player player : game.getPlayers()){
                player.getOut().println(json);
                player.getOut().flush();
            }
        }
        System.out.println("Done ...");
    }


    public void finishGame(String name, String pin, PrintWriter out, int callId){
        /**
         * send the winner name to everyone
         * after the call check the if empty delete the game.
         */
        System.out.println("\nCalled method: 'Finish Game. Arguments: Name " + name + ", Pin " + pin);
        Game game = games.get(pin);
        if (game == null){
            System.out.println("Could NOT find such game. Existing games at this moment");
            for(Map.Entry m : games.entrySet()){
                System.out.println(m.getKey()+" "+m.getValue().toString());
            }
            out.println("");
            out.flush();
        }
        else{
            System.out.print("Game exists. Current players are ...  ");
            for(Player player : game.getPlayers()){
                System.out.print(player.getName() + ".. ");
            }

            System.out.println("\nNotifying everyone");
            Response response = new Response(callId, name);
            String json = gson.toJson(response);
            for(Player player : game.getPlayers()){
                player.getOut().println(json);
                player.getOut().flush();
            }

            System.out.println("Deleting the Game");
            games.remove(pin); // perhaps another action should be done here
        }
        System.out.println("Done ...");
    }

    public void leaveGame(String name, String pin, PrintWriter out, int callId){
        /**
         * take out this guy out of the Array List
         * after the call check the players vector, if empty delete the game
         */
        System.out.println("\nCalled method: 'Leave Game'. Arguments: Name " + name + ", Pin " + pin);
        Game game = games.get(pin);
        if (game == null){
            System.out.println("Could NOT find such game. Existing games at this moment");
            for(Map.Entry m : games.entrySet()){
                System.out.println(m.getKey()+" "+m.getValue().toString());
            }
            out.println("");
            out.flush();
        }
        else{
            System.out.print("Game exists. Current players are ...  ");
            for(Player player : game.getPlayers()){
                System.out.print(player.getName() + ".. ");
            }

            System.out.println("\nDeleting the player .. " + name);
            for(Player player : game.getPlayers()){
                if (player.getName().equals(name)) {
                    game.getPlayers().remove(player);
                    break;
                }
            }
            Response response = new Response(callId, name);
            String json = gson.toJson(response);

            System.out.println("Checking and deleting the Game if there is NO player");
            if (game.getPlayers().size() == 0)
                games.remove(pin);
        }
        System.out.println("Done ...");
    }

    public void startGame(String pin, PrintWriter out, int callId){
        /**
         * status set started
         * send all the players that started
         * generate random number within that difficulty range: board id
         */
        System.out.println("\nCalled method: 'Start Game'. Arguments: Pin " + pin);
        Game game = games.get(pin);
        if (game == null){
            System.out.println("Could NOT find such game. Existing games at this moment");
            for(Map.Entry m : games.entrySet()){
                System.out.println(m.getKey()+" "+m.getValue().toString());
            }
            out.println("");
            out.flush();
        }
        else{
            System.out.print("Game exists. Current players are ...  ");
            for(Player player : game.getPlayers()){
                System.out.print(player.getName() + ".. ");
            }

            System.out.println("\nStarting the game");
            game.setStatus(true);

            System.out.println("Notifying everyone");
            Response response = new Response(callId, "Status " + game.getStatus());
            String json = gson.toJson(response);
            for(Player player : game.getPlayers()){
                player.getOut().println(json);
                player.getOut().flush();
            }
        }
        System.out.println("Done ...");
    }

    public void removePlayer(String name, String pin, PrintWriter out, int callId){
        /**
         * take out this guy out of the vector
         * after the call check the players vector, if empty delete the game
         * if not empty then notify all the players send ArrayList<String>
         *     if owner removes, notify all the players (some key value)
         */
        System.out.println("\nCalled method: 'Remove Player'. Arguments: Name " + name + ", Pin " + pin);
        Game game = games.get(pin);
        if (game == null){
            System.out.println("Could NOT find such game. Existing games at this moment");
            for(Map.Entry m : games.entrySet()){
                System.out.println(m.getKey()+" "+m.getValue().toString());
            }
            out.println("");
            out.flush();
        }
        else{
            System.out.print("Game exists. Current players are ...  ");
            for(Player player : game.getPlayers()){
                System.out.print(player.getName() + ".. ");
            }
            // TBA
            Response response = new Response(callId,  "");
            String json = gson.toJson(response);
        }
        System.out.println("Done ...");
    }


    public void setDifficulty(String difficult, String pin, PrintWriter out, int callId){
        /** diff: int range 0-2. pin not sure.
         no need to check owner or not. limited from ui
        Set difficulty, and send all clients **/
        System.out.println("\nCalled method: 'Set Difficulty'. Arguments: Difficulty " + difficult + ", Pin " + pin);
        Game game = games.get(pin);
        if (game == null){
            System.out.println("Could NOT find such game. Existing games at this moment");
            for(Map.Entry m : games.entrySet()){
                System.out.println(m.getKey()+" "+m.getValue().toString());
            }
            out.println("");
            out.flush();
        }
        else{
            System.out.print("Game exists. Current players are ...  ");
            for(Player player : game.getPlayers()){
                System.out.print(player.getName() + ".. ");
            }

            System.out.println("\nSetting the difficulty .. " + difficult);
            game.setDifficulty(Integer.parseInt(difficult));

            System.out.println("Notifying everyone");
            Response response = new Response(callId, difficult);
            String json = gson.toJson(response);
            for(Player player : game.getPlayers()){
                player.getOut().println(json);
                player.getOut().flush();
            }
        }
        System.out.println("Done ...");
    }

    /** helper **/
    private String generateUniqueId(){
        //String uniqueID = UUID.randomUUID().toString();
        return Integer.toString(uniqueId++);
    }

}
