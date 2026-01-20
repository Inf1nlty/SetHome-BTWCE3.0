package btw.community.sethome;

import api.AddonHandler;
import api.BTWAddon;
import com.inf1nlty.sethome.command.*;

public class HomeAddon extends BTWAddon {
    @Override
    public void initialize() {
        AddonHandler.logMessage(getName() + " v" + getVersionString() + " Initializing...");
        AddonHandler.registerCommand(new SetHomeCommand(), false);
        AddonHandler.registerCommand(new HomeCommand(), false);
        AddonHandler.registerCommand(new ListHomeCommand(), false);
        AddonHandler.registerCommand(new ShareHomeCommand(), false);
        AddonHandler.registerCommand(new DelHomeCommand(), false);

        AddonHandler.registerCommand(new SetWarpCommand(), false);
        AddonHandler.registerCommand(new WarpCommand(), false);
        AddonHandler.registerCommand(new ListWarpCommand(), false);
        AddonHandler.registerCommand(new ShareWarpCommand(), false);
        AddonHandler.registerCommand(new DelWarpCommand(), false);

        AddonHandler.registerCommand(new TPACommand(), false);
        AddonHandler.registerCommand(new BackCommand(), false);
    }
}