package battleship.handling;

import java.io.IOException;

import battleship.players.ComputerOpponent;
import battleship.players.CustomComputerOpponent;
import battleship.players.Human;
import battleship.players.Player;
import battleship.utilities.Coordinates;
import battleship.utilities.Input;
import battleship.utilities.StringManipulation;

public class GameHandler {

    private static Player	playerOne;
    private static Player	playerTwo;
    private static boolean	simultaneousTurns;
    private static boolean	showBothFleets;
    private static boolean	passiveSpectatorMode = false;
    private static boolean	gameIsOver;
    private static boolean	confirmTurnBeginning;

    /**
     * If your console output is too wide, you can do any of the following:
     * 
     * 1. Right-click the console, click "Preferences", or
     * 2. (Windows) Go through Window -> Preferences -> Run/Debug -> Console, or
     * 3. (Mac) Go through Preferences -> Run/Debug:
     * 
     * Lower "Displayed tab width". A value of 2 is recommended. 	 
     */

    /**
     * Initializes the game and runs it until one player wins.
     * 
     * @throws Exception
     */
    public static void main(String[] arguments) throws Exception {

        // ------------------------------------------------
        // Prepare utilities.
        // ------------------------------------------------

        Input.initialize();
        
        // ------------------------------------------------
        // Offer a dialogue that lets the user select the number of players,
        // and whether the game should wait for input before starting a new turn. 
        // ------------------------------------------------

        System.out.println("How many human players do you want? (0-2)");
        int playerNumber = Input.nextInt();

        if (playerNumber == 0) {
            playerOne = new CustomComputerOpponent("MYBEST");
            playerTwo = new ComputerOpponent("Perfidious Albion");
            showBothFleets = true;
            passiveSpectatorMode = true;
        } else if (playerNumber == 1) {
            playerOne = new Human("You");
            playerTwo = new ComputerOpponent("Perfidious Albion");
            showBothFleets = false;
        } else if (playerNumber == 2) {
            playerOne = new Human("Player 1");
            playerTwo = new Human("Player 2");
            showBothFleets = false;
        } else {
            throw new Exception("Invalid number of players specified (" + playerNumber + ").");
        }

        System.out.println("Do you want to print new turns after pressing a key, or as soon as they begin? (true/false)");
        confirmTurnBeginning = Input.nextBoolean();

        playerOne.placeShips();
        playerTwo.placeShips();

        simultaneousTurns = false;

        // ------------------------------------------------
        // Start playing!
        // ------------------------------------------------
        System.out.println("The battle between " + playerOne + " and " + playerTwo + " begins!");
        playUntilTheEnd();
    }

    // ----------------------------------------------------------------------------------------------------
    // Running the game
    // ----------------------------------------------------------------------------------------------------

    /**
     * Makes the players play turn by turn until either one of them loses.
     */
    private static void playUntilTheEnd() throws Exception {
        System.out.println("You have " + Settings.MAXIMUM_PLAYABLE_TURNS + " turns to end this!");
        for (int turns = 0; turns < Settings.MAXIMUM_PLAYABLE_TURNS; turns++) {
            if (gameIsOver) 
                return;
            System.out.println((Settings.MAXIMUM_PLAYABLE_TURNS - turns) + " turns remaining.");
            playOneTurn();
        }
        throw new Exception("Maximum number of turns (" + Settings.MAXIMUM_PLAYABLE_TURNS + ") exceeded!");
    }

    /**
     * Makes both players play one turn.
     */
    private static void playOneTurn() throws Exception {
        if (confirmTurnBeginning) {
            System.out.println("Press a key to start this turn.");
            Input.waitForAnyInput();
        }
        if (passiveSpectatorMode) {
            drawTheFleetsAsSeenByPlayer(playerOne);
            playerOne.yourTurnHasBegun();
            openFire(playerOne);
            endTheGameIfEitherPlayerIsDefeated();
            if (!gameIsOver) {
                drawTheFleetsAsSeenByPlayer(playerTwo);
                playerTwo.yourTurnHasBegun();
                openFire(playerTwo);
                endTheGameIfEitherPlayerIsDefeated();
            }
        } else {
            drawThePlayerSeparatorIfTwoHumansArePlaying(playerOne);
            if (!playerOne.isAnAIPlayer() || showBothFleets)
                drawTheFleetsAsSeenByPlayer(playerOne);
            drawAFancyLine();
            playerOne.yourTurnHasBegun();
            openFire(playerOne);
            if (!simultaneousTurns)
                endTheGameIfEitherPlayerIsDefeated();

            if (!gameIsOver) {
                drawThePlayerSeparatorIfTwoHumansArePlaying(playerTwo);
                if (!playerTwo.isAnAIPlayer())
                    drawTheFleetsAsSeenByPlayer(playerTwo);
                drawAFancyLine();
                playerTwo.yourTurnHasBegun();
                openFire(playerTwo);
                endTheGameIfEitherPlayerIsDefeated();
            }
        }
    }

