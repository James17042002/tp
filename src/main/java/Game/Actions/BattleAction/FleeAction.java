package Game.Actions.BattleAction;

import Exceptions.RolladieException;
import Functionalities.Storage;
import Functionalities.UIHandler;
import Game.Game;

public class FleeAction extends BattleAction {
    @Override
    public void executeBattleAction(Game game, Storage storage, UIHandler ui) throws RolladieException {
        String message = game.flee();
        ui.printMessage(message);
    }
}
