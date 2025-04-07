/*
 * This source file was generated by the Gradle 'init' task
 */

import java.util.Scanner;

import Characters.Abilities.*;
import Characters.Players.Player;
import Equipment.*;
import Functions.DiceBattleAnimation;
import Functions.Pair;
import Functions.Storage;
import Functions.TypewriterEffect;
import UI.BattleDisplay;
import UI.HpBar;
import UI.Narrator;

public class Rolladie {
    public String getGreeting() {
        return "Hello World!";
    }
    
    public static void main(String[] args) throws InterruptedException {
        mainMenu();
    }

    /**
     * Starts the game menu and shows options for new game or loading from save
     */
    public static void mainMenu() throws InterruptedException {
        Scanner scanner = new Scanner(System.in);
        System.out.println("=== WELCOME TO ROLLaDIE ===");
        System.out.println("1. Start New Game");
        System.out.println("2. Load Game");
        System.out.print("Select an option: ");
        String input = scanner.nextLine();

        Player player;
        int wave = 1;

        if (input.equals("1")) {
            // Uncomment to view new game story!
            // Narrator.newGameSequence();
            player = createNewPlayer(scanner);
            wave = 1;
        } else if (input.equals("2")) {
            Pair<Player, Integer> loaded = Storage.loadGame(scanner);
            if (loaded != null) {
                player = loaded.first;
                wave = loaded.second;
            } else {
                System.out.println("Starting new game instead.");
                // Narrator.newGameSequence();
                player = createNewPlayer(scanner);
                wave = 1;
            }
        } else {
            System.out.println("Invalid option, starting new game.");
            // Narrator.newGameSequence();
            player = createNewPlayer(scanner);
            wave = 1;
        }

        startGameLoop(player, wave, scanner);
    }

    /**
     * Creates a new human player when starting from new game
     * 
     * @param scanner
     * @return Player character
     */
    public static Player createNewPlayer(Scanner scanner) {
        System.out.print("Enter your hero's name: ");
        String name = scanner.nextLine();
    
        // todo: choose character class to vary these starting stats
        Player player = new Player(name, 100, 5, 2, new FlamingSword(), new DragonShield(), true);
        player.abilities.add(new BasicAttack());
        player.abilities.add(new PowerStrike());
        player.abilities.add(new Heal());
    
        return player;
    }
    
    /**
     * Main game loop logic
     * 
     * @param player1 player character
     * @param wave the number of enemies encountered so far
     * @param scanner
     * @throws InterruptedException
     */
    public static void startGameLoop(Player player1, int wave, Scanner scanner) throws InterruptedException {
        Player player2 = new Player("AI Bot", 60, 3, 3, new Weapon("Axe", 1), new Armor("Mail", 2), false);

        while (player1.isAlive()) {
            System.out.println("🌊 Encounter " + wave + " begins!");

            if (!player2.isAlive()) {
                player2 = generateNewEnemy(wave); // todo make tougher per wave
            }
            startBattle(player1, player2);

            if (!player1.isAlive()) {
                TypewriterEffect.print("💀 You fell at encounter " + wave, 1000);
                break;
            }

            // Heal partially, recharge power
            System.out.println("🍃 You survived! Regaining strength...");
            player1.resetAllCooldowns();
            player1.hp = Math.min(player1.maxHp, player1.hp + 10);
            player1.power = Math.min(player1.maxPower, player1.power + 20);

            wave++;

            if (wave == 3 && !player1.hasAbility("Whirlwind")) {
                player1.abilities.add(new Whirlwind());
                TypewriterEffect.print("🔥 You’ve learned Whirlwind!", 1000);
            }

            if (wave == 5) {
                player1.weapon = new Weapon("Flame Blade", 5);
                TypewriterEffect.print("🗡️ You obtained the Flame Blade!", 1000);
            }

            System.out.print("💾 Save game? (y/n): ");
            String saveInput = scanner.nextLine();
            if (saveInput.equalsIgnoreCase("y")) {
                Storage.saveGame(player1, wave, scanner);
            }
        }
    }

    /**
     * Creates a new enemy when the previous one is defeated, increasing difficulty as wave progresses
     */
    public static Player generateNewEnemy(int wave) {
        Weapon claws = new Weapon("Claws", 2 + wave);
        Armor hide = new Armor("Hide", 1 + wave / 2);
        Player enemy = new Player("Enemy " + wave, 20 + wave * 30, (3 + wave) / 2, 3, claws, hide, false);
    
        enemy.abilities.add(new PowerStrike());
        if (wave >= 2) enemy.abilities.add(new Heal());
        if (wave >= 3) enemy.abilities.add(new Crush());
    
        return enemy;
    }


}