    private static void endTheGameIfEitherPlayerIsDefeated() {
        if (playerOne.isDefeated() || playerTwo.isDefeated()) {
            gameIsOver = true;
            proclaimTheWinner();
        }
    }

    private static void drawTheFleetsAsSeenByPlayer(Player currentlyActivePlayer) {
        Player inactivePlayer = (currentlyActivePlayer == playerOne ? playerTwo : playerOne);

        // Add empty lines to visually separate the playing field from other messages
        drawAFancyLine();
        System.out.println("It is " + currentlyActivePlayer + "'s turn.");
        System.out.println(
                "Fleet strength: " + currentlyActivePlayer + " at " + currentlyActivePlayer.currentFleetStrength() + "/"
                        + currentlyActivePlayer.originalFleetStrength() + ", " + inactivePlayer + " at "
                        + inactivePlayer.currentFleetStrength() + "/" + inactivePlayer.originalFleetStrength() + ".");
        System.out.println(currentlyActivePlayer.getAccuracyInformation());
        drawAFancyLine();
        
        String headline = StringManipulation.generateFleetRepresentationHeadingLine() 
                + StringManipulation.TAB + StringManipulation.TAB
                + StringManipulation.generateFleetRepresentationHeadingLine();
        System.out.println(headline);

        for (int lineNumber = 0; lineNumber < Settings.PLAYING_FIELD_VERTICAL_SIZE; lineNumber++) {
            String line = currentlyActivePlayer.generateOneLineOfTheVisualFleetRepresentation(lineNumber, true)
                    + StringManipulation.TAB + StringManipulation.TAB
                    + inactivePlayer.generateOneLineOfTheVisualFleetRepresentation(lineNumber, showBothFleets);
            System.out.println(line);
        }
    }

    private static void drawThePlayerSeparatorIfTwoHumansArePlaying(Player nextActivePlayer) throws IOException {
        // Draw a hundred blank lines to push away the previous turn
        for (int i = 0; i < 100; i++)
            System.out.println();
        if (playerSeparatorScreenNeeded())
            drawThePlayerSeparator(nextActivePlayer);
        else {
            System.out.println();
            System.out.println();
        }
    }

    private static boolean playerSeparatorScreenNeeded() {
        return showBothFleets == false && playerOne.isAnAIPlayer() == false && playerTwo.isAnAIPlayer() == false;
    }

    private static void drawThePlayerSeparator(Player nextActivePlayer) throws IOException {
        System.out.println("Admiral " + nextActivePlayer + ", to the bridge!");
        Input.waitForAnyInput();
    }

    private static void proclaimTheWinner() {
        Player winner = null;
        if (playerOne.isDefeated() && !playerTwo.isDefeated()) {
            winner = playerTwo;
        } else if (playerTwo.isDefeated() && !playerOne.isDefeated()) {
            winner = playerOne;
        } else if (playerOne.isDefeated() && playerTwo.isDefeated()) {
            drawAFancyLine();
            System.out.println("Both fleets have been sunk. The fight is a draw!");
            drawAFancyLine();
            return;
        } else if (!playerOne.isDefeated() && !playerTwo.isDefeated()) {
            drawAFancyLine();
            System.out.println("Both fleets have depleted their munitions! The fight is a draw!");
            drawAFancyLine();
            return;
        }
        drawAFancyLine(60, 3, 3);
        System.out.println("RULE THE WAVES! " + winner + " has won the battle!");
        drawAFancyLine(60, 3, 3);
    }

    public static void drawAFancyLine() {
        drawAFancyLine(60, 1, 1);
    }

    public static void drawAFancyLine(int length, int blankSurroundingLines, int fancyLines) {
        String fancyLine = "";
        for (int i = 0; i < length; i++)
            fancyLine += "~";
        for (int i = 0; i < blankSurroundingLines; i++)
            System.out.println();
        for (int i = 0; i < fancyLines; i++) 
            System.out.println(fancyLine);
        for (int i = 0; i < blankSurroundingLines; i++) 
            System.out.println();
    }


    // ----------------------------------------------------------------------------------------------------
    // Player Actions
    // ----------------------------------------------------------------------------------------------------

    private static void openFire (Player attacker) throws Exception {
        fireAtCoordinates (attacker, attacker.promptToFireShot());
    }

    private static void fireAtCoordinates(Player attacker, Coordinates coordinates) {
        Player defender = (attacker == playerOne ? playerTwo : playerOne);
        defender.receiveFireAtCoordinates(attacker, coordinates);
    }
}
